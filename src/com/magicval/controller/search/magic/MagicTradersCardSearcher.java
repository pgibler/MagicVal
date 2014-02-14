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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

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
	
	private String domain = "https://api.deckbrew.com";
	private String path = "/mtg/cards";
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
		try
		{
			return jsonRead(stream);
		} catch(JSONException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	private ArrayList<Card> jsonRead(InputStream json) throws IOException, JSONException
	{
		return jsonRead(json, -1);
	}
	
	private ArrayList<Card> jsonRead(InputStream stream, int numberToRead) throws IOException, JSONException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		List<Card> cardsTemp = new LinkedList<Card>();
		
		JSONArray array = new JSONArray( br.readLine() );
		
		for(int i = 0; i < array.length() && i < numberToRead; i++)
		{
			JSONObject item = array.getJSONObject(i);
			
			cardsTemp.add(generateCard(item));
		}
		
		br.close();
		return new ArrayList<Card>(cardsTemp);
	}
	
	private Card generateCard(JSONObject object) throws JSONException
	{
		String name = object.getString("name");
		String current = "$0.00";
		String high = "$0.00";
		String low = "$0.00";
		
		return new Card(name, current, high, low);
	}

	public Card searchForClosestMatch(String search) throws IOException, NoMatchFoundException {
		// This method uses an extremely naive way of finding the closest match.
		// It just returns the first card found.
		
		InputStream stream = getSearchStream(search);
		
		ArrayList<Card> cards;
		try {
			cards = jsonRead(stream, 1);
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
		
		if(cards.size() == 0)
		{
			throw new NoMatchFoundException("No card matched the search string '"+search+"'.");
		}
		return cards.get(0);
	}
	
	private InputStream getSearchStream(String search) throws IOException {
		String urlWithQuery = uri + "?name="+search.trim().replaceAll(" ", "+");
		
		// Target is the search query we search for cards on.
		request = new HttpGet(urlWithQuery);
		
		// Now execute it
		HttpResponse hr = client.execute(request);
		
		// Get contents of response
		return hr.getEntity().getContent();
	}
}
