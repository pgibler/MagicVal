package com.magicval.search;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.magicval.card.MagicCard;
import com.magicval.card.MagicSet;

import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;


/**
 * DeckbrewApiSearcher performs a card information search on the website
 * https://api.deckbrew.com. This data is used for finding single cards and
 * performing name matching search queries.
 *
 * @author Paul Gibler
 */
public class DeckbrewApiSearcher extends MagicCardSearcherBase {

    private String domain = "https://api.deckbrew.com";
    private String path = "/mtg/cards";
    private String uri = domain + path;

    protected HttpRequestBase buildSearchRequest(String search) {
        return new HttpGet(String.format("%s?name=%s", uri, search.trim().replaceAll(" ", "+")));
    }

    protected SearchRequestResult<MagicCard> parseResponse(InputStream stream) {
        // Temp list to hold the cards in.
        List<MagicCard> cardsTemp = new LinkedList<MagicCard>();

        try {
            // Get the text from the response stream.
            String text = StreamFunctions.getTextFromStream(stream);

            // Fail if nothing comes back
            if (text == null) {
                return new FailedSearchRequestResult<MagicCard>();
            }

            JSONArray array = new JSONArray(text);

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);

                Collection<MagicCard> cardsByEdition = getCardsByEdition(item);

                cardsTemp.addAll(cardsByEdition);
            }

            return new SuccessfulSearchRequestResult<MagicCard>(new ArrayList<MagicCard>(cardsTemp));
        }
        catch(Exception e) {
            return new FailedSearchRequestResult<MagicCard>();
        }
    }

    private Collection<MagicCard> getCardsByEdition(JSONObject cardJson) {

        Collection<MagicCard> cards = new LinkedList<MagicCard>();
        JSONArray editions;

        try {
            editions = cardJson.getJSONArray("editions");
        } catch (JSONException e) {
            // Failed to parse the node
            return cards;
        }

        if (editions != null) {
            for (int i = 0; i < editions.length(); i++) {
                try {
                    JSONObject cardEditionJSON = editions.getJSONObject(i);

                    MagicCard card = generateCard(cardJson, cardEditionJSON);

                    if (card.isValid()) {
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
}
