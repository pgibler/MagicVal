package com.magicval.image;

import java.io.IOException;

import android.graphics.Bitmap;

/**
 * Loads a image from a class instance.
 * @author Paul Gibler
 *
 */
public interface ImageLoader<E> {
	/**
	 * Lazily loads an instances image.
	 * @param e The instance whose image is being loaded.
	 * @return A Bitmap image of the card.
	 * @throws IOException If there is an error loading the card image, this error is thrown.
	 */
	public Bitmap getImage(E image) throws IOException;
	
}
