package com.magicval.model.card;

import java.text.DecimalFormat;

public class MonetaryValue {
	
	private double currentPrice;
	private double highPrice;
	private double lowPrice;
	private DecimalFormat currencyFormat = new DecimalFormat("$0.00");
	
	public MonetaryValue(final double currentPrice, final double highPrice, final double lowPrice)
	{
		setValues(currentPrice, highPrice, lowPrice);
	}
	
	public MonetaryValue(final String currentPrice, final String highPrice, final String lowPrice)
	{
		setValues(Double.valueOf(currentPrice), Double.valueOf(highPrice), Double.valueOf(lowPrice));
	}
	
	/**
	 * Returns the current price of this card.
	 * @return The current price of this card.
	 */
	public double getCurrentPrice() {
		return currentPrice;
	}
	/**
	 * Returns a nicely formatted current price using the currently set currency format.
	 * @return A nicely formatted current price using the currently set currency format.
	 */
	public String getCurrentPriceAsCurrency() {
		return currencyFormat.format(currentPrice);
	}
	/**
	 * Returns the high price of this card.
	 * @return The high price of this card.
	 */
	public double getHighPrice() {
		return highPrice;
	}
	/**
	 * Returns a nicely formatted high price using the currently set currency format.
	 * @return A nicely formatted high price using the currently set currency format.
	 */
	public String getHighPriceAsCurrency() {
		return currencyFormat.format(highPrice);
	}
	/**
	 * Returns the low price of this card.
	 * @return The low price of this card.
	 */
	public double getLowPrice() {
		return lowPrice;
	}
	/**
	 * Returns a nicely formatted low price using the currently set currency format.
	 * @return A nicely formatted low price using the currently set currency format.
	 */
	public String getLowPriceAsCurrency() {
		return currencyFormat.format(lowPrice);
	}
	
	private void setValues(final double currentPrice, final double highPrice, final double lowPrice) {
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.currentPrice = currentPrice;
	}
}
