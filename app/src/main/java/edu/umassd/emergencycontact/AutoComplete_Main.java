package edu.umassd.emergencycontact;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static edu.umassd.emergencycontact.R.id.contactList;
import static edu.umassd.emergencycontact.R.id.locationList;

public class AutoComplete_Main extends AppCompatActivity {
    public static File fileJson = new File("data/data/edu.umassd.emergencycontact/location.json");
    FileJsonHelper fjhelper = new FileJsonHelper();
    ArrayList<Location> locationArrayListList = new ArrayList<Location>();
    ListView locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationList = (ListView) findViewById(R.id.locationList);


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
                startActivity(inte);
            }
        });
        }

    public void readFile() throws Exception {

        String jsText = fjhelper.getStringFromFile(fileJson.toString());

        Log.e("Displaying JS",jsText);

        int GSONleng = new JSONObject(jsText).getJSONObject("locations").length();
        //Log.e("GONSLENg","length "+GSONleng);
        JsonElement je = new JsonParser().parse(jsText);
        JsonObject jobj = je.getAsJsonObject();
        jobj = jobj.getAsJsonObject("locations");
        locationArrayListList.clear();
        Log.e("lname ",jobj.getAsJsonObject(1+"").get("latLng").getClass().getSimpleName());


        for(int x=1;x<=GSONleng;x++) {

            //decoding lat long sub elements to latlng
            JsonElement jsp = new JsonParser().parse(String.valueOf(jobj.getAsJsonObject(x+"").get("latLng")));
            JsonObject jsonObject = jsp.getAsJsonObject();
            double lat = Double.parseDouble(jsonObject.get("latitude").toString());
            double lon = Double.parseDouble(jsonObject.get("longitude").toString());
            LatLng latlng = new LatLng(lat,lon);


            Location temp = new Location();

            temp.lName =jobj.getAsJsonObject(x+"").get("lName").toString().replace("\"", "");
            temp.lId=jobj.getAsJsonObject(x+"").get("lId").toString().replace("\"", "");
            temp.latLng = latlng;

            locationArrayListList.add(temp);
        }

        locationListAdapter adapter = new locationListAdapter(this, locationArrayListList);
        locationList.setAdapter(adapter);




    }

}

