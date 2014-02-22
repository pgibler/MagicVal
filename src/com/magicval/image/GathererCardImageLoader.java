package com.magicval.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.magicval.card.MagicCard;

public class GathererCardImageLoader implements ImageLoader<MagicCard> {

	// Maybe I'll use the imageURL as a default if the card image is not loaded...
	private String defaultImageURL = "http://upload.wikimedia.org/wikipedia/en/thumb/a/aa/Magic_the_gathering-card_back.jpg/250px-Magic_the_gathering-card_back.jpg";
	private String searchURL = "http://gatherer.wizards.com/Handlers/InlineCardSearch.ashx?nameFragment=";
	private String cardSearchURL = "http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=";
	
	public GathererCardImageLoader() {
	}
	
	public Bitmap getImage(MagicCard card) throws IOException {
		String urlCardName = card.getNameForURL();
		JSONObject jo = searchFor(urlCardName);
		Bitmap b = null;
		try
		{
			JSONArray resultsArray = jo.getJSONArray("Results");
			JSONObject firstResult = (JSONObject)resultsArray.get(0);
			String cardID = firstResult.getString("ID");
			String search = cardSearchURL + cardID;
			b = loadImage(search);
			b = removeCardCornerFast(b);
		} catch(JSONException e) {
			b = loadImage(defaultImageURL);
			b = removeCardCornerFast(b);
		}
		return b;
	}
	
	private Bitmap removeCardCornerFast(Bitmap b) {
		b = b.copy(b.getConfig(), true);
		int bWidth = b.getWidth()-1;
		int bHeight = b.getHeight()-1;
		// r is the radius
		int r = bWidth/20;
		int x = 0;
		int y = 0;
		int yMax = 0;
		while(x < r)
		{
			// Remove the non-black pixels both ways.
			yMax = r - (int)Math.sqrt(2*x*r-x^2);
			while(y < yMax)
			{
				// Alter current pixel.
				b.setPixel(x,y,Color.TRANSPARENT);
				// Alter pixel opposite horizontally, same y.
				b.setPixel(bWidth-x,y,Color.TRANSPARENT);
				// Alter pixel opposite vertically, same x.
				b.setPixel(x,bHeight-y,Color.TRANSPARENT);
				// Alter pixel opposite horizontally and vertically.
				b.setPixel(bWidth-x,bHeight-y,Color.TRANSPARENT);
				
				y++;
			}
			y = 0;  
			x++;
		}
		return b;
	}

	private JSONObject searchFor(String search) throws IOException {
		String urlWithQuery = searchURL + search;
		
		// Target is the search query we search for cards on.
		URL url = new URL(urlWithQuery);
		URLConnection urlConnection = url.openConnection();
		InputStream stream = urlConnection.getInputStream();
		
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
