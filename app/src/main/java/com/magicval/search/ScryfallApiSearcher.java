package com.magicval.search;

import com.magicval.card.MagicCard;
import com.magicval.card.MagicSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;

public class ScryfallApiSearcher extends MagicCardSearcherBase {

    private String domain = "https://api.scryfall.com";
    private String path = "/cards/search";
    private String uri = domain + path;

    protected HttpRequestBase buildSearchRequest(String search) {
        return new HttpGet(String.format("%s?q=%s", uri, search.trim().replaceAll(" ", "+")));
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

            // API returns result as a top-level object with the card list contained within.
            JSONObject result = new JSONObject(text);

            // Get the card data array from the api.
            JSONArray data = result.getJSONArray("data");

            // Convert the card data array to cards.
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                MagicCard card = generateCard(item);

                cardsTemp.add(card);
            }

            return new SuccessfulSearchRequestResult<MagicCard>(new ArrayList<MagicCard>(cardsTemp));
        }
        catch(Exception e) {
            return new FailedSearchRequestResult<MagicCard>();
        }
    }

    private MagicCard generateCard(JSONObject item) throws JSONException {
        // Name
        String name = item.getString("name");

        // Price
        Double price = item.has("usd") ? item.getDouble("usd") : null;

        // Set
        String setName = item.getString("set_name");
        String setId = item.getString("set");

        MagicSet set = new MagicSet(setName, setId);

        // Generate the card from the retrieved data
        return new MagicCard(name, price, set);
    }
}
