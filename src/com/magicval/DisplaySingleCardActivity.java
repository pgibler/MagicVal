package com.magicval;

import java.io.IOException;

import com.magicval.model.card.MagicCard;
import com.magicval.model.card.MonetaryValue;
import com.magicval.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplaySingleCardActivity extends Activity {
	
	//private ProgressDialog pd;
	private FrameLayout frame;
	private DisplaySingleCardActivity ref;
	private MagicCard card;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_single_card);
        
        ref = this;
        
        Bundle b = getIntent().getExtras();
        
        frame = (FrameLayout) findViewById(R.id.DisplaySingleCardImageFrameLayout);
        
        View v = generateView(b);
        renderView(v);
        
        // TODO Properly use AsyncTask to load the image.
        
        //pd = ProgressDialog.show(this, "Rendering", "Rendering card data", true,false);
        //new DisplaySingleCardImageTask().execute(b);
		//pd.dismiss();
	}
	
	/*class DisplaySingleCardImageTask extends AsyncTask<Bundle, Void, View>
	{

		@Override
		protected View doInBackground(Bundle... params) {
			return generateView(params[0]);
		}
		
		@Override
		protected void onPostExecute(View result) {
			pd.dismiss();
			renderView(result);
		}
		
	}*/
	
	private View generateView(Bundle b) {
		View v = null;
		try {
			card = b.getParcelable("Card");
        	// TODO Make it so that if the user clicks on the card image,
        	// it expands to the center of the screen for easier viewing.
        	Bitmap cardImage = card.getImage();
        	ImageView image = new ImageView(ref);
            int val = 15;
            image.setPadding(val, val, val, val);
			image.setImageBitmap(cardImage);
	        v = image;
		} catch (IOException e) {
			TextView text = new TextView(ref);
			text.setText("No image available for this card");
			text.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			text.setGravity(Gravity.CENTER);
			v = text;
		}
		return v;
	}
	
	private void renderView(View result) {
		MonetaryValue v = card.getMonetaryValue();
		setTxt(R.id.DisplaySingleCardNameText, card.getNameFromSearch());
        setTxt(R.id.DisplaySingleCardMedianPriceTextView, v.getMedianPriceAsCurrency());
        setTxt(R.id.DisplaySingleCardHighPriceTextView, v.getHighPriceAsCurrency());
        setTxt(R.id.DisplaySingleCardLowPriceTextView, v.getLowPriceAsCurrency());
		frame.addView(result);
		
	}
	
	private void setTxt(int viewid, String txt)
	{
		((TextView) findViewById(viewid)).setText(txt);
	}

}
