package me.taedium.android.profile;

import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import me.taedium.android.ApplicationGlobals;
import me.taedium.android.HeaderActivity;
import me.taedium.android.R;
import me.taedium.android.domain.FilterItem;
import me.taedium.android.domain.FilterItemAdapter;
import me.taedium.android.listener.LoggedInChangedListener;


public class ProfileActivity extends HeaderActivity implements LoggedInChangedListener {
	
	ViewSwitcher vsMain;
	ListView lvSummary;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.profile_page_title);
		setContentView(R.layout.profile);
		addLoggedInListener(this);
		
		// Display appropriate view depending on if user is logged in or not
		vsMain = (ViewSwitcher) findViewById(R.id.vsProfile);
		if (ApplicationGlobals.getInstance().isLoggedIn(this)) {
			vsMain.showNext();
			setupLoggedInView();
		} else {
			vsMain.showNext();
			vsMain.showNext();
			setupNotLoggedInView();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeLoggedInListener(this);
	}
	
	private void setupLoggedInView() {
		
		// Set user's name in main text
		TextView tvWelcome = (TextView) findViewById(R.id.tvLoggedInAs);
		tvWelcome.setText(String.format(getString(R.string.tvLoggedInAs), ApplicationGlobals.getInstance().getUser(this)));
		
		// Set top list view displaying info about activities created, liked, disliked
        lvSummary = (ListView)findViewById(R.id.lvProfileSummary);
        lvSummary.setAdapter(new FilterItemAdapter(this, R.id.list_item_text, getFilterItems()));
        lvSummary.setTextFilterEnabled(true);        
        lvSummary.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	switch((int)id) {
                    case 0:                    	
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                }
            }
        });
		
	}
	
	// Get list items for the activity summary at the top of the page
    private FilterItem[] getFilterItems() {
        String [] options = getResources().getStringArray(R.array.lvProfileSummaryArray);
        FilterItem[] filterItems = new FilterItem[options.length];
        for (int i = 0; i <options.length; i++) {            
            switch (i) {
            case 0:
                filterItems[i] = new FilterItem(R.drawable.people32, options[i], "32");
                break;
            case 1:
                filterItems[i] = new FilterItem(R.drawable.cityicon32, options[i], "15");
                break;
            case 2:
                filterItems[i] = new FilterItem(R.drawable.mapsicon32, options[i], "22");
                break;
            }
        }
        return filterItems;
    }
	
	private void setupNotLoggedInView() {
		
		Button bLogin = (Button) findViewById(R.id.bLoginProfile);
		bLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		Button bRegister = (Button) findViewById(R.id.bRegisterProfile);
		bRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				register();
			}
		});
	}

	@Override
	public void loggedIn() {
		vsMain.showNext();
		setupLoggedInView();
	}
	
	public void loggedOut() {}
	

}
