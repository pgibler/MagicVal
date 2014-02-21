package com.magicval.controller.search.sources;

import java.util.ArrayList;

import com.magicval.controller.search.Searcher;
import com.magicval.controller.search.magic.DeckbrewApiSearcher;
import com.magicval.model.card.MagicCard;

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
