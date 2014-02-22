package com.magicval.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpUriRequest;
import ch.boye.httpclientandroidlib.conn.ssl.SSLConnectionSocketFactory;
import ch.boye.httpclientandroidlib.conn.ssl.SSLContextBuilder;
import ch.boye.httpclientandroidlib.conn.ssl.TrustStrategy;
import ch.boye.httpclientandroidlib.impl.client.CloseableHttpClient;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;
import ch.boye.httpclientandroidlib.impl.client.HttpClients;

import com.magicval.card.MagicCard;
import com.magicval.card.MagicSet;

/**
 * DeckbrewApiSearcher performs a card information search on the website
 * https://api.deckbrew.com. This data is used for finding single cards and
 * performing name matching search queries.
 * 
 * @author Paul Gibler
 * 
 */
public class DeckbrewApiSearcher implements Searcher<MagicCard> {

	private String domain = "https://api.deckbrew.com";
	private String path = "/mtg/cards";
	private String uri = domain + path;

	private HttpUriRequest request;

	/**
	 * Constructor for DeckbrewApiSearcher.
	 */
	public DeckbrewApiSearcher() {
	}

	public ArrayList<MagicCard> searchFor(String search) throws IOException {
		InputStream stream;
		try {
			stream = getSearchStream(search);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}

		// And then parse
		try {
			return jsonRead(stream);
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
	}

	private ArrayList<MagicCard> jsonRead(InputStream json) throws IOException,
			JSONException {
		return jsonRead(json, -1);
	}

	private ArrayList<MagicCard> jsonRead(InputStream stream, int numberToRead)
			throws IOException, JSONException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		List<MagicCard> cardsTemp = new LinkedList<MagicCard>();

		String text = br.readLine();
		
		// Fail if nothing comes back
		if(text == null)
		{
			return new ArrayList<MagicCard>();
		}
		
		JSONArray array = new JSONArray(text);

		for (int i = 0; i < array.length() && iWithinNumberToRead(i, numberToRead); i++) {
			JSONObject item = array.getJSONObject(i);
			
			Collection<MagicCard> cardsByEdition = getCardsByEdition(item);

			cardsTemp.addAll(cardsByEdition);
		}

		br.close();
		return new ArrayList<MagicCard>(cardsTemp);
	}
	
	// If i = -1, then we should always return true. This gives us the option to ignore having a maximum.
	// Otherwise make sure it's less than numberToRead
	private boolean iWithinNumberToRead(int i, int numberToRead) {
		return numberToRead == -1 || i < numberToRead;
	}
	
	private Collection<MagicCard> getCardsByEdition(JSONObject cardJson) {
		
		Collection<MagicCard> cards = new LinkedList<MagicCard>();
		JSONArray editions = null;
		
		try {
			editions = cardJson.getJSONArray("editions");
		} catch (JSONException e) {
			// Failed to parse the node
			return cards;
		}
		
		if(editions != null)
		{
			for (int i = 0; i < editions.length(); i++) {
				try {
					JSONObject cardEditionJSON = editions.getJSONObject(i);
					
					MagicCard card = generateCard(cardJson, cardEditionJSON);
					
					if(card.isValid())
					{
						cards.add(card);
					}
				} catch (JSONException e) {
					// Couldn't parse this edition. Continue to the next one.
					continue;
				}
			}
		}
		
		return cards;
	}
	
	private MagicCard generateCard(JSONObject cardData, JSONObject editionData) throws JSONException {
		// Name
		String name = cardData.getString("name");
		
		// Price
		JSONObject price = editionData.getJSONObject("price");
		
		// These values are in cents. Divide by 100 to get the dollar amount
		double median = price.getDouble("median") / 100;
		double high = price.getDouble("high") / 100;
		double low = price.getDouble("low") / 100;

		// Set
		String setName = editionData.getString("set");
		String setId = editionData.getString("set_id");
		
		MagicSet set = new MagicSet(setName, setId);
		
		// Generate the card from the retrieved data
		return new MagicCard(name, median, high, low, set);
	}

	public MagicCard searchForClosestMatch(String search) throws IOException {
		// This method uses an extremely naive way of finding the closest match.
		// It just returns the first card found.

		try {
			InputStream stream = getSearchStream(search);
			ArrayList<MagicCard> cards = jsonRead(stream, 1);
			if (cards.size() == 0) {
				return null; // Failure
			}
			return cards.get(0);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	private InputStream getSearchStream(String search) throws IOException,
			NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		String urlWithQuery = uri + "?name="
				+ search.trim().replaceAll(" ", "+");

		// Open an SSL connection
		SSLContextBuilder builder = new SSLContextBuilder();

		builder.loadTrustMaterial(null, new TrustStrategy(){
		    public boolean isTrusted(X509Certificate[] chain, String authType)
		        throws CertificateException {
		        return true;
		    }
		});

		SSLContext context = builder.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				context);

		HttpClientBuilder clientBuilder = HttpClients.custom();
		clientBuilder.setSSLSocketFactory(sslsf);
		CloseableHttpClient httpclient = clientBuilder.build();

		// Target is the search query we search for cards on.
		request = new HttpGet(urlWithQuery);
		// Now execute it
		HttpResponse hr = httpclient.execute(request);

		// Get contents of response
		return hr.getEntity().getContent();
	}
}
