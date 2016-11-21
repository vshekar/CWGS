package edu.umassd.emergencycontact;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jayesh on 11/12/16.
 */

public class Location {
    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getlId() {
        return lId;
    }

    public void setlId(String lId) {
        this.lId = lId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String lName;
    public String lId;
    public LatLng latLng;

}
