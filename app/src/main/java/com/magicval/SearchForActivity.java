package com.magicval;

import java.util.ArrayList;

import com.magicval.card.MagicCard;
import com.magicval.card.MagicCardArrayAdapter;
import com.magicval.search.DeckbrewApiSearcher;
import com.magicval.search.MultiSourceSearcher;
import com.magicval.search.SearchRequestResult;
import com.magicval.search.Searcher;
import com.magicval.viewtools.DialogBuilder;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * SearchForActivity is the default search activity for the application.
 *
 * @author Paul Gibler
 */
public class SearchForActivity extends ListActivity {

    private Searcher<MagicCard> searcher;
    private ArrayList<MagicCard> cards;
    private SearchForActivity ref;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        searcher = new MultiSourceSearcher<MagicCard>(new Searcher[]{
                new DeckbrewApiSearcher()
        });
        ref = this;

        final Intent queryIntent = getIntent();
        final String queryAction = queryIntent.getAction();
        if (Intent.ACTION_SEARCH.equals(queryAction)) {
            final String queryString = queryIntent.getStringExtra(SearchManager.QUERY);
            search(queryString, bundle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        out.putParcelableArrayList("Cards", cards);
    }

    private void search(final String query, final Bundle b) {
        try {
            cards = b.getParcelableArrayList("Cards");
            renderCards(cards);
        } catch (Exception except) {
            // If we catch an exception here, that means that our Bundle did not contain
            // the ArrayList of cards. Thus, we need to search for them based on the search query.
            pd = ProgressDialog.show(this, "Performing some magic", "Retrieving list of cards", true, false);
            //hideKeyboard();
            new SearchForAsyncTask().execute(query);
        }

    }

    class CardExceptionHolder {
        public ArrayList<MagicCard> cards = null;
        public Exception exception = null;
    }

    /**
     * Searches for and populates the list view with the found card data.
     *
     * @author Paul Gibler
     */
    class SearchForAsyncTask extends AsyncTask<String, Void, CardExceptionHolder> {

        @Override
        protected CardExceptionHolder doInBackground(String... params) {
            CardExceptionHolder returnMe = new CardExceptionHolder();
            try {
                String searchText = params[0];
                SearchRequestResult<MagicCard> result = searcher.searchFor(searchText);

                if (result.getSuccess() && result.getCards().size() > 0) {
                    returnMe.cards = result.getCards();
                } else {
                    throw new Exception("Search for '" + searchText + "' returned no results.");
                }
            } catch (Exception e) {
                returnMe.exception = e;
            }
            return returnMe;
        }

        @Override
        protected void onPostExecute(CardExceptionHolder result) {
            pd.dismiss();

            if (result.exception != null) {
                Exception e = result.exception;
                DialogBuilder.showAlert(ref, e.getMessage(), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ref.finish();
                    }
                });
            } else {
                cards = result.cards;
                renderCards(cards);
            }
        }
    }

    private void renderCards(final ArrayList<MagicCard> cards) {
        MagicCardArrayAdapter cardAdapter = new MagicCardArrayAdapter(ref, R.layout.card_search_row, cards);
        setListAdapter(cardAdapter);
        getListView().setTextFilterEnabled(true);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MagicCard c = cards.get(position);
                Intent i = new Intent(ref, DisplaySingleCardActivity.class);
                i.putExtra("Card", c);
                ref.startActivity(i);
            }
        });
    }
}
