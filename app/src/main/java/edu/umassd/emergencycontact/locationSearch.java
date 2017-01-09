package edu.umassd.emergencycontact;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import java.io.File;
import edu.umassd.emergencycontact.classes.Locations;
import edu.umassd.emergencycontact.helpers.FileJsonHelper;

public class locationSearch extends AppCompatActivity implements PlaceSelectionListener {
    public static File fileJson = new File("data/data/edu.umassd.emergencycontact/location.json");


//get search values here and then to file and back to autocomplete_main

    private static final String LOG_TAG = "PlaceSelectionListener";
    private TextView txt_location;
    FileJsonHelper filehelper = new FileJsonHelper();
    boolean logging = new MainActivity().logging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlocation_ac);
        try {
            filehelper.createJsonFiles(fileJson,"locations");
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_fragment);
        txt_location = (TextView) findViewById(R.id.txt_location);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        if(logging)Log.e(LOG_TAG ,""+place.getName());
       // txt_location.setText(place.getName());
        try {
            saveToFile(place.getId(),place.getLatLng(),place.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
        //call method to save this info in a file
        //then go back to prev activity to list locations in a list view
    }

    private void saveToFile(String id, LatLng latLng, CharSequence name) throws Exception {
        String strFileJson = null;
        strFileJson = filehelper.getStringFromFile(fileJson.toString());
        JSONObject jsonObj = new JSONObject(strFileJson);

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        Locations local = new Locations();
        local.setLatLng(latLng);
        local.setlId(id);
        local.setlName(name.toString());
        String jsStr = jsonParser.parse(gson.toJson(local)).toString();
        JSONObject Json = new JSONObject(jsStr);


        int len = jsonObj.getJSONObject("locations").length()+1;
        jsonObj.getJSONObject("locations").put(len+"", Json);
        filehelper.writeJsonFile(fileJson, jsonObj.toString());

    }

    @Override
    public void onError(Status status) {

    }
}
