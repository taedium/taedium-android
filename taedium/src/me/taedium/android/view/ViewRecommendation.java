package me.taedium.android.view;

import java.util.ArrayList;

import me.taedium.android.ApplicationGlobals;
import me.taedium.android.R;
import me.taedium.android.api.Caller;
import me.taedium.android.domain.Recommendation;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ViewRecommendation extends FragmentHeaderActivity implements Runnable {
	private ViewPager vpActivities;
    private ProgressDialog progressDialog;
    private ArrayList<Recommendation> recommendations;
    private Recommendation curRec;
    
    private Button bLike;
    private Button bDislike;
    private Button bFlag;
    private Spinner spFlag;
    // Spinner's OnItemSelectedListener gets called on creation.. somewhat hacky workaround
    private boolean flagFirstRun = true;
    
    // Display only a single activity
    public static final String KEY_DISPLAY_BY_ID = "display_activity_by_id";
    public static final String KEY_ID_TO_FETCH = "activity_id_to_fetch";
    private boolean displaySingleRec;
    private int idToFetch = 0;
    
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
		setContentView(R.layout.view_rec);
        setTitle(R.string.main_title);
        
        initializeHeader();
        
        // Check if we are displaying one activity or many
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
	        displaySingleRec = bundle.getBoolean(KEY_DISPLAY_BY_ID);
	        idToFetch = bundle.getInt(KEY_ID_TO_FETCH);
        }

        bLike = (Button) findViewById(R.id.bUpVote);
		bDislike = (Button) findViewById(R.id.bDownVote);
		bFlag = (Button) findViewById(R.id.bFlag);
		spFlag = (Spinner) findViewById(R.id.spFlag);
        spFlag.setVisibility(View.INVISIBLE);
		
        // Initialize gaActivities
        vpActivities = (ViewPager) findViewById(R.id.vpActivities);
        // Take care of highlighting footer as a new item is brought up
        vpActivities.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				changeHighlighting();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
        
        // Start a progress dialog to get new activitiesProgressDialog
        if (displaySingleRec) {
	        progressDialog = ProgressDialog.show(this, "", getString(R.string.msgLoadingActivity));
        } else {
	        progressDialog = ProgressDialog.show(this, "", getString(R.string.msgLoadingActivities));
        }
        // Spawn a new thread to perform the computation
        new Thread(this).start();
    }
    
    private void changeHighlighting() {
    	curRec= (Recommendation) ((RecommendationAdapter)vpActivities.getAdapter()).getRecommendation(vpActivities.getCurrentItem());
				
    	if (curRec == null) {
    		unhighlightFooter();
    		return;
    	}
    	
		// Un-highlight all buttons
		if (curRec.likedByUser == null) {
			unhighlightFooter();
		}
		// Highlight like button
		else if (curRec.likedByUser) {
			highlightLikeFooter();
		}
		// Highlight dislike button
		else {
			highlightDislikeFooter();
		}
    }
    
    // Helper function to make sure a user is logged in when pressing footer buttons
    // otherwise display a message
    private boolean verifyLoggedIn() {
    	if(!ApplicationGlobals.getInstance().isLoggedIn(getApplicationContext())) {
    		Toast.makeText(this, getString(R.string.msgMustLogin), Toast.LENGTH_LONG).show();
    		return false;
    	}
    	return true;
    }
    
    // This is the handler that gets notified when the Thread is finished, it resumes building the layout
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            
            RecommendationAdapter recAdapter = new RecommendationAdapter(ViewRecommendation.this, recommendations);
            if (displaySingleRec) recAdapter.setShowSingleRec(true);
            vpActivities.setAdapter(recAdapter);
            
            // Initialize footer buttons
            // UpVote/Like
            Button bUp = (Button) findViewById(R.id.bUpVote);
            bUp.setOnClickListener(new OnClickListener() {
    			
    			public void onClick(View v) {
    				
    				if (verifyLoggedIn()) {
    					
    					// if already liked, remove like
	    				if (curRec.likedByUser != null && curRec.likedByUser == true) {
	    					if (!removeLikeDislike()) {
	    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgRemoveLikeFailed), Toast.LENGTH_LONG).show();
	    					}
	    				}
	    				// otherwise like it
	    				else {	    				
	    					if (Caller.getInstance(getApplicationContext()).likeDislike(curRec.id, true)) {
	    						highlightLikeFooter();
	    						curRec.likedByUser = true;
	    					}
	    					else {
	    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgLikeFailed), Toast.LENGTH_LONG).show();
	    					}
	    				}
    				}				
    			}
    		});
            
            // DownVote/Dislike
            Button bDown = (Button) findViewById(R.id.bDownVote);
            bDown.setOnClickListener(new OnClickListener() {
    			
    			public void onClick(View v) {
    				if (verifyLoggedIn()) {
    					// if already dislike, remove dislike
	    				if (curRec.likedByUser != null && curRec.likedByUser == false) {
	    					if (!removeLikeDislike()) {
	    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgRemoveDislikeFailed), Toast.LENGTH_LONG).show();
	    					}
	    				}
	    				// otherwise dislike it
	    				else {	  
	    					if (Caller.getInstance(getApplicationContext()).likeDislike(curRec.id, false)) {
	    						highlightDislikeFooter();
	    						curRec.likedByUser = false;
	    					}
	    					else {
	    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgDislikeFailed), Toast.LENGTH_LONG).show();
	    					}
	    				}
    				}				
    			}
    		});
            
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ViewRecommendation.this, R.array.spFlagReasons, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spFlag.setAdapter(adapter);
            spFlag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!flagFirstRun) {
                        String reason = ((TextView)view).getText().toString();
                        boolean success = Caller.getInstance(getApplicationContext()).flagActivity(curRec.id, reason);
                        if (success) {
                        	Toast.makeText(ViewRecommendation.this, getString(R.string.msgFlagSuccess)+" "+reason, Toast.LENGTH_SHORT).show();
                        	int current = vpActivities.getCurrentItem();
                        	((RecommendationAdapter)vpActivities.getAdapter()).removeRecommendation(current);
                        	// There is a bug in android where FragmentPageLoader's don't respond to notifyDataSetChanged
                        	// This is a hacky workaround and causes weirdness when flagging and then going back
                        	// See http://code.google.com/p/android/issues/detail?id=19001
                        	vpActivities.setCurrentItem(current+1);
                        } else {
                            Toast.makeText(ViewRecommendation.this, getString(R.string.msgFlagFailed), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        flagFirstRun = false;
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Flag
            bFlag.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				if (verifyLoggedIn()) {
    					if (curRec.flaggedByUser) {
    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgCannotUnflag), Toast.LENGTH_LONG).show();
    					} else {
    					    spFlag.performClick();
    					}
    				}
    			}
    		});
            
            // Make sure we highlight the first activity if it is liked/disliked
            changeHighlighting();
            
            // Show a message if no recommendations were returned
            if (recommendations.size() == 0) {
                Toast.makeText(ViewRecommendation.this, getString(R.string.msgNoRecommendationsFound), Toast.LENGTH_LONG).show();
            }
        }
    };
 
    // Helper to remove a like or dislike
    private boolean removeLikeDislike() {
    	if (Caller.getInstance(getApplicationContext()).removeLikeDislike(curRec.id)) {
			unhighlightFooter();
			curRec.likedByUser = null;
			return true;
		}
    	return false;
    }
    
    /*
     * Helpers for changing colours of footer buttons
     */
    private void unhighlightFooter() {
    	bLike.setBackgroundResource(R.drawable.b_footer_bg_fsm);
		bDislike.setBackgroundResource(R.drawable.b_footer_bg_fsm);
		bFlag.setBackgroundResource(R.drawable.b_footer_bg_fsm);
    }
    
    private void highlightLikeFooter() {
    	bLike.setBackgroundResource(R.drawable.b_footer_bg_highlighting_fsm);
		bDislike.setBackgroundResource(R.drawable.b_footer_bg_fsm);
		bFlag.setBackgroundResource(R.drawable.b_footer_bg_fsm);
    }
    
    private void highlightDislikeFooter() {
    	bLike.setBackgroundResource(R.drawable.b_footer_bg_fsm);
		bDislike.setBackgroundResource(R.drawable.b_footer_bg_highlighting_fsm);
		bFlag.setBackgroundResource(R.drawable.b_footer_bg_fsm);
    }

    // This is the run method of the thread that retrieves activities.
    public void run() {
        /**/
    	if (displaySingleRec) {
    		recommendations = new ArrayList<Recommendation>();
    		Recommendation rec = Caller.getInstance(getApplicationContext()).getRecommendation(idToFetch);
    		if (rec!= null) {
	    		recommendations.add(rec);
    		}
    		
    	} else {
	        recommendations = Caller.getInstance(getApplicationContext()).getRecommendations();
    	}
        /**
        // Backup recommendations hardcoded for testing
        recommendations = new ArrayList<Recommendation>();
        recommendations.add(new Recommendation("Take a Hike", "Venture forth into the backwoods of your childhood.  Bring a backpack and some friends.  Pack a light lunch, but don't eat it until you reach the perfect spot.  Bring a camera and some binoculors and try to see how many birds you can identify.  Make sure to wear proper footwear and always be mindful of keeping the trail clean. This description gets even longer to cause a little bit of overflow here and there.  It's going to be good! I hope it will, or I have just wasted a bunch of my time.  Stupid stupid stupid.", 1, 6, 60, 240, 1, false, 43.490656877933816, -80.54049253463745, null, false));
        recommendations.add(new Recommendation("Go somewhere", "Do something", 1, 6, 60, 240, 1, false, 43.490656877933816, -80.54049253463745, null, false));
        recommendations.add(new Recommendation("Go fly a kite", "Don't get it caught in hydro line"));
        recommendations.add(new Recommendation("Go rent a convertible you could never afford to own", "Take it down country roads with the top down"));
        /**/
        if (recommendations.size() > 0) {
	        curRec = recommendations.get(0);
        }
        // Since separate threads can't touch another thread's view, send a message via Handler when computation is done to complete building the layout.
        handler.sendEmptyMessage(0);
    }
}
