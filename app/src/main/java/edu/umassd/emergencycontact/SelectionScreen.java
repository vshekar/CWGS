package edu.umassd.emergencycontact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import edu.umassd.emergencycontact.classes.Locations;


/**
 * Created by Jayesh on 11/26/16.
 */


public class SelectionScreen extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Button locationAc, emergency, share, record;
    View mLayout;
    static final int PERMISSION_LOCATION = 0;
    public Location location;
    boolean logging = new MainActivity().logging;
    private GoogleApiClient mGoogleApiClient;


    //first method that gets called when this class is called
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionscreen);

        //init buttons from selectionScreen.xml in res folder
        locationAc = (Button) findViewById(R.id.locationActivity);
        emergency = (Button) findViewById(R.id.emergencyActivity);
        share = (Button) findViewById(R.id.shareActivity);
        record = (Button) findViewById(R.id.recordActivity);

        mLayout = findViewById(R.id.mLayout);//displays details about permissions

        locationAc.setOnClickListener(this);
        emergency.setOnClickListener(this);
        share.setOnClickListener(this);
        record.setOnClickListener(this);

        //init google services to get accurate location
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

    }

    //on click listener for the selection screen
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

    //location helper class
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    //location helper class
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



    //location permission helper class
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

    //when google services is connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Locations locationClass = new Locations();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            //crashes here in emulator but works on a device since location.getlatitude returns null
            locationClass.currentLocation = new LatLng(location.getLatitude(),location.getLongitude());


            if(logging) { Log.e("Location",""+location.toString());}
        }else {
            if(logging) { Log.e("Location","no permission");}
            requestLocationPermission();
        }
        if(logging) { Log.e("Location","locationclass curr location "+locationClass.currentLocation);}

    }

    private void handleNewLocation(Location location) {
        if(logging) { Log.e("Location",""+location.toString());}

    }

    @Override
    public void onConnectionSuspended(int i) {
        if(logging) { Log.e("onConnection Suspended","connecion suspended");}
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(logging) { Log.e("conn failed","Failed");}
    }


    //request location access override method
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Location permission was granted..",
                        Snackbar.LENGTH_SHORT)
                        .show();
                //if permission was already granted this block will get executed
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Location permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }


}