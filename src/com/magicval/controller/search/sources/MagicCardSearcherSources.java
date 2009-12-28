package com.magicval.controller.search.sources;

import java.util.ArrayList;

import com.magicval.controller.search.Searcher;
import com.magicval.controller.search.magic.MagicTradersCardSearcher;
import com.magicval.model.card.Card;

public class MagicCardSearcherSources extends CardSearcherSources {
	
	private ArrayList<Searcher<Card>> searchers;
	
	public MagicCardSearcherSources() {
		searchers = new ArrayList<Searcher<Card>>(1);
		searchers.add(0, new MagicTradersCardSearcher());
	}

	@Override
	public ArrayList<Searcher<Card>> getSearchers() {
		return searchers;
	}

}
