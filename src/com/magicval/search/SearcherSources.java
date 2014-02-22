package com.magicval.search;

import java.util.ArrayList;

import android.os.Parcelable;

/**
 * SearcherSources maintains all Searcher types for the
 * particular Parcelable type. In this application, we use it
 * to search for Magic Cards, but in theory we could search for
 * anything we want, such as baseball cards.
 * 
 * @author Paul Gibler
 *
 * @param <E> The Parcelable type that can be searched for.
 */
public interface SearcherSources<E extends Parcelable> {
	/**
	 * Returns an instance of each type of Searcher that can
	 * search for this particular type of card.
	 * @return An ArrayList of Searcher instances of each type of Searcher that can find the particular type of card.
	 */
	public ArrayList<Searcher<E>> getSearchers();
}
