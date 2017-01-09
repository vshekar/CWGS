package edu.umassd.emergencycontact;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umassd.emergencycontact.classes.Contact;
import edu.umassd.emergencycontact.classes.Locations;
import edu.umassd.emergencycontact.helpers.FileJsonHelper;
import edu.umassd.emergencycontact.helpers.LocationDictionary;
import edu.umassd.emergencycontact.helpers.locationListAdapter;

// this will be the first activity shown to the user
// will contain a list view of locations and  add location button
// read from JSON and display in listview
//show listview of pre added locations and the user will have the option to add a new location

public class AutoComplete_Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static File fileJson = new File("data/data/edu.umassd.emergencycontact/location.json");
    FileJsonHelper fjhelper = new FileJsonHelper();

    ArrayList<Locations> locationsArrayListList = new ArrayList<Locations>();
    ListView locationList;
    LocationDictionary locationDictionary = new LocationDictionary();
    MainActivity m = new MainActivity();
    boolean logging = m.logging;
    Button eButton;
    View mLayout;
    static final int PERMISSION_LOCATION = 0;
    Location location; //will contain current location, will be got from google play services after granting location permission
    Locations locationClass = new Locations();

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    private GoogleApiClient mGoogleApiClient;

    public AutoComplete_Main() throws Exception {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);
        eButton = (Button)findViewById(R.id.eButton);
        locationList = (ListView) findViewById(R.id.locationList);
        mLayout = findViewById(R.id.mLayout);
        getSendSmsPermission();
        //init google play services to get location | uses builder pattern
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        //test emergency button
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //location is got right after user grants permission
                callEmergency(location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            //update list view from file, if not found creates a new file
            readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingbutton);

        //add location button click event
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent inte = new Intent(AutoComplete_Main.this,locationSearch.class);
                startActivityForResult(inte,1);
            }
        });

        }

    private void getSendSmsPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)) {
            Snackbar.make(mLayout, "SMS access is required to send SMS", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(AutoComplete_Main.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                        }
                    }).show();}
        else {
            Snackbar.make(mLayout, "Permission not available, Requesting permission to send SMS", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(AutoComplete_Main.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

     /*   if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }*/

    }

    //test emergency button | will be replaced with events later like user shake etc..
    public void callEmergency(Location location) throws Exception {
        //converting location to latlong obj
        LatLng currLocation = new LatLng(location.getLatitude(),location.getLongitude());

        if(currLocation==null) {
            if(logging) { System.out.println("callEmergency no location found"); }
            currLocation = new LatLng(44.23, 112.22);
        }
        if(logging) { System.out.println("Location is "+currLocation.toString()); }

        HashMap<String, LatLng> temp = new HashMap<>(locationDictionary.iDandLatlonForComparing());

        if(logging) {
            for(Map.Entry<String,LatLng> entry: locationDictionary.iDandLatlonForComparing().entrySet()) {
                Log.e("Key ",""+entry.getKey());
                Log.e("Value",""+entry.getValue());
            }
        }
        //for storing the closest distance and key in two unsorted lists | later use the index to find the key
        List<Float> distance = new ArrayList<Float>();
        List<String> dKey = new ArrayList<>();

        for (Map.Entry<String, LatLng> te : temp.entrySet()) {
            //converting latlon to location obj | source & destination
            Location tempSLocation = new Location("source");
            Location tempDLocation = new Location("destination");
            tempDLocation.setLatitude(te.getValue().latitude);
            tempDLocation.setLongitude(te.getValue().longitude);

            tempSLocation.setLatitude(currLocation.latitude);
            tempSLocation.setLongitude(currLocation.longitude);

                distance.add(tempSLocation.distanceTo(tempDLocation));
                dKey.add(te.getKey());

                locationDictionary.setClosestlocationkey(te.getKey()); //not working, hence used list
        }


        int minIndex = distance.indexOf(Collections.min(distance)); //matching the index with the dKey

        if(logging)Log.e("min key ",""+dKey.get(minIndex));

        //should generalize instead of providing path every time
        String jsText = fjhelper.getStringFromFile("data/data/edu.umassd.emergencycontact/contacts.json");


        int GSONleng = new JSONObject(jsText).getJSONObject("Contacts").length();

        JsonElement je = new JsonParser().parse(jsText);
        JsonObject jobj = je.getAsJsonObject();
        jobj = jobj.getAsJsonObject("Contacts");

        for(int x=1;x<=GSONleng;x++) {

            Contact temps = new Contact();
            temps.Pname =jobj.getAsJsonObject(x+"").get("Pname").toString().replace("\"", "");;
            temps.Pnumber =jobj.getAsJsonObject(x+"").get("Pnumber").toString().replace("\"", "");
            temps.LocId = jobj.getAsJsonObject(x+"").get("LocId").toString().replace("\"", "");

            //if(temps.LocId.equals(locationDictionary.getClosestlocationkey())) {
            if(temps.LocId.equals(dKey.get(minIndex))) {
                //send text here
                String message = "Help my current location is http://maps.google.com/maps?q="+location.getLatitude()+","+location.getLongitude();
                sendSMS(temps.getPnumber(), message);
            }
        }
    }

    //send sms helper function
    private void sendSMS(String phoneNumber, String message)
    {
        Toast.makeText(this,"sending to "+phoneNumber, Toast.LENGTH_LONG).show();
        if(logging) {Log.v("phoneNumber",phoneNumber);
        Log.v("Message",message); }
        //uncomment the bottom part to send sms

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

        if(logging)Log.e("Displaying JS",jsText);

        int GSONleng = new JSONObject(jsText).getJSONObject("locations").length();
        //Log.e("GONSLENg","length "+GSONleng);
        JsonElement je = new JsonParser().parse(jsText);
        JsonObject jobj = je.getAsJsonObject();
        jobj = jobj.getAsJsonObject("locations");
        locationsArrayListList.clear();
        if(logging)Log.e("lname ",jobj.getAsJsonObject(1+"").get("latLng").getClass().getSimpleName());


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

            if(logging)Log.e("adding to  map",""+temp.lId+"-"+temp.latLng);
            ///for comparing the values during emergency to find the closest location
            locationDictionary.addToMap(temp.lId,temp.latLng);
        }
        //customadapter for listview | have used the same adapter for contacts also
        locationListAdapter adapter = new locationListAdapter(this, locationsArrayListList);
        locationList.setAdapter(adapter);

        //adding on click event on location
        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Locations item = (Locations)(locationList.getItemAtPosition(position));
                /*String name = item.getlName()+""+item.getlId()+""+item.getLatLng();
                Log.e("final log",""+name);
                */

                String locID = item.getlId();
                Intent callContact = new Intent(view.getContext(), MainActivity.class);
                callContact.putExtra("locationId",locID); //passing locID to next class MainActivity.class
                startActivity(callContact);


            }
        });

    }

    //location helper function
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }
    //location helper function
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //location helper function
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(mLayout, "GPS access is required to get the current location", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(AutoComplete_Main.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                        }
                    }).show();
        } else {
            Snackbar.make(mLayout, "Permission not available, Requesting location permission", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(AutoComplete_Main.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        }
    }
    //location helper function
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Snackbar.make(mLayout, "Location permission is available", Snackbar.LENGTH_SHORT).show();
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            locationClass.currentLocation = new LatLng(location.getLatitude(),location.getLongitude());


            if(logging) { Log.e("Location",""+location.toString());}
        }else {
            if(logging) { Log.e("Location","no permission");}
            requestLocationPermission();
        }
        if(logging) { Log.e("Location","locationclass curr location "+locationClass.currentLocation);}

    }
    //location helper function
    private void handleNewLocation(Location location) {
        if(logging) { Log.e("Location",""+location.toString());}

    }
    //location helper function
    @Override
    public void onConnectionSuspended(int i) {
        if(logging) { Log.e("onConnection Suspended","connecion suspended");}
    }
    //location helper function
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(logging) { Log.e("conn failed","Failed");}
    }

    //location helper function
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Location permission was Granted..",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Location permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        if(requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Sending SMS Permission Granted..",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }else{
                // Permission request was denied.
                Snackbar.make(mLayout, "Sending SMS permission was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

        // END_INCLUDE(onRequestPermissionsResult)
    }



}

