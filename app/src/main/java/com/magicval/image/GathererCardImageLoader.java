package com.magicval.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;

import com.magicval.card.MagicCard;

public class GathererCardImageLoader implements ImageLoader<MagicCard> {

    // Maybe I'll use the imageURL as a default if the card image is not loaded...
    private String defaultImageURL = "http://upload.wikimedia.org/wikipedia/en/thumb/a/aa/Magic_the_gathering-card_back.jpg/250px-Magic_the_gathering-card_back.jpg";
    private String searchURL = "http://gatherer.wizards.com/Handlers/InlineCardSearch.ashx?nameFragment=";
    private String cardSearchURL = "http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=";

    public GathererCardImageLoader() {
    }

    /**
     * Gets the image of the Magic Card by pulling it down from it's Gatherer address.
     *
     * @param card The MagicCard to get the image of.
     */
    public Bitmap getImage(MagicCard card) throws IOException {
        String urlCardName = card.getNameForURL();
        JSONObject jo = searchFor(urlCardName);
        try {
            JSONArray resultsArray = jo.getJSONArray("Results");
            JSONObject firstResult = (JSONObject) resultsArray.get(0);
            String cardID = firstResult.getString("ID");
            String search = cardSearchURL + cardID;
            return removeCardCornerFaster(
                    loadImage(search));
        } catch (JSONException e) {
            return
                    removeCardCornerFaster(
                            loadImage(defaultImageURL));
        }
    }

    private Point[] points = new Point[]{
            // Row 1
            new Point(0, 0),
            new Point(1, 0),
            new Point(2, 0),
            new Point(3, 0),
            new Point(4, 0),
            new Point(5, 0),
            new Point(6, 0),

            // Row 2
            new Point(0, 1),
            new Point(1, 1),
            new Point(2, 1),
            new Point(3, 1),
            new Point(4, 1),

            // Row 3
            new Point(0, 2),
            new Point(1, 2),
            new Point(2, 2),

            // Row 4
            new Point(0, 3),
            new Point(1, 3),

            // Row 5
            new Point(0, 4),
            new Point(1, 4),

            // Row 6
            new Point(0, 5),

            // Row 7
            new Point(0, 6)
    };

    private Bitmap removeCardCornerFaster(Bitmap b) {
        // Copy the bitmap
        b = b.copy(Bitmap.Config.ARGB_8888, true);

        // Set the bitmap to have alpha.
        b.setHasAlpha(true);

        // Store the width/height.
        int bWidth = b.getWidth() - 1;
        int bHeight = b.getHeight() - 1;

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            int x = p.x;
            int y = p.y;

            // Alter current pixel.
            setPixelTransparent(b, x, y);
            // Alter pixel opposite horizontally, same y.
            setPixelTransparent(b, bWidth - x, y);
            // Alter pixel opposite vertically, same x.
            setPixelTransparent(b, x, bHeight - y);
            // Alter pixel opposite horizontally and vertically.
            setPixelTransparent(b, bWidth - x, bHeight - y);
        }
        return b;
    }

    private void setPixelTransparent(Bitmap b, int x, int y) {
        b.setPixel(x, y, Color.TRANSPARENT);
    }

    private JSONObject searchFor(String search) throws IOException {
        String urlWithQuery = searchURL + search;

        // Target is the search query we search for cards on.
        URL url = new URL(urlWithQuery);
        URLConnection urlConnection = url.openConnection();
        InputStream stream = urlConnection.getInputStream();

        // And then parse
        return readAll(stream);
    }

    private Bitmap loadImage(String search) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(search).openConnection();
        conn.setDoInput(true);
        conn.connect();

        Options opts = new Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        InputStream inStream = conn.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inStream, null, opts);
        inStream.close();

        conn.disconnect();
        return bitmap;
    }

    private JSONObject readAll(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String st = "";
        String line;
        while ((line = br.readLine()) != null) {
            st += line;
        }
        br.close();
        try {
            return new JSONObject(st);
        } catch (JSONException e) {
            throw new IOException("Could not read in JSONObject correctly.");
        }
    }
}
