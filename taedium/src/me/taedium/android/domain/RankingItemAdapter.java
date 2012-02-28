package me.taedium.android.domain;

import me.taedium.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RankingItemAdapter extends ArrayAdapter<RankingItem> {
	
	RankingItem [] rankingItems;
    Context context;

    public RankingItemAdapter(Context context, int textViewResourceId, RankingItem [] objects) {
        super(context, textViewResourceId, objects);
        this.rankingItems = objects;
        this.context = context;
    }    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_ranking, null);
            }
            RankingItem o = rankingItems[position];
            if (o != null) {
	            TextView rank = (TextView) v.findViewById(R.id.tvListItemRank);
	            TextView user = (TextView) v.findViewById(R.id.tvListItemUser);
	            TextView score = (TextView) v.findViewById(R.id.tvListItemScore);
            	
	            if (rank != null) {
	            	rank.setText(Integer.toString(position + 1));
	            }
	            if (user != null){
	            	user.setText(o.name);
	            }
	            if (score != null) {
	            	score.setText(Integer.toString(o.score));
	            }
            }
            return v;
    }

}
