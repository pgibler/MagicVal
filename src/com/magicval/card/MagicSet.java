package com.magicval.card;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A MagicSet contains all information needed by the app to render set information to the client.
 * @author Paul Gibler
 *
 */
public class MagicSet implements Parcelable {
	private String setName;
	
	/**
	 * Gets the set name.
	 * @return The set name.
	 */
	public String getSetName() {
		return setName;
	}
	
	private String setId;

	/**
	 * Gets the set ID.
	 * @return The set ID.
	 */
	public String getSetId() {
		return setId;
	}
	
	/**
	 * Constructs an instance of MagicSet.
	 * @param setName The set name of the MagicSet.
	 * @param setId The set ID of the MagicSet.
	 */
	public MagicSet(String setName, String setId)
	{
		this.setName = setName;
		this.setId = setId;
	}

	/**
	 * Creates an instance of MagicSet from a Parcel.
	 * @param p The parcel the MagicSet is being created from.
	 */
	public MagicSet(Parcel p) {
		readFromParcel(p);
	}

	// Parcelable methods
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(setName);
		dest.writeString(setId);	
	}
	
    public int describeContents() {
        return 0;
    }
	
	public static final Parcelable.Creator<MagicSet> CREATOR
    = new Parcelable.Creator<MagicSet>() {
		public MagicSet createFromParcel(Parcel in) {
		    return new MagicSet(in);
		}
		
		public MagicSet[] newArray(int size) {
		    return new MagicSet[size];
		}
	};
	
	
	// Private methods
	
	/**
	 * Reads in this MagicCard from a Parcel.
	 */
	private void readFromParcel(Parcel p)
	{
		setName = p.readString();	// Name From Search
		setId = p.readString();	// Name From Search
	}
	
}
