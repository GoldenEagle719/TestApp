package com.android.anton.testapp.SubActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.SFSharedPreference;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestInfoLocationActivity extends AppCompatActivity {
    private static final String TAG = "RestInfoLocation";

    MapView mapView;
    GoogleMap map;

    private double  lat;
    private double  lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_info_location);

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        mapView = (MapView)findViewById(R.id.activity_rest_info_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                BitmapDescriptor icon_marker = BitmapDescriptorFactory.fromResource(R.mipmap.marker);

                map = googleMap;
                map.addMarker( new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .icon(icon_marker)
                );

            }
        });
    }

    public void onBack(View v) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



}
