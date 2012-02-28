package me.taedium.android.domain;

public class UserStats {
	
	public int created;
	public int likes;
	public int dislikes;
	
	public UserStats(int created, int likes, int dislikes) {
		this.created = created;
		this.likes = likes;
		this.dislikes = dislikes;
	}

}
