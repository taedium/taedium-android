package me.taedium.android.domain;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import me.taedium.android.R;

public class FilterItemAdapter extends ArrayAdapter<FilterItem> {
    
    FilterItem[] filterItems;
    Context context;

    public FilterItemAdapter(Context context, int textViewResourceId, FilterItem[] objects) {
        super(context, textViewResourceId, objects);
        this.filterItems = objects;
        this.context = context;
    }    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            FilterItem o = filterItems[position];
            if (o != null) {
	            TextView label = (TextView) v.findViewById(R.id.list_item_text);
	            ImageView image = (ImageView) v.findViewById(R.id.ivListItemIcon);
	            TextView feedbackLabel = (TextView) v.findViewById(R.id.list_item_feedback);
	            if (label != null) {
	                  label.setText(o.getLabel());                            }
	            if(image != null){
	                  image.setImageResource(o.getImageResourceId());
	            }
	            if (feedbackLabel != null) {
	            	feedbackLabel.setText(o.getFeedbackLabel());                    
	                if (o.getFeedbackLabel().equalsIgnoreCase(parent.getContext().getResources().getStringArray(R.array.lvFeedbackDefaultsArray)[position])) {
	                	feedbackLabel.setTextColor(Color.LTGRAY);        	
	                }
	                else {
	                	feedbackLabel.setTextColor(Color.BLACK);        	
	                }
	            }
            }
            return v;
    }
    
    public void replace(FilterItem changed, int index) {
    	/*super.remove(old);
    	super.insert(changed, index);*/
    	filterItems[index]=changed;    	
    }
}
