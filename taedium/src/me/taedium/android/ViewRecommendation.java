package me.taedium.android;

import java.util.ArrayList;

import me.taedium.android.api.Caller;
import me.taedium.android.domain.Recommendation;
import me.taedium.android.widgets.RecommendationGallery;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ViewRecommendation extends HeaderActivity implements Runnable {
    private RecommendationGallery gaActivities;
    private ProgressDialog progressDialog;
    private ArrayList<Recommendation> recommendations;
    private Recommendation curRec;
    
    private Button bLike;
    private Button bDislike;
    private Button bFlag;
    private Spinner spFlag;
    // Spinner's OnItemSelectedListener gets called on creation.. somewhat hacky workaround
    private boolean flagFirstRun = true;
    
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
		setContentView(R.layout.view_rec);
        setTitle(R.string.main_title);
        
        initializeHeader();

        bLike = (Button) findViewById(R.id.bUpVote);
		bDislike = (Button) findViewById(R.id.bDownVote);
		bFlag = (Button) findViewById(R.id.bFlag);
		spFlag = (Spinner) findViewById(R.id.spFlag);
        spFlag.setVisibility(View.INVISIBLE);
		
        // Initialize gaActivities
        gaActivities = (RecommendationGallery) findViewById(R.id.gaActivities);
        // Take care of highlighting footer as a new item is brought up
        gaActivities.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				curRec= (Recommendation) gaActivities.getSelectedItem();
				
				// Un-highlight all buttons
				if (curRec.getLikedByUser() == null) {
					unhighlightFooter();
				}
				// Highlight like button
				else if (curRec.getLikedByUser()) {
					highlightLikeFooter();
				}
				// Highlight dislike button
				else {
					highlightDislikeFooter();
				}			
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				unhighlightFooter();
			}        	
		});
        
        // Start a progress dialog to get new activitiesProgressDialog
        progressDialog = ProgressDialog.show(this, "", "Loading Activities...");
        // Spawn a new thread to perform the computation
        new Thread(this).start();
    }
    
    // Helper function to make sure a user is logged in when pressing footer buttons
    // otherwise display a message
    private boolean verifyLoggedIn() {
    	if(!ApplicationGlobals.getInstance().isLoggedIn()) {
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
            
            gaActivities.setAdapter(new RecommendationAdapter(ViewRecommendation.this, recommendations));
            
            // Initialize footer buttons
            // UpVote/Like
            Button bUp = (Button) findViewById(R.id.bUpVote);
            bUp.setOnClickListener(new OnClickListener() {
    			
    			public void onClick(View v) {
    				
    				if (verifyLoggedIn()) {
    					
    					// if already liked, remove like
	    				if (curRec.getLikedByUser() != null && curRec.getLikedByUser() == true) {
	    					if (!removeLikeDislike()) {
	    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgRemoveLikeFailed), Toast.LENGTH_LONG).show();
	    					}
	    				}
	    				// otherwise like it
	    				else {	    				
	    					if (Caller.getInstance().likeDislike(curRec.getId(), true)) {
	    						highlightLikeFooter();
	    						curRec.setLikedByUser(true);
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
	    				if (curRec.getLikedByUser() != null && curRec.getLikedByUser() == false) {
	    					if (!removeLikeDislike()) {
	    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgRemoveDislikeFailed), Toast.LENGTH_LONG).show();
	    					}
	    				}
	    				// otherwise dislike it
	    				else {	  
	    					if (Caller.getInstance().likeDislike(curRec.getId(), false)) {
	    						highlightDislikeFooter();
	    						curRec.setLikedByUser(false);
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
                        boolean success = Caller.getInstance().flagActivity(curRec.getId(), reason);
                        if (success) {                        	
                        	((RecommendationAdapter)gaActivities.getAdapter()).removeRecommendation(
                        			gaActivities.getSelectedItemPosition());
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
    					if (curRec.isFlaggedByUser()) {
    						Toast.makeText(ViewRecommendation.this, getString(R.string.msgCannotUnflag), Toast.LENGTH_LONG).show();
    					} else {
    					    spFlag.performClick();
    					}
    				}
    			}
    		});
            
            // Show a message if no recommendations were returned
            if (recommendations.size() == 0) {
                Toast.makeText(ViewRecommendation.this, getString(R.string.msgNoRecommendationsFound), Toast.LENGTH_LONG).show();
            }
        }
    };

    // Helper to remove a like or dislike
    private boolean removeLikeDislike() {
    	if (Caller.getInstance().removeLikeDislike(curRec.getId())) {
			unhighlightFooter();
			curRec.setLikedByUser(null);
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
        recommendations = Caller.getInstance().getRecommendations();
        /**
        // Backup recommendations hardcoded for testing
        recommendations = new ArrayList<Recommendation>();
        recommendations.add(new Recommendation("Go somewhere", "Do something", 1, 6, 60, 240, 1, false, 43.490656877933816, -80.54049253463745));
        recommendations.add(new Recommendation("Take a Hike", "Venture forth into the backwoods of your childhood.  Bring a backpack and some friends.  Pack a light lunch, but don't eat it until you reach the perfect spot.  Bring a camera and some binoculors and try to see how many birds you can identify.  Make sure to wear proper footwear and always be mindful of keeping the trail clean. This description gets even longer to cause a little bit of overflow here and there.  It's going to be good! I hope it will, or I have just wasted a bunch of my time.  Stupid stupid stupid.", 1, 6, 60, 240, 1, false, 43.490656877933816, -80.54049253463745));
        recommendations.add(new Recommendation("Go fly a kite", "Don't get it caught in hydro line"));
        recommendations.add(new Recommendation("Go rent a convertible you could never afford to own", "Take it down country roads with the top down"));
        /**/
        
        // Since separate threads can't touch another thread's view, send a message via Handler when computation is done to complete building the layout.
        handler.sendEmptyMessage(0);
    }
}
