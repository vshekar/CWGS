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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {
    public static File fileJson = new File("data/data/edu.umassd.emergencycontact/contacts.json");

    Button addLocation;
    TextView viewLocation,displayJson;
    EditText locName;
    int locId = 1;
    public ArrayList<Contact> contactList = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createJsonFiles();

        addLocation = (Button) findViewById(R.id.add_location);
        locName = (EditText)findViewById(R.id.locName);


//will be active on add_location layout

/*        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locName.getText().toString().trim().equalsIgnoreCase("")) {
                    locName.setError("This field can not be blank");
                }
            }
        });*/
    }

    public void callContacts(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == ActionBarActivity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData,null,null,null,null);

                if(c.moveToFirst()) {
                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String number = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if(number.equalsIgnoreCase("1")) {
                        //using pho to query inbuilt database | ContactsContract.CommonDataKinds
                        Cursor pho = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);

                        pho.moveToFirst();
                        String cNumber = pho.getString(pho.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));


                        String strFileJson = null;
                        try {
                            strFileJson = getStringFromFile(fileJson.toString());
                            JSONObject jsonObj = new JSONObject(strFileJson);
                            Gson gson = new Gson();
                            JsonParser jsonParser = new JsonParser();

                            Contact contact = new Contact();
                            contact.setLocId(locId); //hard coding it for now | associated with location id
                            contact.setPname(nameContact);
                            contact.setPnumber(cNumber);

                            String jsonStr = jsonParser.parse(gson.toJson(contact)).toString();

                            JSONObject JSONObject = new JSONObject(jsonStr);
                            int leng = jsonObj.getJSONObject("Contacts").length()+1;

                            Toast.makeText(getApplicationContext(), "Writing to file", Toast.LENGTH_SHORT).show();
                            jsonObj.getJSONObject("Contacts").put(leng +"", JSONObject);


                            writeJsonFile(fileJson, jsonObj.toString());



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }  else {


                    }

                    try {
                        displaycontacts();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void displaycontacts() throws Exception {
        //Toast.makeText(getApplicationContext(), "displaying", Toast.LENGTH_SHORT).show();

        displayJson= (TextView) findViewById(R.id.displayJson);

        String jsText = getStringFromFile(fileJson.toString());
        int GSONleng = new JSONObject(jsText).getJSONObject("Contacts").length()+1;

        //http://stackoverflow.com/questions/5490789/json-parsing-using-gson-for-java
        JsonElement je = new JsonParser().parse(jsText);
        JsonObject jobj = je.getAsJsonObject();
        jobj = jobj.getAsJsonObject("Contacts");

        String sb = "";
        for(int x=1;x<=GSONleng;x++) {
            //Contact tempc = new Contact(jobj.getAsJsonObject(x+"").get("Pname").toString(), jobj.getAsJsonObject(x+"").get("Pnumber").toString(),1);
            Contact tempc = new Contact();
            tempc.Pname =jobj.getAsJsonObject(x+"").get("Pname").toString();
            tempc.Pnumber =jobj.getAsJsonObject(x+"").get("Pnumber").toString();

            contactList.add(tempc);
            //sb+= ("\n"+jobj.getAsJsonObject(x+"").get("Pnumber").toString());
            displayJson.setText(sb);
        }

        Toast.makeText(getApplicationContext(), contactList.size(), Toast.LENGTH_SHORT).show();
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

    public void createJsonFiles(){
        Toast.makeText(getApplicationContext(), "creating a file", Toast.LENGTH_SHORT).show();

        if(!fileJson.exists()){
            try {
                fileJson.createNewFile();
                String jsonString = "{\"Contacts\":{}}";

                writeJsonFile(fileJson, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
