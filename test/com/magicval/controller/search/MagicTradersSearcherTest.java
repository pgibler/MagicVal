package com.magicval.controller.search;

import java.io.IOException;
import java.util.List;

import com.magicval.controller.search.MagicTradersSearcher;
import com.magicval.model.card.Card;

import junit.framework.TestCase;

public class MagicTradersSearcherTest extends TestCase {

	MagicTradersSearcher mtpr;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		mtpr = new MagicTradersSearcher();
	}
	
	/**
	 * Checks to see if the object will properly handle the error of the
	 * case of when a does not exist.
	 */
	public void testNonexistantCard() {
		
		try {
			mtpr.searchFor("");
		} catch (IllegalArgumentException e) {
			assert true;
		} catch (IOException e) {
			fail();
		}
	}
	
	public void testRetrieveListOfCards() {
		try {
			List<Card> cards = mtpr.searchFor("Baneslayer Angel");
			int size = cards.size();
			assertEquals(size, 1);
		} catch (IllegalArgumentException e) {
			assert true;
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Tests to see if we actually properly find a card based on name.
	 */
	public void testExistingCard() {
		try {
			Card c = mtpr.searchForClosestMatch("Black Lotus");
			assertTrue(c.getName().startsWith("Black Lotus"));
		} catch (IllegalArgumentException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
	}

}
