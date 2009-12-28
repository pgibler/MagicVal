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
	
	@Override
	public ArrayList<E> searchFor(String search) throws IOException {
		int ioExceptions = 0;
		int totalSearchers = searchers.size();
		ArrayList<E> results = new ArrayList<E>();
		for(Searcher<E> s : searchers) {
			try {
				results = s.searchFor(search);
				return results;
			} catch (IOException e) {
				ioExceptions++;
				continue;
			}
		}
		/** 
		 * If the user got IOExceptions for all searches, that means that they
		 * probably don't have an internet connection at the moment.
		 */
		if(ioExceptions == totalSearchers) {
			throw new IOException("An error occured with your connection. Please try again later.");
		}
		return results;
	}

	@Override
	public E searchForClosestMatch(String search) throws IOException,
			NoMatchFoundException {
		E result = null;
		int matchesFailed = 0;
		int ioExceptions = 0;
		int totalSearchers = searchers.size();
		for(Searcher<E> s : searchers) {
			try {
				result = s.searchForClosestMatch(search);
				return result;
			} catch (IOException e) {
				ioExceptions++;
				continue;
			} catch (NoMatchFoundException e) {
				matchesFailed++;
				continue;
			}
		}
		/** 
		 * If the user got IOExceptions for all searches, that means that they
		 * probably don't have an internet connection at the moment.
		 */
		if(ioExceptions == totalSearchers) {
			throw new IOException("An error occured with your connection. Please try again later.");
		}
		/**
		 * Otherwise, assume they just couldn't match the search.
		 */
		else {
			throw new NoMatchFoundException("Failed to find a match for the search '"+search+"'.");
		}
	}

}
