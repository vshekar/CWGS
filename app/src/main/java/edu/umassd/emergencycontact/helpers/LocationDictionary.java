package edu.umassd.emergencycontact.helpers;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;

/**
 * Created by Jayesh on 11/27/16.
 */

public class LocationDictionary {
    HashMap<String, LatLng> iDandLatlon = new HashMap<>();
    String closestlocationkey;

    public void addToMap(String key, LatLng latLng) {
        if(iDandLatlon.containsKey(key)) {} else {
            iDandLatlon.put(key,latLng);
        }
    }
    public HashMap iDandLatlonForComparing() {
        return iDandLatlon;
    }
    public String getClosestlocationkey() {
        Log.e("getting ","id = "+closestlocationkey);
        return closestlocationkey;
    }
    public void setClosestlocationkey(String id) {
        Log.e("setting location key","id = "+id);
        closestlocationkey =id;
    }
}
