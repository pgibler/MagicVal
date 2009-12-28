package com.magicval.controller.search.sources;

import java.util.ArrayList;

import com.magicval.controller.search.Searcher;

import android.os.Parcelable;

/**
 * SearcherSources maintains all Searcher types for the
 * particular generic type.
 * @author Paul Gibler
 *
 * @param <E> The generic type that can be searched for.
 */
public interface SearcherSources<E extends Parcelable> {
	/**
	 * Returns an instance of each type of Searcher that can
	 * search for this particular type of card.
	 * @return An ArrayList of Searcher instances of each type of Searcher that can find the particular type of card.
	 */
	public ArrayList<Searcher<E>> getSearchers();
}
