package com.magicval.model.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.magicval.model.card.Card;

/**
 * Loads a Card image.
 * @author Paul Gibler
 *
 */
public class CardImageLoader {
	
	private String imageURL = "http://upload.wikimedia.org/wikipedia/en/thumb/a/aa/Magic_the_gathering-card_back.jpg/250px-Magic_the_gathering-card_back.jpg";
	
	/**
	 * Lazily loads a card image.
	 * @param card The card whose image is being loaded.
	 * @return A Bitmap image of the card.
	 * @throws IOException If there is an error loading the card image, this error is thrown.
	 */
	public Bitmap getCardImage(Card card) throws IOException {
		
		HttpURLConnection conn = (HttpURLConnection) new URL(imageURL).openConnection();
		conn.setDoInput(true);
		conn.connect();
		InputStream inStream = conn.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(inStream);
		inStream.close();
		conn.disconnect();
		
		return bitmap;
	}
	
}
