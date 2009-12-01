package com.magicval.controller.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.magicval.model.card.Card;

/**
 * MagicTradersSearcher performs a card information search on the website
 * http://magictraders.com. This data is used for finding single cards and
 * performing queries.
 * @author Paul Gibler
 *
 */
public class MagicTradersSearcher implements Searcher<Card> {
	
	private String domain = "http://magictraders.com";
	private String path = "/cgi-bin/query.cgi";
	private String uri = domain + path;

	private HttpClient client;
	private HttpUriRequest request;
	
	/**
	 * Constructor for MagicTradersSearcher.
	 */
	public MagicTradersSearcher() {
		// Prepare HTTP request
		client = new DefaultHttpClient();
		pattern = Pattern.compile("(.*?) (\\(\\w*\\))");
	}
	
	@Override
	public ArrayList<Card> searchFor(String search) throws IOException {
		String urlWithQuery = uri + "?list=magic&field=0&operator=re&target="+search.replaceAll(" ", "+");
		
		// Target is the search query we search for cards on.
		request = new HttpGet(urlWithQuery);
		
		// Now execute it
		HttpResponse hr = client.execute(request);
		
		// Get contents of response
		InputStream stream = hr.getEntity().getContent();
		
		// And then parse
		return fastRead(stream);
	}

	@Override
	public Card searchForClosestMatch(String search) throws IOException {
		// This method uses an extremely naive way of finding the closest match.
		// It simply performs a string comparison of the search against the
		// beginning of each string that matched.
		
		ArrayList<Card> cards = searchFor(search);
		Card bestMatch = null;
		String bestMatchName = null;
		String compareName = null;
		int strlen = search.length();
		int closeness = 0;
		int currCloseness = 0;
		// We use a simple string comparison to find the closest match.
		for(Card c : cards)
		{
			if(bestMatch != null)
			{
				compareName = c.getName();
				if(compareName.length() < strlen)
				{
					continue;
				}
				compareName = compareName.substring(0, strlen);
				currCloseness = Math.abs(search.compareTo(compareName));
				if(currCloseness < closeness)
				{
					bestMatch = c;
					bestMatchName = c.getName();
					if(currCloseness == 0)
					{
						break;
					}
					closeness = currCloseness;
				}
			} else {
				bestMatch = c;
				bestMatchName = c.getName();
				closeness = Math.abs(search.compareTo(bestMatchName));
			}
		}
		return bestMatch;
	}
	
	/**
	 * Reads fast but is very dependent on the resulting content maintaining its format.
	 * @param stream The input stream to read the cards in from.
	 * @return A list of cards from the stream.
	 * @throws IOException
	 */
	private ArrayList<Card> fastRead(InputStream stream) throws IOException
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
	 * of a card search on magictraders.
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
	
	Pattern pattern;
	
	private String generateRealName(String foundName) {
		Matcher m = pattern.matcher(foundName);
		if(m.find())
		{
			return m.group(1);
		} else {
			return foundName;
		}
	}

}
