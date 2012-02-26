package me.taedium.android.domain;

public class FilterItem {
    public int imageResourceId;
    public String label;
    public String feedbackLabel;
    
    public FilterItem(int resourceId, String label, String feedbackLabel) {
        this.imageResourceId = resourceId;
        this.label = label;
        this.feedbackLabel = feedbackLabel;
    }
    
}
