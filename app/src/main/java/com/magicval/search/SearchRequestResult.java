package com.magicval.search;

import java.util.ArrayList;

public class SearchRequestResult<E> {
    private ArrayList<E> cards;
    private Boolean success;

    protected SearchRequestResult(ArrayList<E> cards, boolean success) {
        set(cards, success);
    }

    private void set(ArrayList<E> cards, boolean success) {
        this.cards = cards;
        this.success = success;
    }

    public ArrayList<E> getCards() {
        return cards;
    }

    public Boolean getSuccess() {
        return success;
    }
}
