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
import android.graphics.Color;

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
			Bitmap b = loadImage(search);
			b = removeWhiteFast(b);
			return b;
		} catch(JSONException e) {
			throw new IOException("Could not read in card image properly.");
		}
	}
	
	private Bitmap removeWhiteFast(Bitmap b) {
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
	
	// We don't use this method but it has proven to be useful.
	// Maybe some other time.
	@SuppressWarnings("unused")
	private Bitmap removeWhite(Bitmap b) {
		b = b.copy(b.getConfig(), true);
		int bWidth = b.getWidth()-1;
		int bHeight = b.getHeight()-1;
		int x = 0;
		int y = 0;
		int currentPixel;
		while(y < bHeight)
		{
			// Remove the non-black pixels both ways.
			while(x < bWidth)
			{
				// Alter current pixel.
				b.setPixel(x,y,Color.TRANSPARENT);
				// Alter pixel opposite horizontally, same y.
				b.setPixel(bWidth-x,y,Color.TRANSPARENT);
				// Alter pixel opposite vertically, same x.
				b.setPixel(x,bHeight-y,Color.TRANSPARENT);
				// Alter pixel opposite horizontally and vertically.
				b.setPixel(bWidth-x,bHeight-y,Color.TRANSPARENT);
				
				x++;
				
				currentPixel = b.getPixel(x, y);
				
				// If we see a black pixel, go to the next row.
				if(x >= bWidth || currentPixel < Color.GRAY)
				{
					break;
				}
			}
			x = 0;  
			y++;
			
			currentPixel = b.getPixel(x, y);
			
			if(y >= bHeight || currentPixel < Color.GRAY)
			{
				break;
			}
		}
		return b;
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
