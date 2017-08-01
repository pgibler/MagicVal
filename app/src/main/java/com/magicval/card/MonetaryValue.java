package com.magicval.card;

import java.text.DecimalFormat;

public class MonetaryValue {

    private final String noPriceDataMessage = "No price data";

    private Double medianPrice;
    private Double highPrice;
    private Double lowPrice;
    private DecimalFormat currencyFormat = new DecimalFormat("$0.00");

    public MonetaryValue(final Double medianPrice, final Double highPrice, final Double lowPrice) {
        setValues(medianPrice, highPrice, lowPrice);
    }

    public MonetaryValue(Double price) {
        setValues(price, null, null);
    }

    /**
     * Returns the median price of this card.
     *
     * @return The median price of this card.
     */
    public Double getMedianPrice() {
        return medianPrice;
    }

    /**
     * Returns the high price of this card.
     *
     * @return The high price of this card.
     */
    public Double getHighPrice() {
        return highPrice;
    }

    /**
     * Returns the low price of this card.
     *
     * @return The low price of this card.
     */
    public Double getLowPrice() {
        return lowPrice;
    }

    /**
     * Returns a nicely formatted median price using the currently set currency format.
     *
     * @return A nicely formatted median price using the currently set currency format.
     */
    public String getMedianPriceAsCurrency()
    {
        return priceAsCurrency(medianPrice);
    }

    /**
     * Returns a nicely formatted high price using the currently set currency format.
     *
     * @return A nicely formatted high price using the currently set currency format.
     */
    public String getHighPriceAsCurrency()
    {
        return priceAsCurrency(highPrice);
    }

    /**
     * Returns a nicely formatted low price using the currently set currency format.
     *
     * @return A nicely formatted low price using the currently set currency format.
     */
    public String getLowPriceAsCurrency()
    {
        return priceAsCurrency(lowPrice);
    }

    private void setValues(final Double medianPrice, final Double highPrice, final Double lowPrice) {
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.medianPrice = medianPrice;
    }

    private String priceAsCurrency(Double price) {
        return price == null ? noPriceDataMessage : currencyFormat.format(price);
    }
}
