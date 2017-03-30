package edu.umassd.emergencycontact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import edu.umassd.emergencycontact.classes.Contact;
import edu.umassd.emergencycontact.helpers.CustomListAdapter;
import edu.umassd.emergencycontact.helpers.FileJsonHelper;
import edu.umassd.emergencycontact.helpers.LocationDictionary;

public class MainActivity extends AppCompatActivity {
    public static File fileJson = new File("data/data/edu.umassd.emergencycontact/contacts.json");
    public boolean logging = true;
    ListView listView;
    ArrayList<Contact> contactList = new ArrayList<Contact>();
    FileJsonHelper fjhelper = new FileJsonHelper();
    String locIdfromIntent = null;
    LocationDictionary locationDictionary = new LocationDictionary();
    private View mLayout;
    private static final int PERMISSION_REQUEST_CONTACT =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.mainLayout);

        try {
            fjhelper.createJsonFiles(fileJson, "Contacts");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        locIdfromIntent = intent.getStringExtra("locationId");
        listView = (ListView) findViewById(R.id.contactList);
        try {
            displaycontacts();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void callContacts(View v) {
        if (logging) {Log.e("CALLCONTACTS","Starting call contacts");}
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mLayout,"Contact permission is available. ",Snackbar.LENGTH_SHORT).show();
            listContactDirectory();
            if (logging) {Log.i("Permisson","contact reading permisson granted");}

        } else  {
            requestContactPermission();
        }

    }

    private void listContactDirectory() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void requestContactPermission() {
        if (logging) {Log.i("Permisson","requestcontactpermission block");}

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {
            Snackbar.make(mLayout, "Contact access is required to display the contacts.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_REQUEST_CONTACT);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == AppCompatActivity.RESULT_OK) {
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
                            strFileJson = fjhelper.getStringFromFile(fileJson.toString());
                            JSONObject jsonObj = new JSONObject(strFileJson);
                            Gson gson = new Gson();
                            JsonParser jsonParser = new JsonParser();

                            Contact contact = new Contact();
                            contact.setLocId(locIdfromIntent); // associated with location id
                            contact.setPname(nameContact);
                            contact.setPnumber(cNumber);

                            String jsonStr = jsonParser.parse(gson.toJson(contact)).toString();

                            JSONObject JSONObject = new JSONObject(jsonStr);
                            int leng = jsonObj.getJSONObject("Contacts").length()+1;

                            Toast.makeText(getApplicationContext(), "Adding contact", Toast.LENGTH_SHORT).show();
                            jsonObj.getJSONObject("Contacts").put(leng +"", JSONObject);


                            fjhelper.writeJsonFile(fileJson, jsonObj.toString());



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

    public void displaycontacts() throws Exception {

        String jsText = fjhelper.getStringFromFile(fileJson.toString());
        if(logging){Log.e("Displaying JS",jsText);}


        int GSONleng = new JSONObject(jsText).getJSONObject("Contacts").length();
        if(logging){Log.e("GONSLENg","length "+GSONleng);}

        //http://stackoverflow.com/questions/5490789/json-parsing-using-gson-for-java
        JsonElement je = new JsonParser().parse(jsText);
        JsonObject jobj = je.getAsJsonObject();
        jobj = jobj.getAsJsonObject("Contacts");

        contactList.clear();

        for(int x=1;x<=GSONleng;x++) {

            Contact temp = new Contact();
            temp.Pname =jobj.getAsJsonObject(x+"").get("Pname").toString().replace("\"", "");;
            temp.Pnumber =jobj.getAsJsonObject(x+"").get("Pnumber").toString().replace("\"", "");
            temp.LocId = jobj.getAsJsonObject(x+"").get("LocId").toString().replace("\"", "");

            boolean stemp = temp.LocId.equals(locIdfromIntent);
            if(logging){Log.e("comparing ",temp.LocId+" - "+locIdfromIntent+""+stemp);}
            if(temp.LocId.equals(locIdfromIntent)) {
                contactList.add(temp);
            }

            }

        CustomListAdapter adapter = new CustomListAdapter(this, contactList);
        listView.setAdapter(adapter);

        Toast.makeText(getApplicationContext(), contactList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CONTACT) {
            // Request for contact permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Contacts permission was granted. Starting preview.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                listContactDirectory();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Contacts permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }


}
