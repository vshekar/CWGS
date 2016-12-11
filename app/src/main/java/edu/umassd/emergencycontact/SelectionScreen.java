package edu.umassd.emergencycontact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.umassd.emergencycontact.classes.Locations;


/**
 * Created by Jayesh on 11/26/16.
 */


public class SelectionScreen extends AppCompatActivity implements View.OnClickListener {

    Button locationAc, emergency, share, record;
    public Location myLocation;
    public static final String TAG = SelectionScreen.class.getSimpleName();
    Button eButton;
    Locations locationClass = new Locations();
    View mLayout;
    static final int PERMISSION_LOCATION = 0;
    LocationManager locationManager;
    String provider;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionscreen);
        locationAc = (Button) findViewById(R.id.locationActivity);
        emergency = (Button) findViewById(R.id.emergencyActivity);
        share = (Button) findViewById(R.id.shareActivity);
        record = (Button) findViewById(R.id.recordActivity);
        eButton = (Button) findViewById(R.id.eButton);
        mLayout = findViewById(R.id.mLayout);

        locationAc.setOnClickListener(this);
        emergency.setOnClickListener(this);
        share.setOnClickListener(this);
        record.setOnClickListener(this);

//        getCurrentLocation();

    }
/*

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Snackbar.make(mLayout, "Location permission is available", Snackbar.LENGTH_SHORT).show();
            getCurrentLocation();
        } else {

        }
    }
*/
/*

    private void getCurrentLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        provider = locationManager.getBestProvider(c, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mLayout, "Location permission is available", Snackbar.LENGTH_SHORT).show();
            location = locationManager.getLastKnownLocation(provider);
            getLocation();
        } else {
            requestLocationPermission();
        }

    }
*/

/*
    private void getLocation() {
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Toast.makeText(getApplicationContext(), "GPS Coords not found", Toast.LENGTH_SHORT).show();
        }

    }
*/

/*
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(mLayout, "GPS access is required to get the current location", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SelectionScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                        }
                    }).show();
        } else {
            Snackbar.make(mLayout, "Permission not available, Requesting location permission", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(SelectionScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        }
    }

*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.locationActivity:
                Intent intent = new Intent(this, AutoComplete_Main.class);
                startActivity(intent);
                break;
            case R.id.emergencyActivity:
                break;
            case R.id.shareActivity:
                break;
            case R.id.recordActivity:
                break;
            default:
                break;

        }

    }
/*

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Location permission was granted..",
                        Snackbar.LENGTH_SHORT)
                        .show();
                getCurrentLocation();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Location permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
*/

}