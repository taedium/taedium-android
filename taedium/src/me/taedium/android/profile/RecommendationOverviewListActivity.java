package me.taedium.android.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import me.taedium.android.HeaderActivity;
import me.taedium.android.R;
import me.taedium.android.domain.RecommendationBase;
import me.taedium.android.domain.RecommendationBaseAdapter;
import me.taedium.android.domain.RecommendationOverviewList;
import me.taedium.android.view.ViewRecommendation;

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
		final RecommendationOverviewList recList = extras.getParcelable(KEY_RECOMMENDATION_LIST);
        ListView lvActivities = (ListView) findViewById(R.id.lvActivities);
        lvActivities.setAdapter(new RecommendationBaseAdapter(this, R.id.list_item_text, recList.toRecommendationBaseArray()));
        lvActivities.setTextFilterEnabled(true);
        lvActivities.setOnItemClickListener(new OnItemClickListener() {
			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RecommendationBase rec = recList.get((int) id);
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
