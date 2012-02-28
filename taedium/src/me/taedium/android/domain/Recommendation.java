package me.taedium.android.domain;

import com.google.gson.annotations.SerializedName;

/*
 * Domain object representing a recommendation
 */
public class Recommendation extends RecommendationBase {

    public enum LocationType { HOME, OUTDOORS, OUT };
    
    public String description;
    public int min_people;
    public int max_people;
    public int min_duration;
    public int max_duration;
    public double cost;   
    public boolean cost_is_per_person;
    public int start_time;
    public int end_time;
    public double lat;
    @SerializedName("long")
    public double lon;
    public boolean flaggedByUser;
  
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
    	this.name = name;
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
    
}