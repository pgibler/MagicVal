package com.magicval;

import java.io.IOException;

import com.magicval.controller.search.MagicTradersSearcher;
import com.magicval.controller.search.Searcher;
import com.magicval.model.card.Card;
import com.magicval.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Performs a search for card using the Searcher's algorithm for determing
 * what a closest match in this case is.
 * @author Paul Gibler
 *
 */
public class SearchForClosestMatchActivity extends Activity {
	
	private final Searcher<Card> mts = new MagicTradersSearcher();
	private EditText input;
	private ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_for_closest_match);
        
        input = (EditText) findViewById(R.id.SearchForClosestMatchEditText);
        input.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If they hit enter just invoke the search.
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        	search();
		        	return true;
		        }
		        return false;
		    }
		});
        
        final Button submit = (Button) findViewById(R.id.SearchForClosestMatchSubmitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	search();
            }
        });
    }
	
	class CardExceptionHolder {
		public Card card  = null;
		public Exception exception = null;
	}
	
	class SearchForClosestMatchAsyncTask extends AsyncTask<String, Void, CardExceptionHolder>  {
		
		@Override
		protected CardExceptionHolder doInBackground(String... params) {
			String search = params[0];
			CardExceptionHolder returnMe = new CardExceptionHolder();
	    	try {
				Card c = mts.searchForClosestMatch(search);
				returnMe.card = c;
			} catch (IOException e) {
				returnMe.exception = new IOException("A connection error occured during the search");
			} catch (IllegalArgumentException e) {
				returnMe.exception = new IllegalArgumentException("No card could match the search input");
			}
			return returnMe;
		}
		
		@Override
		protected void onPostExecute(CardExceptionHolder result) {
			pd.dismiss();
			hideKeyboard();
			
			if(result.card != null)
			{
				Intent i = new Intent(SearchForClosestMatchActivity.this, DisplaySingleCardActivity.class);
				i.putExtra("Card", result.card);
				startActivity(i);
			} else if(result.exception != null) {
				showAlert(result.exception.getMessage());
			} else {
				showAlert("Search failed.");
			}
		}
	}
	
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}
	
	private void search() {
		hideKeyboard();
		pd = ProgressDialog.show(this, "Performing some magic", "Retrieving list of cards", true,false);
		String searchText = input.getText().toString();
		new SearchForClosestMatchAsyncTask().execute(searchText);
	}
	
	private void showAlert(String msg) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle("Search failed");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.create().show();
	}
	
}
