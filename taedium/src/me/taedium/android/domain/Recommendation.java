package me.taedium.android.domain;

import com.google.gson.annotations.SerializedName;

/*
 * Domain object representing a recommendation
 */
public class Recommendation extends RecommendationBase {

    public enum LocationType { HOME, OUTDOORS, OUT };
    
    private String description;
    private int min_people;
    private int max_people;
    private int min_duration;
    private int max_duration;
    private double cost;   
    private boolean cost_is_per_person;
    private int start_time;
    private int end_time;
    private double lat;
    @SerializedName("long")
    private double lon;
    @SerializedName("is_liked_by_user")
    private Boolean likedByUser;
    private boolean flaggedByUser;
  
    // Public constructors for various arguments
    // The only required argument for the API is name 
    public Recommendation(String name) {
        this(name, "");
    }
    
    public Recommendation(String name, String description) {
        this(name, description, 0, 0, 0, 0, 0, false, 0, 0, null, false);
    }
    
    public Recommendation(String name, String description, int min_people, int max_people) {
        this(name, description, min_people, max_people, 0, 0, 0, false, 0, 0, null, false);
    }
    
    public Recommendation(String name, String description, int min_people, int max_people, int min_duration, int max_duration) {
        this(name, description, min_people, max_people, min_duration, max_duration, 0, false, 0, 0, null, false);
    }
    
    public Recommendation(String name, String description, int min_people, int max_people,
                            int min_duration, int max_duration, double cost, boolean cost_per_person, double lat, double lon, Boolean likedByUser, boolean flaggedByUser) {
    	super(name);
        this.description = description;
        this.min_people = min_people;
        this.max_people = max_people;
        this.min_duration = min_duration;
        this.max_duration = max_duration;
        this.cost = cost;
        this.cost_is_per_person = cost_per_person;
        this.lat = lat;
        this.lon = lon;
        this.likedByUser = likedByUser;
        this.flaggedByUser = flaggedByUser;
    }
     
    public void setCostIsPerPerson(boolean costIsPerPerson) {
        this.cost_is_per_person = costIsPerPerson;
    }
    public boolean isCostIsPerPerson() {
        return cost_is_per_person;
    }
    public void setMaxDuration(int maxDuration) {
        this.max_duration = maxDuration;
    }
    public int getMaxDuration() {
        return max_duration;
    }
    public void setMaxPeople(int maxPeople) {
        this.max_people = maxPeople;
    }
    public int getMaxPeople() {
        return max_people;
    }
    public void setStartTime(int startTime) {
        this.start_time = startTime;
    }
    public int getStartTime() {
        return start_time;
    }
    public void setMinPeople(int minPeople) {
        this.min_people = minPeople;
    }
    public int getMinPeople() {
        return min_people;
    }
    public void setMinDuration(int minDuration) {
        this.min_duration = minDuration;
    }
    public int getMinDuration() {
        return min_duration;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public double getCost() {
        return cost;
    }
    public void setEndTime(int endTime) {
        this.end_time = endTime;
    }
    public int getEndTime() {
        return end_time;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
	public void setLong(double lon) {
		this.lon = lon;
	}
	public double getLong() {
		return lon;
	}

	public void setLikedByUser(Boolean likedByUser) {
		this.likedByUser = likedByUser;
	}

	public Boolean getLikedByUser() {
		return likedByUser;
	}

	public void setFlaggedByUser(boolean flaggedByUser) {
		this.flaggedByUser = flaggedByUser;
	}

	public boolean isFlaggedByUser() {
		return flaggedByUser;
	}
}