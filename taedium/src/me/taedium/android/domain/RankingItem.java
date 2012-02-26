package me.taedium.android.domain;

public class RankingItem {
	
	int rank;
	String user;
	int score;
	
	// TODO won't need this once we get real data, just necessary for making quick dummy data
	public RankingItem(int rank, String user, int score) {
		this.rank = rank;
		this.user = user;
		this.score = score;
	}

}
