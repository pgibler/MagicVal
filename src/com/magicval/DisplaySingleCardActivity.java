package com.magicval;

import java.io.IOException;

import com.magicval.card.MagicCard;
import com.magicval.card.MonetaryValue;
import com.magicval.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

/**
 * Displays a single card, loaded through the Bundle, onto the screen.
 * The card metadata is displayed immediately, and the card image is
 * requested from the Gatherer search engine.
 * 
 * @author Paul Gibler
 *
 */
public class DisplaySingleCardActivity extends Activity {
	
	private DisplaySingleCardActivity ref;
	
	/**
	 * Container for the card image or error message,
	 * whichever is returned by the image fetch task.
	 */
	private FrameLayout frame;
	
	/**
	 * The current card
	 */
	private MagicCard card;
	
	/**
	 * Stores the current image
	 */
	private View cachedImageView;
	
	/**
	 * Sets the current child view of the frame.
	 * This is exposed so AsyncTasks can set the child view frame of the FrameLayout.
	 * @param view The view to set as the only child of the center FrameLayout.
	 */
	public void setFrameChildView(View view)
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
			ref.setFrameChildView(result);
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
			
			cardImage = adjustOpacity(cardImage, 0);
			
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
	
	/**
	 * @param bitmap The source bitmap.
	 * @param opacity a value between 0 (completely transparent) and 255 (completely
	 * opaque).
	 * @return The opacity-adjusted bitmap.  If the source bitmap is mutable it will be
	 * adjusted and returned, otherwise a new bitmap is created.
	 */
	private Bitmap adjustOpacity(Bitmap bitmap, int opacity)
	{
	    Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
	    Canvas canvas = new Canvas(mutableBitmap);
	    int colour = (opacity & 0xFF) << 24;
	    canvas.drawColor(colour, android.graphics.PorterDuff.Mode.DST_IN);
	    return mutableBitmap;
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
