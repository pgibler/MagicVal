package com.magicval.search;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Parcelable;

/**
 * MultiSourceSearcher iterates through all Search classes that can
 * search for a particular type, or until it succeeds.
 * It throws the proper exception given the method called if it fails.
 *
 * @param <E> The SearcherSources to use for the searching.
 * @param <Q> The type that the SearcherSource can find.
 * @author Paul Gibler
 */
public class MultiSourceSearcher<E extends Parcelable> implements Searcher<E> {

    private Searcher<E>[] searchers;

    public MultiSourceSearcher(Searcher<E>[] searchers) {
        this.searchers = searchers;
    }

    public SearchRequestResult searchFor(String search) {
        for (Searcher<E> s : searchers) {
            try {
                SearchRequestResult<E> results = s.searchFor(search);

                if(results.getSuccess()) {
                    return results;
                }
                break;
            } catch (IOException e) {
                continue;
            }
        }

        return new FailedSearchRequestResult();
    }
}
