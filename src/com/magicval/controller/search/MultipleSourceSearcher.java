package com.magicval.controller.search;

import java.io.IOException;
import java.util.ArrayList;

import com.magicval.controller.search.sources.SearcherSources;

import android.os.Parcelable;

/**
 * MultipleSourceSearcher iterates through all Search classes that can
 * search for a particular type, or until it succeeds.
 * It throws the proper exception given the method called if it fails.
 * @author Paul Gibler
 * @param <E> The SearcherSources to use for the searching.
 * @param <Q> The type that the SearcherSource can find.
 *
 */
public class MultipleSourceSearcher<E extends Parcelable> implements Searcher<E> {

	private SearcherSources<E> source;
	private ArrayList<Searcher<E>> searchers;
	
	public MultipleSourceSearcher(SearcherSources<E> sources) {
		source = sources;
		searchers = source.getSearchers();
	}
	
	public ArrayList<E> searchFor(String search) {
		ArrayList<E> results = new ArrayList<E>();
		for(Searcher<E> s : searchers) {
			try {
				results = s.searchFor(search);
				break;
			} catch (IOException e) {
				continue;
			}
		}
		return results;
	}

	public E searchForClosestMatch(String search) {
		E result = null;
		for(Searcher<E> s : searchers) {
			try {
				result = s.searchForClosestMatch(search);
				break;
			} catch (IOException e) {
				continue;
			}
		}
		return result;
	}

}
