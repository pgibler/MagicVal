package com.magicval;

import com.magicval.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * MagicValActivity is the entry point to this program.
 * It displays buttons to all of the top level features the
 * user may use.
 * @author Paul Gibler
 *
 */
public class MagicValActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        final Button searchForClosestMatchButton = (Button) findViewById(R.id.HomeSearchForClosestMatchButton);
        searchForClosestMatchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MagicValActivity.this, SearchForClosestMatchActivity.class);
                startActivity(intent);
            }
        });
        
        final Button searchForButton = (Button) findViewById(R.id.HomeSearchForButton);
        searchForButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	onSearchRequested();
            }
        });
        
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    }
    
}