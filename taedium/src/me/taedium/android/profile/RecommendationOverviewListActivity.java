package me.taedium.android.profile;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import me.taedium.android.HeaderActivity;
import me.taedium.android.R;
import me.taedium.android.domain.RecommendationBaseAdapter;
import me.taedium.android.domain.RecommendationOverviewList;

public class RecommendationOverviewListActivity extends HeaderActivity {
	
	public static final String KEY_RECOMMENDATION_LIST = "rec_list";
	public static final String KEY_LIST_TITLE_RES_ID = "list_title_res_id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommendation_overview);
		
		Bundle extras = getIntent().getExtras();
		
		// Setup the category name TextView
		TextView tvCategoryName = (TextView) findViewById(R.id.tvRecommendationCategoryName);
		tvCategoryName.setText(extras.getInt(KEY_LIST_TITLE_RES_ID));
		
		// Setup the activity list
		RecommendationOverviewList recList = extras.getParcelable(KEY_RECOMMENDATION_LIST);
        ListView lvActivities = (ListView) findViewById(R.id.lvActivities);
        lvActivities.setAdapter(new RecommendationBaseAdapter(this, R.id.list_item_text, recList.toRecommendationBaseArray()));
        lvActivities.setTextFilterEnabled(true);
	}

}
