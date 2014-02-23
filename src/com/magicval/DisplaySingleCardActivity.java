package com.magicval;

import java.io.IOException;

import com.magicval.R;
import com.magicval.card.MagicCard;
import com.magicval.card.MonetaryValue;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DisplaySingleCardActivity extends Activity {
	
	private FrameLayout frame;
	private DisplaySingleCardActivity ref;
	private MagicCard card;
	private View cachedImageView;
	
	public void setCachedImageView(View view)
	{
		this.cachedImageView = view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			setContentView(R.layout.display_single_card_horizontal);
		}
		else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			setContentView(R.layout.display_single_card_vertical);
		}
        
        ref = this;
        
        Bundle b = getIntent().getExtras();
        
        card = getCard(b);
        displayPriceData();
        
        frame = (FrameLayout) findViewById(R.id.DisplaySingleCardImageFrameLayout);
        
        // Run image loading background task.
        if(cachedImageView == null)
        {
        	new DisplaySingleCardImageTask().execute(b);
        }
        else
        {
        	animateView(cachedImageView);
        }
	}
	
	class DisplaySingleCardImageTask extends AsyncTask<Bundle, Void, View>
	{

		@Override
		protected View doInBackground(Bundle... params) {
			return generateCardView();
		}
		
		@Override
		protected void onPostExecute(View result) {
			ref.setCachedImageView(result);
			ref.animateView(result);
			
			renderView(result);
		}
		
	}
	
	public void animateView(View view)
	{
		Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
		view.startAnimation(fadeInAnimation);
	}
	
	private MagicCard getCard(Bundle b) {
		return b.getParcelable("Card");
	}

	private View generateCardView() {
		View v = null;
		try {
			Bitmap cardImage = card.getImage();
        	ImageView image = new ImageView(ref);
            int val = 15;
            image.setPadding(val, val, val, val);
			image.setImageBitmap(cardImage);
			image.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
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
	
	private void displayPriceData()
	{
		MonetaryValue v = card.getMonetaryValue();
		setTxt(R.id.DisplaySingleCardNameText, card.getNameFromSearch());
        setTxt(R.id.DisplaySingleCardMedianPriceTextView, v.getMedianPriceAsCurrency());
        setTxt(R.id.DisplaySingleCardHighPriceTextView, v.getHighPriceAsCurrency());
        setTxt(R.id.DisplaySingleCardLowPriceTextView, v.getLowPriceAsCurrency());
	}
	
	private void renderView(View result) {
		frame.addView(result);
	}
	
	private void setTxt(int viewid, String txt)
	{
		((TextView) findViewById(viewid)).setText(txt);
	}

}
