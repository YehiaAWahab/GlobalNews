package com.example.globalnews;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    public static final int REQUEST_ACCESS_FINE_LOCATION = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        printPlaceAndLikelihood();
    }


    private void printPlaceAndLikelihood() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        } else {
            PlaceDetectionClient placeDetectionClient = Places.getPlaceDetectionClient(this);
            Task<PlaceLikelihoodBufferResponse> placeResult  = placeDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    int FIRST_INDEX = 0;
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    PlaceLikelihood likelihood = likelyPlaces.get(FIRST_INDEX);
                    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                    try {
                        // I got help from this answer: https://stackoverflow.com/a/2296416
                        double latitude = likelihood.getPlace().getLatLng().latitude;
                        double longitude = likelihood.getPlace().getLatLng().longitude;
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        Address address = addressList.get(FIRST_INDEX);
                        Log.i(TAG, "Country Name is: " + address.getCountryName());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    printPlaceAndLikelihood();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.i(TAG, String.format("Permission not Granted"));
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}