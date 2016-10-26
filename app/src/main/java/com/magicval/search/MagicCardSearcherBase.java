package com.magicval.search;

import com.magicval.card.MagicCard;

import java.io.IOException;
import java.io.InputStream;

import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;

/**
 * Base class that handles the network duties.
 * Implementations will contains the request building and response parsing.
 */
public abstract class MagicCardSearcherBase implements Searcher<MagicCard> {

    public SearchRequestResult searchFor(String search) throws IOException {
        InputStream stream;
        try {
            stream = HttpFunctions.performHttpsRequest(buildSearchRequest(search));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        // Get the result.
        return parseResponse(stream);
    }

    protected abstract HttpRequestBase buildSearchRequest(String search);

    protected abstract SearchRequestResult<MagicCard> parseResponse(InputStream stream);
}
