package com.magicval.model.image;

import java.util.ArrayList;

/**
 * ImageLoaderSources gives access to all ImageLoaders for a particular
 * type that can have images of it loaded.
 * @author Paul Gibler
 *
 * @param <E>
 */
public interface ImageLoaderSources<E> {
	/**
	 * Returns an instance of each type of ImageLoader that can
	 * load an image for the generic type.
	 * @return An instance of each type of ImageLoader that can
	 * load an image for the generic type.
	 */
	public ArrayList<ImageLoader<E>> getImageLoaders();
}
