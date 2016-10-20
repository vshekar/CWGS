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

public class AutoComplete_Main extends AppCompatActivity {
    public static File fileJson = new File("data/data/edu.umassd.emergencycontact/places.json");
    Button addLocation;
    EditText locName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);
        try {
            createJsonFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addLocation = (Button) findViewById(R.id.add_location);
        locName = (EditText)findViewById(R.id.locName);

//will be active on add_location layout
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locName.getText().toString().trim().equalsIgnoreCase("")) {
                    locName.setError("This field can not be blank");
                }
            }
        });
    }

    private String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;

    }
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public void createJsonFiles() throws Exception {
        Toast.makeText(getApplicationContext(), "creating a file or showing", Toast.LENGTH_SHORT).show();

        if(!fileJson.exists()){
            try {
                fileJson.createNewFile();
                String jsonString = "{\"Contacts\":{}}";

                writeJsonFile(fileJson, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //displaycontacts();
        }
    }
    public static void writeJsonFile(File file, String json)
    {
        BufferedWriter bufferedWriter = null;
        try {

            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }



}
