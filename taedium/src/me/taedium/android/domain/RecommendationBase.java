package me.taedium.android.domain;

public class RecommendationBase {
	
	public int id;
	public String name;
	
	public RecommendationBase() {
		
	}
	public RecommendationBase(String name) {
		this.name = name;
	}
	
	// TODO probably don't need this once profile apis exist (used for easier testing)
	public RecommendationBase(int id, String name) {
		this.id = id;
		this.name = name;
	}
	

}
