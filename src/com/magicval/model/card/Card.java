package com.magicval.model.card;


import java.io.IOException;

import com.magicval.model.image.CardImageLoader;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Card is, for the purposes of this application, a tradable object.
 * It has a name and a price to distinguish itself.
 * Cards are also Parcelable for the purposes of sending them
 * between Activity instances.
 * @author Paul Gibler
 *
 */
public class Card implements Parcelable {
	
	/**
	 * The name of the card.
	 */
	private String name;
	/**
	 * Returns the name of this card.
	 * @return The name of this card.
	 */
	public String getName() {
		return name;
	}
	/**
	 * The monetary value of the card.
	 */
	private MonetaryValue monetaryValue;
	/**
	 * Returns the monetary value of this card.
	 * @return The monetary value of this card.
	 */
	public MonetaryValue getMonetaryValue()
	{
		return monetaryValue;
	}
	
	/**
	 * Creates an instance of Card.
	 * @param name The name of the card.
	 * @param current The current price of the card.
	 * @param high The highest price the card has been.
	 * @param low The lowest price the card has been.
	 */
	public Card(final String name, final String current, final String high, final String low)
	{
		this.name = name;
		this.monetaryValue = new MonetaryValue(current, high, low);
	}
	
	/**
	 * Creates an instance of Card.
	 * @param name The name of the card.
	 * @param value The MonetaryValue of the card. Contains current price, highest price, and lowest price.
	 */
	public Card(final String name, MonetaryValue value)
	{
		this.name = name;
		this.monetaryValue = value;
	}
	
	/**
	 * Creates an instance of Card from a Parcel.
	 * @param p The Parcel to create this card from.
	 */
	public Card(Parcel p)
	{
		readFromParcel(p);
	}
	
	/**
	 * Loads this Card's image.
	 * @return A Bitmap image of the card.
	 * @throws IOException If the image fails to load, throw this exception.
	 */
	public Bitmap getImage() throws IOException {
		return new CardImageLoader().getCardImage(this);
	}
	
	// Parcelable methods

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeDouble(monetaryValue.getCurrentPrice());
		dest.writeDouble(monetaryValue.getHighPrice());
		dest.writeDouble(monetaryValue.getLowPrice());
		
	}
	
	@Override
    public int describeContents() {
        return 0;
    }
	
	public static final Parcelable.Creator<Card> CREATOR
    = new Parcelable.Creator<Card>() {
		public Card createFromParcel(Parcel in) {
		    return new Card(in);
		}
		
		public Card[] newArray(int size) {
		    return new Card[size];
		}
	};
		
	// Private methods
	
	private void readFromParcel(Parcel p)
	{
		name = p.readString();		// Name
		monetaryValue = new MonetaryValue(
				p.readDouble(),		// Current
				p.readDouble(),		// High
				p.readDouble());	// Low
	}
}
