package com.magicval;

import java.io.IOException;

import com.magicval.model.card.Card;
import com.magicval.model.card.MonetaryValue;
import com.magicval.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplaySingleCardActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_single_card);
        
        Bundle b = getIntent().getExtras();
        
        Card c = b.getParcelable("Card");
        MonetaryValue v = c.getMonetaryValue();
        
        setTxt(R.id.DisplaySingleCardNameText, c.getNameFromSearch());
        setTxt(R.id.DisplaySingleCardCurrentPriceTextView, v.getCurrentPriceAsCurrency());
        setTxt(R.id.DisplaySingleCardHighPriceTextView, v.getHighPriceAsCurrency());
        setTxt(R.id.DisplaySingleCardLowPriceTextView, v.getLowPriceAsCurrency());
        
        FrameLayout frame = (FrameLayout) findViewById(R.id.DisplaySingleCardImageFrameLayout);
        
        try {
        	// TODO Make it so that if the user clicks on the card image,
        	// it expands to the center of the screen for easier viewing.
        	Bitmap cardImage = c.getImage();
        	ImageView image = new ImageView(this);
            int val = 15;
            image.setPadding(val, val, val, val);
			image.setImageBitmap(cardImage);
	        frame.addView(image);
		} catch (IOException e) {
			TextView text = new TextView(this);
			text.setText("No image available for this card");
			frame.addView(text);
		}
	}
	
	private void setTxt(int viewid, String txt)
	{
		((TextView) findViewById(viewid)).setText(txt);
	}

}
