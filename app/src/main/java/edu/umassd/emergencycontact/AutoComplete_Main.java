package edu.umassd.emergencycontact;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.umassd.emergencycontact.classes.Contact;
import edu.umassd.emergencycontact.classes.Locations;
import edu.umassd.emergencycontact.helpers.CustomListAdapter;
import edu.umassd.emergencycontact.helpers.FileJsonHelper;
import edu.umassd.emergencycontact.helpers.LocationDictionary;
import edu.umassd.emergencycontact.helpers.locationListAdapter;

import static edu.umassd.emergencycontact.R.id.contactList;

public class AutoComplete_Main extends AppCompatActivity {
    public static File fileJson = new File("data/data/edu.umassd.emergencycontact/location.json");
    FileJsonHelper fjhelper = new FileJsonHelper();
    ArrayList<Locations> locationsArrayListList = new ArrayList<Locations>();
    ListView locationList;
    Button eButton;
    LocationDictionary locationDictionary = new LocationDictionary();
    SelectionScreen getloc = new SelectionScreen();
    MainActivity m = new MainActivity();
boolean logging = m.logging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);

        locationList = (ListView) findViewById(R.id.locationList);
/*
        eButton = (Button) findViewById(R.id.eButton);
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callEmergency();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/


        // this will be the first activity shown to the user
        // will contain a list view of locations and  add location button
        // read from JSON and display in listview
        //show listview of pre added locations and the user will have the option to add a new location

        try {
            readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(AutoComplete_Main.this,locationSearch.class);
                startActivityForResult(inte,1);
            }
        });




        }
    /*public Location getCurrentLocation() {

    }*/
    public void callEmergency() throws Exception {
        LatLng currLocation;
        currLocation = new LatLng(getloc.location.getLatitude(), getloc.location.getLongitude());
        if(currLocation==null) {
            if(logging) { System.out.println("callEmergency no location found"); }
        currLocation = new LatLng(44.23, 112.22);
        }
        //Float  va = currLocation.distanceTo(currLocation);
 /*               for(Map.Entry<String, LatLng> entry : locationDictionary.iDandLatlonForComparing().entrySet()) {

                }*/
        HashMap<String, LatLng> temp = new HashMap<String, LatLng>(locationDictionary.iDandLatlonForComparing());
        float distance = 0;
        for (Map.Entry<String, LatLng> te : temp.entrySet()) {

            Location tempSLocation = new Location("souce");
            Location tempDLocation = new Location("destination");
            tempDLocation.setLatitude(te.getValue().latitude);
            tempDLocation.setLongitude(te.getValue().longitude);

            tempSLocation.setLatitude(currLocation.latitude);
            tempSLocation.setLongitude(currLocation.longitude);
            if (distance == 0) {
                distance = tempSLocation.distanceTo(tempDLocation);
            } else if (distance > tempSLocation.distanceTo(tempDLocation)) {
                distance = tempSLocation.distanceTo(tempDLocation);
                locationDictionary.setClosestlocationkey(te.getKey());
                //use this key to get the contacts
            }

        }
        Log.e("closest is ",""+locationDictionary.getClosestlocationkey());

        String jsText = fjhelper.getStringFromFile("data/data/edu.umassd.emergencycontact/contacts.json");


        int GSONleng = new JSONObject(jsText).getJSONObject("Contacts").length();

        //http://stackoverflow.com/questions/5490789/json-parsing-using-gson-for-java
        JsonElement je = new JsonParser().parse(jsText);
        JsonObject jobj = je.getAsJsonObject();
        jobj = jobj.getAsJsonObject("Contacts");

        // String sb = "";

        for(int x=1;x<=GSONleng;x++) {

            Contact temps = new Contact();
            temps.Pname =jobj.getAsJsonObject(x+"").get("Pname").toString().replace("\"", "");;
            temps.Pnumber =jobj.getAsJsonObject(x+"").get("Pnumber").toString().replace("\"", "");
            temps.LocId = jobj.getAsJsonObject(x+"").get("LocId").toString().replace("\"", "");

            if(temps.LocId.equals(locationDictionary.getClosestlocationkey())) {
                //send text here
                String message = "help";
                sendSMS(temps.getPnumber(), message);
            }
        }


    }
    private void sendSMS(String phoneNumber, String message)
    {
        Log.v("phoneNumber",phoneNumber);
        Log.v("Message",message);
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this,Object.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }


    //will get called when returning from search location to update the listview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void readFile() throws Exception {

        String jsText = fjhelper.getStringFromFile(fileJson.toString());

        Log.e("Displaying JS",jsText);

        int GSONleng = new JSONObject(jsText).getJSONObject("locations").length();
        //Log.e("GONSLENg","length "+GSONleng);
        JsonElement je = new JsonParser().parse(jsText);
        JsonObject jobj = je.getAsJsonObject();
        jobj = jobj.getAsJsonObject("locations");
        locationsArrayListList.clear();
        Log.e("lname ",jobj.getAsJsonObject(1+"").get("latLng").getClass().getSimpleName());


        for(int x=1;x<=GSONleng;x++) {

            //decoding lat long sub elements to latlng
            JsonElement jsp = new JsonParser().parse(String.valueOf(jobj.getAsJsonObject(x+"").get("latLng")));
            JsonObject jsonObject = jsp.getAsJsonObject();
            double lat = Double.parseDouble(jsonObject.get("latitude").toString());
            double lon = Double.parseDouble(jsonObject.get("longitude").toString());
            LatLng latlng = new LatLng(lat,lon);


            Locations temp = new Locations();

            temp.lName =jobj.getAsJsonObject(x+"").get("lName").toString().replace("\"", "");
            temp.lId=jobj.getAsJsonObject(x+"").get("lId").toString().replace("\"", "");
            temp.latLng = latlng;
            locationsArrayListList.add(temp);


            ///for comparing the values during emergency to find the closest location
            locationDictionary.addToMap(temp.lId,temp.latLng);
        }

        locationListAdapter adapter = new locationListAdapter(this, locationsArrayListList);
        locationList.setAdapter(adapter);


        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Locations item = (Locations)(locationList.getItemAtPosition(position));
                /*String name = item.getlName()+""+item.getlId()+""+item.getLatLng();
                Log.e("final log",""+name);
                */

                String locID = item.getlId();
                Intent callContact = new Intent(view.getContext(), MainActivity.class);
                callContact.putExtra("locationId",locID);
                startActivity(callContact);


            }
        });

    }


}

