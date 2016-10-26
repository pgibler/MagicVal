package com.magicval.search;

import java.util.ArrayList;

/**
 * Why so many classes? Because hierarchy is just how you do it in Java.
 */
public class FailedSearchRequestResult<E> extends SearchRequestResult<E> {
    protected FailedSearchRequestResult() {
        super(new ArrayList<E>() {}, false);
    }
}
