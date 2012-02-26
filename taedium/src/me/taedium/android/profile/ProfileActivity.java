package me.taedium.android.profile;

import me.taedium.android.ApplicationGlobals;
import me.taedium.android.HeaderActivity;
import me.taedium.android.R;
import me.taedium.android.domain.FilterItem;
import me.taedium.android.domain.FilterItemAdapter;
import me.taedium.android.domain.RankingItem;
import me.taedium.android.domain.RankingItemAdapter;
import me.taedium.android.listener.LoggedInChangedListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class ProfileActivity extends HeaderActivity implements LoggedInChangedListener {
	
	private static final int LIST_ITEM_ADDED = 0;
	private static final int LIST_ITEM_LIKED = 1;
	private static final int LIST_ITEM_DISLIKED = 2;
	private ViewSwitcher vsMain;
	
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
        ListView lvSummary = (ListView) findViewById(R.id.lvProfileSummary);
        lvSummary.setAdapter(new FilterItemAdapter(this, R.id.list_item_text, getSummaryItems()));
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
        
        // Setup scoreboard
        ListView lvRankings= (ListView) findViewById(R.id.lvRankings);
        lvRankings.setAdapter(new RankingItemAdapter(this, R.id.tvListItemUser, getDummyRankings()));
        lvRankings.setTextFilterEnabled(true);        
		
	}
	
	// Get list items for the activity summary at the top of the page
    private FilterItem[] getSummaryItems() {
        String [] options = getResources().getStringArray(R.array.lvProfileSummaryArray);
        FilterItem[] filterItems = new FilterItem[options.length];
        for (int i = 0; i <options.length; i++) {            
            switch (i) {
            case LIST_ITEM_ADDED:
                filterItems[i] = new FilterItem(R.drawable.round_plus, options[i], "32");
                break;
            case LIST_ITEM_LIKED:
                filterItems[i] = new FilterItem(R.drawable.hand_pro, options[i], "15");
                break;
            case LIST_ITEM_DISLIKED:
                filterItems[i] = new FilterItem(R.drawable.hand_contra, options[i], "22");
                break;
            }
        }
        return filterItems;
    }
    
	// Get a dummy list items for the scoreboard  TODO TEMPORARY TILL WE HAVE AN API
    private RankingItem[] getDummyRankings() {
    	RankingItem [] rankings = new RankingItem[7]; 
    	rankings[0] = new RankingItem(1, "jason", 100000001);
    	rankings[1] = new RankingItem(2, "shane", 10000001);
    	rankings[2] = new RankingItem(3, "nugget", 1000001);
    	rankings[3] = new RankingItem(4, "evan", 100001);
    	rankings[4] = new RankingItem(5, "ahal", 10001);
    	rankings[5] = new RankingItem(6, "edubs", 1001);
    	rankings[6] = new RankingItem(7, "joynt", 101);
    	return rankings;
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
