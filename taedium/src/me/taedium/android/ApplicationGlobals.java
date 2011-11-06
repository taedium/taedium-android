package me.taedium.android;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

//Singleton class for holding globals
//Apparently the recommended way for passing around big lists of objects
//between activities is via globals, hence the existence of this class.
public class ApplicationGlobals {
    private static final String MODULE = "GLOBALS";
    
    //TODO: used age instead of pass KIDFRIENDLY/ADULTSONLY
    public enum RecParamType {
        INDOOR, OUTDOOR, AROUNDTOWN, 
        KIDFRIENDLY, ADULTSONLY,
        PEOPLE, COST, MINDURATION, MAXDURATION,
        TOD, LAT, LONG, MAXDIST, LIMIT
    };
    /*
     * TODO: Currently not handling/using
     * TOD, LIMIT, ADULTSONLY
     */
    
    /* 
     * TODO: Currently on the server side we use 5km by default. On the UI/app side it
     * might be a good idea to use a slider to specify radius or perhaps a choice of
     * "walk, bike, drive" or something like that rather than entering in a number.
     */
    public static int DEFAULT_MAX_DIST = 5;
    
    private final HashSet<RecParamType> unaryParams = new HashSet<RecParamType>();    
    private static ApplicationGlobals instance = null;    
    private HashMap<RecParamType, String> recommendationParams = new HashMap<RecParamType, String>();
    private String userpass;
    private boolean loggedIn = false;
    private boolean mLocationEnabled = false;
    private CurrentLocationListener mLocationListener;
    private LocationManager mLocationManager;
    
    private ApplicationGlobals() {
        unaryParams.add(RecParamType.INDOOR);
        unaryParams.add(RecParamType.OUTDOOR);
        unaryParams.add(RecParamType.AROUNDTOWN);
        unaryParams.add(RecParamType.KIDFRIENDLY);
        unaryParams.add(RecParamType.ADULTSONLY);
        mLocationListener = new CurrentLocationListener();
    }
    
    public static ApplicationGlobals getInstance() {
        if (instance ==null) {
            instance = new ApplicationGlobals();
        }
        return instance;
    }
    
    public void startLocationListener(Context context) {
    	/* Use the LocationManager class to obtain GPS locations */
	    mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    
	    // TODO when adding an activity may want to use GPS instead of network...or speed up the GPS!
	    String provider = getLocationProvider();
	    if (provider == null) {
	        Toast.makeText(context, "No Location providers enabled.", Toast.LENGTH_LONG).show();
	    } else {
            Log.i(MODULE, "Initializing location listener with: " + provider);
    	    mLocationManager.requestLocationUpdates(provider, 0, 100, mLocationListener);
	    }
    }
    
    public void stopLocationListener() {
    	mLocationManager.removeUpdates(mLocationListener);
    }
    
    public boolean isUnaryParam(RecParamType type) {
        return unaryParams.contains(type);
    }
    
    public String getUser() {
    	int index = userpass.indexOf(':');
    	return userpass.substring(0, index);
    }
    
    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getUserpass() {
        return userpass;
    }

    public HashMap<RecParamType,String> getRecommendationParams() {
        return recommendationParams;
    }

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public static int getCurrentTimeInSeconds() {
	    Calendar cal = Calendar.getInstance();
	    int seconds = cal.getTime().getSeconds();
	    seconds += cal.getTime().getMinutes() * 60;
	    seconds += cal.getTime().getHours() * 3600;	    
	    return seconds;
	}
	
	public Location getCurrentLocation() {
	    Location l = mLocationListener.getCurrentLocation();
	    if (l == null) {
	        String provider = getLocationProvider();
	        if (provider == null) return null;
	        return mLocationManager.getLastKnownLocation(provider);
	    }
		return l;
	}
	
	public String getLocationProvider() {
	    if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
	        return LocationManager.NETWORK_PROVIDER;
	    }
	    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	        return LocationManager.GPS_PROVIDER;
	    }
	    return null;
	}
	
	public boolean locationIsEnabled(Activity currentActivity) {
	    LocationManager locationManager = (LocationManager)currentActivity.getSystemService(
                Context.LOCATION_SERVICE);
	    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
	        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	public void setLocationEnabled(boolean mUseLocation) {
		this.mLocationEnabled = mUseLocation;
	}

	public boolean isLocationEnabled() {
		return mLocationEnabled;
	}

	private class CurrentLocationListener implements LocationListener {
	    private Location location = null;
        public void onLocationChanged(Location location) {
            this.location = location;
        }

        public Location getCurrentLocation() {
            return this.location;
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
}
