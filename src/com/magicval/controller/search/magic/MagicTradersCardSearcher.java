package com.magicval.controller.search.magic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.magicval.controller.search.NoMatchFoundException;
import com.magicval.controller.search.Searcher;
import com.magicval.model.card.Card;

/**
 * MagicTradersSearcher performs a card information search on the website
 * http://magictraders.com. This data is used for finding single cards and
 * performing queries.
 * @author Paul Gibler
 *
 */
public class MagicTradersCardSearcher implements Searcher<Card> {
	
	private String domain = "http://magictraders.com";
	private String path = "/cgi-bin/query.cgi";
	private String uri = domain + path;

	private HttpClient client;
	private HttpUriRequest request;
	
	/**
	 * Constructor for MagicTradersSearcher.
	 */
	public MagicTradersCardSearcher() {
		// Prepare HTTP request
		client = new DefaultHttpClient();
	}
	
	public ArrayList<Card> searchFor(String search) throws IOException {
		InputStream stream = getSearchStream(search);
		
		// And then parse
		return fastRead(stream);
	}

	public Card searchForClosestMatch(String search) throws IOException, NoMatchFoundException {
		// This method uses an extremely naive way of finding the closest match.
		// It just returns the first card found.
		
		InputStream stream = getSearchStream(search);
		
		ArrayList<Card> cards = fastRead(stream, 1);
		if(cards.size() == 0)
		{
			throw new NoMatchFoundException("No card matched the search string '"+search+"'.");
		}
		return cards.get(0);
	}
	
	private ArrayList<Card> fastRead(InputStream stream) throws IOException {
		return fastRead(stream, -1);
	}
	
	/**
	 * Reads fast but is very dependent on the resulting content maintaining its format.
	 * @param stream The input stream to read the cards in from.
	 * @param numberToRead The number of cards to read in. If -1, reads in until there are none left.
	 * @return A list of cards from the stream.
	 * @throws IOException Throws this exception if a connection issue interrupts the search.
	 */
	private ArrayList<Card> fastRead(InputStream stream, int numberToRead) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		List<Card> cardsTemp = new LinkedList<Card>();
		
		String line;
		boolean readyToRead = false;
		
		while((line = br.readLine()) != null)
		{
			if(readyToRead)
			{
				try {
					// We decrement so that we only read in the correct number of cards.
					if(numberToRead != -1) {
						if(numberToRead <= 0) {
							break;
						}
						numberToRead--;
					}
					// We attempt to parse the line and add it to the card list.
					cardsTemp.add(generateCard(line));
				} catch(Exception e) {
					// If we fail (which means we are likely done with reading cards) we can simply break out.
					break;
				}
			} 
			else
			{
				// This is why we are playing with fire. This is how the line
				// before the card prices begins. I'm hoping this stays where it is.
				if(line.startsWith("----"))
				{
					readyToRead = true;
				}
			}
		}
		br.close();
		return new ArrayList<Card>(cardsTemp);
	}
	
	/**
	 * Generates an instance of Card from a line that comes from the output
	 * of a card search on MagicTraders.
	 * @param line The line of text to be read in and made into a Card.
	 * @return An instance of a Card. An array indexing error will be thrown if the line is not correctly formatted.
	 */
	private Card generateCard(String line) {
		String[] parts = line.split(",");
		String front = parts[1].trim();
		try {
			// If the "front" is a number, that means there is no comma in the card name.
			// If this is the case, the first index of the parts array is the entire name,
			// since no part of the name appears after the comma.
			Double.valueOf(front);
			String name = parts[0].trim();
			return new Card(generateRealName(name), name, front, parts[4].trim(), parts[5].trim());
		} catch(Exception e) {
			String name = parts[0] + "," + parts[1];
			String realName = generateRealName(name);
			return new Card(realName, name, parts[2].trim(), parts[5].trim(), parts[6].trim());
		}
	}
	
	private String generateRealName(String foundName)
	{
		int i = 0;
		// We iterate over the characters to see if we encounter a set indicator.
		// We want to return the substring of foundName of everything before the
		// set indicator and the space character preceding it. This will run
		// faster than a regular expression.
		while(i < foundName.length())
		{
			char current = foundName.charAt(i);
			if(current == ' ')
			{
				// This means we see the beginning of a set indicator in the card name.
				// If this is the case, we just return everything we've seen before it.
				if(foundName.charAt(i+1) == '(') {
					break;
				}
			}
			i++;
		}
		return foundName.substring(0, i);
	}
	
	private InputStream getSearchStream(String search) throws IOException {
		String urlWithQuery = uri + "?list=magic&field=0&operator=re&target="+search.trim().replaceAll(" ", "+");
		
		// Target is the search query we search for cards on.
		request = new HttpGet(urlWithQuery);
		
		// Now execute it
		HttpResponse hr = client.execute(request);
		
		// Get contents of response
		return hr.getEntity().getContent();
	}
}
