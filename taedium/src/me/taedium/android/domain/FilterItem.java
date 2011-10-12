package me.taedium.android.domain;

public class FilterItem {
    private int resourceId;
    private String label;
    private String feedbackLabel;
    
    public FilterItem(int resourceId, String label, String feedbackLabel) {
        this.setImageResourceId(resourceId);
        this.setLabel(label);
        this.setFeedbackLabel(feedbackLabel);
    }

    public void setImageResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getImageResourceId() {
        return resourceId;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

	public void setFeedbackLabel(String feedbackLabel) {
		this.feedbackLabel = feedbackLabel;
	}

	public String getFeedbackLabel() {
		return feedbackLabel;
	}
}
