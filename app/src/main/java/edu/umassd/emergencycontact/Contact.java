package edu.umassd.emergencycontact;

/**
 * Created by Jayesh on 10/1/16.
 */
public class Contact {

//    public int Id;
    public String Pname;
    public String Pnumber;
    public int LocId;

/*    public Contact(String pname, String pnumber, int LocId) {
        this.setPnumber(pnumber);
        this.setPname(pname);
        this.setLocId(LocId);
    }*/

    public int getLocId() {
        return LocId;
    }

    public void setLocId(int locId) {
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

  /*  public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
*/


}