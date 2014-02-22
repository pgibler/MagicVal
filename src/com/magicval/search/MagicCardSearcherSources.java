package com.magicval.search;

import java.util.ArrayList;

import com.magicval.card.MagicCard;

public class MagicCardSearcherSources extends CardSearcherSources {
	
	private ArrayList<Searcher<MagicCard>> searchers;
	
	public MagicCardSearcherSources() {
		searchers = new ArrayList<Searcher<MagicCard>>(1);
		searchers.add(0, new DeckbrewApiSearcher());
	}
	
	public ArrayList<Searcher<MagicCard>> getSearchers() {
		return searchers;
	}

}
