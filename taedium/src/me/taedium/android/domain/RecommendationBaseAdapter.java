package me.taedium.android.domain;

import me.taedium.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecommendationBaseAdapter extends ArrayAdapter<RecommendationBase> {

	RecommendationBase [] recs;
    Context context;

    public RecommendationBaseAdapter(Context context, int textViewResourceId, RecommendationBase [] objects) {
        super(context, textViewResourceId, objects);
        this.recs = objects;
        this.context = context;
    }    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_base_recommendation, null);
            }
            RecommendationBase o = recs[position];
            if (o != null) {
	            TextView name = (TextView) v.findViewById(R.id.tvListItemActivityName);
            	
	            if (name != null) {
	            	name.setText(o.name);
	            }
	            
            }
            return v;
    }
}
