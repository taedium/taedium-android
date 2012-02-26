package me.taedium.android.domain;

import com.google.gson.annotations.SerializedName;

public class RecommendationBase {
	public int id;
	public String name;
    @SerializedName("like")
    public Boolean likedByUser;
}
