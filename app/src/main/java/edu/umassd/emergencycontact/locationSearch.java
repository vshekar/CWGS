package edu.umassd.emergencycontact;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class locationSearch extends AppCompatActivity implements PlaceSelectionListener {


//get search values here and then to file and back to autocomplete_main


    private static final String LOG_TAG = "PlaceSelectionListener";
    private TextView txt_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlocation_ac);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_fragment);
        txt_location = (TextView) findViewById(R.id.txt_location);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        Log.e("Place: " ,""+"hellcat");

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.e(LOG_TAG ,""+place.getName());
        txt_location.setText(place.getName());
        //call method to save this info in a file
        //then go back to prev activity to list locations in a list view
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG,"sss"+"ss");

    }
}
