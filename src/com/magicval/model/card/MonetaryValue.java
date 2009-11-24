package com.magicval.model.card;

public class MonetaryValue {
	
	private double currentPrice;
	private double highPrice;
	private double lowPrice;
	
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
	 * Returns the high price of this card.
	 * @return The high price of this card.
	 */
	public double getHighPrice() {
		return highPrice;
	}
	/**
	 * Returns the low price of this card.
	 * @return The low price of this card.
	 */
	public double getLowPrice() {
		return lowPrice;
	}
	
	private void setValues(final double currentPrice, final double highPrice, final double lowPrice) {
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.currentPrice = currentPrice;
	}
}
