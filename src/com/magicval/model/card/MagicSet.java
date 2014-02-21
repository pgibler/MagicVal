package com.magicval.model.card;

/**
 * A MagicSet contains all information needed by the app to render set information to the client.
 * @author Paul Gibler
 *
 */
public class MagicSet {
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
}
