package edu.umassd.emergencycontact.classes;

/**
 * Created by Jayesh on 10/1/16.
 */
public class Contact {

//    public int Id;
    public String Pname;
    public String Pnumber;
    public String LocId;

    public String getLocId() {
        return LocId;
    }

    public void setLocId(String locId) {
        LocId = locId;
    }

    public String getPnumber() {
        return Pnumber;
    }

    public void setPnumber(String pnumber) {
        Pnumber = pnumber;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

}