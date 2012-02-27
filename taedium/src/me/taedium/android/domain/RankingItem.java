package me.taedium.android.domain;

import com.google.gson.annotations.SerializedName;

public class RankingItem {
	
	@SerializedName("user_id")
	int userId;
	String name;
	int score;
	
}
