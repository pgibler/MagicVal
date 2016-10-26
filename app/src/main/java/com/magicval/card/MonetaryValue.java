package com.magicval.card;

import java.text.DecimalFormat;

public class MonetaryValue {

    private double medianPrice;
    private double highPrice;
    private double lowPrice;
    private DecimalFormat currencyFormat = new DecimalFormat("$0.00");

    public MonetaryValue(final double medianPrice, final double highPrice, final double lowPrice) {
        setValues(medianPrice, highPrice, lowPrice);
    }

    /**
     * Returns the median price of this card.
     *
     * @return The median price of this card.
     */
    public double getMedianPrice() {
        return medianPrice;
    }

    /**
     * Returns a nicely formatted median price using the currently set currency format.
     *
     * @return A nicely formatted median price using the currently set currency format.
     */
    public String getMedianPriceAsCurrency() {
        return currencyFormat.format(medianPrice);
    }

    /**
     * Returns the high price of this card.
     *
     * @return The high price of this card.
     */
    public double getHighPrice() {
        return highPrice;
    }

    /**
     * Returns a nicely formatted high price using the currently set currency format.
     *
     * @return A nicely formatted high price using the currently set currency format.
     */
    public String getHighPriceAsCurrency() {
        return currencyFormat.format(highPrice);
    }

    /**
     * Returns the low price of this card.
     *
     * @return The low price of this card.
     */
    public double getLowPrice() {
        return lowPrice;
    }

    /**
     * Returns a nicely formatted low price using the currently set currency format.
     *
     * @return A nicely formatted low price using the currently set currency format.
     */
    public String getLowPriceAsCurrency() {
        return currencyFormat.format(lowPrice);
    }

    private void setValues(final double medianPrice, final double highPrice, final double lowPrice) {
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.medianPrice = medianPrice;
    }
}
