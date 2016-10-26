package com.magicval.card;

import java.util.List;

import com.magicval.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * MagicCardArrayAdapter is used to display an Array or List of cards.
 *
 * @author Paul Gibler
 */
public class MagicCardArrayAdapter extends ArrayAdapter<MagicCard> {

    private LayoutInflater inflater = null;
    private int layoutRes;
    private List<MagicCard> cards;

    public MagicCardArrayAdapter(Context context, int textViewResourceId,
                                 List<MagicCard> objects) {
        super(context, textViewResourceId, objects);

        layoutRes = textViewResourceId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cards = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(layoutRes, parent, false);
        } else {
            view = convertView;
        }

        // Get the card for the view.
        MagicCard card = cards.get(position);

        // Setup the name field.
        TextView name = (TextView) view.findViewById(R.id.CardSearchRowName);
        name.setText(card.getNameAndSetId());

        TextView price = (TextView) view.findViewById(R.id.CardSearchRowPrice);
        price.setText(card.getMonetaryValue().getMedianPriceAsCurrency());

        return view;
    }

}
