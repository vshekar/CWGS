package edu.umassd.emergencycontact;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String lName;
    public String lId;
    public double lat;
    public double lon;
}
