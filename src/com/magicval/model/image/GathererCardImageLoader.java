package com.magicval.model.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.magicval.model.card.Card;

public class GathererCardImageLoader implements ImageLoader<Card> {

	// Maybe I'll use the imageURL as a default if the card image is not loaded...
	//private String imageURL = "http://upload.wikimedia.org/wikipedia/en/thumb/a/aa/Magic_the_gathering-card_back.jpg/250px-Magic_the_gathering-card_back.jpg";
	//private String cacheBust = "&cacheBust=1259350413520";
	private String searchURL = "http://gatherer.wizards.com/Handlers/InlineCardSearch.ashx?nameFragment=";
	private String cardSearchURL = "http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=";
	
	private HttpClient client;
	private HttpUriRequest request;
	
	public GathererCardImageLoader() {
		// Prepare HTTP request
		client = new DefaultHttpClient();
	}
	
	@Override
	public Bitmap getImage(Card card) throws IOException {
		String urlCardName = card.getNameForURL();
		JSONObject jo = searchFor(urlCardName);
		try
		{
			JSONArray resultsArray = jo.getJSONArray("Results");
			JSONObject firstResult = (JSONObject)resultsArray.get(0);
			String cardID = firstResult.getString("ID");
			String search = cardSearchURL + cardID;
			return loadImage(search);
		} catch(JSONException e) {
			throw new IOException("Could not read in card image properly.");
		}
	}
	
	private JSONObject searchFor(String search) throws IOException {
		String urlWithQuery = searchURL + search;
		
		// Target is the search query we search for cards on.
		request = new HttpGet(urlWithQuery);
		
		// Now execute it
		HttpResponse hr = client.execute(request);
		
		// Get contents of response
		InputStream stream = hr.getEntity().getContent();
		
		// And then parse
		return readAll(stream);
	}
	
	private Bitmap loadImage(String search) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(search).openConnection();
		conn.setDoInput(true);
		conn.connect();
		InputStream inStream = conn.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(inStream);
		inStream.close();
		conn.disconnect();
		return bitmap;
	}
	
	private JSONObject readAll(InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String st = "";
		String line;
		while((line = br.readLine()) != null)
		{
			st += line;
		}
		br.close();
		try {
			return new JSONObject(st);
		}
		catch(JSONException e)
		{
			throw new IOException("Could not read in JSONObject correctly.");
		}
	}
}
