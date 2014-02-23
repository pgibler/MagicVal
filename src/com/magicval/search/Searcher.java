package com.magicval.search;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Parcelable;

/**
 * Searcher classes can pull arbitrary date from a source, and return
 * either best matches or lists of matches.
 * @author Paul Gibler
 *
 */
public interface Searcher<E extends Parcelable> {
	/**
	 * Generates an ArrayList of results from a search string.
	 * We use ArrayList since it is far easier to pass ArrayLists
	 * of Parcelable objects around in the Android app framework.
	 * Any leading or trailing whitespace will be removed.
	 * @param search A search to find cards on.
	 * @return A map of card names and their prices.
	 * @throws IOException Throws this exception if the retrieval fails.
	 */
	public ArrayList<E> searchFor(String search) throws IOException;
}
