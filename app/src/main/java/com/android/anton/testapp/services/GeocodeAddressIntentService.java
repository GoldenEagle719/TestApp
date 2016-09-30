package com.android.anton.testapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GeocodeAddressIntentService extends IntentService {

    private static int SUCCESS = 200;

    protected ResultReceiver resultReceiver;
    private static final String TAG = "FetchAddyIntentService";

    public GeocodeAddressIntentService() {
        super("GeocodeAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        resultReceiver = intent.getParcelableExtra("receiver");
        double lat = intent.getDoubleExtra("lat", 0);
        double lng = intent.getDoubleExtra("lng", 0);

        Log.d("Debug", "1");

        try {
            addresses = geocoder.getFromLocation(
                    lat, lng, 1);
        } catch (IOException ioException) {
            Log.d("Debug", "ioException");
            ioException.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d("Debug", "IllegalArgmentException");
            illegalArgumentException.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {

            Log.d("Debug", "2");
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName();

            deliverResultToReceiver(SUCCESS, address + ", " + city + ", " + country + ", " + postalCode);
        }
    }

    private void deliverResultToReceiver(int resultCode, String address) {
        Log.d("Debug", "3");
        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        resultReceiver.send(resultCode, bundle);
    }

}
