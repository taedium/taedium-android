package me.taedium.android.domain;

import com.google.gson.annotations.SerializedName;

public class RankingItem {
	
	@SerializedName("user_id")
	int userId;
	String name;
	int score;
	
	// TODO won't need this once we get real data, just necessary for making quick dummy data
	public RankingItem(int rank, String user, int score) {
		this.name = user;
		this.score = score;
	}

}
