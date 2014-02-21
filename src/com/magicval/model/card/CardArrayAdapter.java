package com.magicval.model.card;

import java.util.List;

import com.magicval.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * CardArrayAdapter is used to display an Array or List of cards.
 * @author Paul Gibler
 *
 */
public class CardArrayAdapter extends ArrayAdapter<MagicCard> {
	
	private LayoutInflater inflater = null;
	private int layoutRes;
	private List<MagicCard> cards;

	public CardArrayAdapter(Context context, int textViewResourceId,
			List<MagicCard> objects) {
		super(context, textViewResourceId, objects);
		
		layoutRes = textViewResourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cards = objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
        if (convertView == null)
		{
            view = inflater.inflate(layoutRes, parent, false);
        }
		else
		{
            view = convertView;
        }
        MagicCard card = cards.get(position);
        TextView name = (TextView)view.findViewById(R.id.CardSearchRowName);
        TextView price = (TextView)view.findViewById(R.id.CardSearchRowPrice);
        name.setText(card.getNameFromSearch());
        price.setText(card.getMonetaryValue().getMedianPriceAsCurrency());

        return (view);
	}
	
}
