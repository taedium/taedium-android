package me.taedium.android.profile;

import java.util.ArrayList;

import me.taedium.android.HeaderActivity;
import me.taedium.android.R;
import me.taedium.android.api.Caller;
import me.taedium.android.domain.RecommendationBase;
import me.taedium.android.domain.RecommendationBaseAdapter;
import me.taedium.android.view.ViewRecommendation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendationOverviewListActivity extends HeaderActivity implements Runnable {
	
	public static final String KEY_ACTIVITIES_TYPE = "activities_type";
	public static final int KEY_USER_ADDED_ACTIVITIES = 201;
	public static final int KEY_USER_LIKED_ACTIVITIES = 202;
	public static final int KEY_USER_DISLIKED_ACTIVITIES = 203;
	
	private TextView tvCategoryName;
	private ProgressDialog progressDialog;
	private ArrayList <RecommendationBase> recs = new ArrayList<RecommendationBase>();
	private int keyActivitiesType;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommendation_overview);
		
		Bundle extras = getIntent().getExtras();
		keyActivitiesType = extras.getInt(KEY_ACTIVITIES_TYPE);
		initializeHeader("");
		
		// Setup the category name TextView
		tvCategoryName = (TextView) findViewById(R.id.tvHeader);
		switch (keyActivitiesType) {
			case KEY_USER_ADDED_ACTIVITIES:
				tvCategoryName.setText(getString(R.string.added_activities_title));
				break;
			case KEY_USER_LIKED_ACTIVITIES:
				tvCategoryName.setText(getString(R.string.liked_activities_title));
				break;
			case KEY_USER_DISLIKED_ACTIVITIES:
				tvCategoryName.setText(getString(R.string.disliked_activities_title));
				break;
		}
        // Get user stats and setup listview
        progressDialog = ProgressDialog.show(this, "", getString(R.string.msgLoadingActivities));
        new Thread(this).start();
	}
	
	private Handler handler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			
			progressDialog.dismiss();
			
			// Setup the activity list
	        ListView lvActivities = (ListView) findViewById(R.id.lvActivities);
	        final RecommendationBase[] recsArray = new RecommendationBase[recs.size()];
	        for (int i = 0; i < recs.size(); i++) {
	        	recsArray[i] = recs.get(i);
	        }
	        if (recsArray.length < 1) {
	        	lvActivities.setVisibility(View.INVISIBLE);
	        } else {
		        lvActivities.setAdapter(new RecommendationBaseAdapter(RecommendationOverviewListActivity.this, R.id.list_item_text, recsArray));
		        lvActivities.setTextFilterEnabled(true);
		        lvActivities.setOnItemClickListener(new OnItemClickListener() {
					@Override
		            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						RecommendationBase rec = recsArray[(int) id];
						Bundle bundle = new Bundle();
						bundle.putBoolean(ViewRecommendation.KEY_DISPLAY_BY_ID, true);
						bundle.putInt(ViewRecommendation.KEY_ID_TO_FETCH, rec.id);
		                Intent i = new Intent(RecommendationOverviewListActivity.this, ViewRecommendation.class);
		                i.putExtras(bundle);
		                startActivity(i);
					}
				});
	        }
		}
	};

	@Override
	public void run() {
		switch (keyActivitiesType) {
			case KEY_USER_ADDED_ACTIVITIES:
				recs = Caller.getInstance(getApplicationContext()).getActivitiesAddedByUser();
				break;
			case KEY_USER_LIKED_ACTIVITIES:
				recs = Caller.getInstance(getApplicationContext()).getActivitiesLikedByUser();
				break;
			case KEY_USER_DISLIKED_ACTIVITIES:
				recs = Caller.getInstance(getApplicationContext()).getActivitiesDislikedByUser();
				break;
		}
		handler.sendEmptyMessage(0);
	}
}
