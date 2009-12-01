package com.magicval;

import java.util.ArrayList;

import com.magicval.controller.search.MagicTradersSearcher;
import com.magicval.controller.search.Searcher;
import com.magicval.model.card.Card;
import com.magicval.model.card.CardArrayAdapter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * SearchForActivity is the default search activity for the application.
 * @author Paul Gibler
 *
 */
public class SearchForActivity extends ListActivity {

	private Searcher<Card> searcher;
	private ArrayList<Card> cards;
	private SearchForActivity ref;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		searcher = new MagicTradersSearcher();
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

	private void search(final String query, final Bundle b)
	{
		try {
			cards = b.getParcelableArrayList("Cards");
			renderCards(cards);
		} catch(Exception except) {
			// If we catch an exception here, that means that our Bundle did not contain
			// the ArrayList of cards. Thus, we need to search for them based on the search query.
			pd = ProgressDialog.show(this, "Performing some magic", "Retrieving list of cards", true,false);
			//hideKeyboard();
			new SearchForAsyncTask().execute(query);
		}

	}
	
	class CardExceptionHolder {
		public ArrayList<Card> cards  = null;
		public Exception exception = null;
	}

	/**
	 * Searches for and populates the list view with the found card data.
	 * 
	 * @author Paul Gibler
	 *
	 */
	class SearchForAsyncTask extends AsyncTask<String, Void, CardExceptionHolder>  {

		@Override
		protected CardExceptionHolder doInBackground(String... params) {
			CardExceptionHolder returnMe = new CardExceptionHolder();
			try {
				String searchText = params[0];
				ArrayList<Card> cards = searcher.searchFor(searchText);

				if(cards.size() > 0)
				{
					returnMe.cards = cards;
				} else {
					throw new Exception("Search returned no results.");
				}
			} catch (Exception e) {
				returnMe.exception = e;
			}
			return returnMe;
		}

		@Override
		protected void onPostExecute(CardExceptionHolder result) {
			pd.dismiss();

			if(result.exception != null)
			{
				Exception e = result.exception;
				showAlert(e.getMessage());
			} else {
				cards = result.cards;
				renderCards(cards);
			}
		}
	}
	
	private void renderCards(final ArrayList<Card> cards) {
		CardArrayAdapter cardAdapter = new CardArrayAdapter(ref, R.layout.card_search_row, cards);
		setListAdapter(cardAdapter);
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Card c = cards.get(position);
				Intent i = new Intent(ref, DisplaySingleCardActivity.class);
				i.putExtra("Card", c);
				ref.startActivity(i);
			}
		});
	}

	private void showAlert(String msg) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
		dlgAlert.setMessage(msg);
		dlgAlert.setTitle("Search failed");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.create().show();
	}
}
