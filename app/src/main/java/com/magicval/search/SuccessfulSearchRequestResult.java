package com.magicval.search;

import java.util.ArrayList;

/**
 * Why so many classes? Because hierarchy is just how you do it in Java.
 */
public class SuccessfulSearchRequestResult<E> extends SearchRequestResult<E> {
    protected SuccessfulSearchRequestResult(ArrayList<E> cards) {
        super(cards, true);
    }
}
