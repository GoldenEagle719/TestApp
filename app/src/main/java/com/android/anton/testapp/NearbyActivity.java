package com.android.anton.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NearbyActivity extends Activity implements GoogleMap.OnMarkerClickListener {
    private static final String TAG = "NearbyActivity";

    MapView mapView;
    GoogleMap map;
    ProgressDialog progressDialog;
    ArrayList<MarkerOptions> markerOptionses;
    ArrayList<String> appidList;
    ArrayList<Bitmap> bitmapList;
    ArrayList<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_activity);

        mapView = (MapView)findViewById(R.id.nearby_mapview);
        mapView.onCreate(savedInstanceState);

    }

    public void showLocations() {
        String actionName = "loadmapvenuedata";
        String instanceid = "E658D06413D94F1EAF243D171BA4A26E";
        String category = "all";
        String lat = String.valueOf(getSelectedLatLng().latitude);
        String lng = String.valueOf(getSelectedLatLng().longitude);
        String appid = "a72cb1dd-7767-41ca-a3c1-f07af763f469";

        OkHttpClient client = new OkHttpClient();

        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", actionName);
            jsonObject.put("instanceid", instanceid);
            jsonObject.put("category", category);
            jsonObject.put("lat", lat);
            jsonObject.put("long", lng);
            jsonObject.put("appid", appid);

            Log.d(TAG, jsonObject.toString());

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("https://api.appmc2.net/postdata.aspx")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG, "RequestFailure");

                    progressDialog.dismiss();
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        JSONObject resJSONObject = new JSONObject(res);
                        JSONArray resultlist = resJSONObject.getJSONArray("resultlist");

                        Log.d(TAG, resJSONObject.toString());

                        appidList = new ArrayList<String>();
                        bitmapList = new ArrayList<Bitmap>(resultlist.length());
                        markerList = new ArrayList<Marker>(resultlist.length());
                        markerOptionses = new ArrayList<MarkerOptions>();
                        for (int i = 0; i < resultlist.length(); i++) {
                            JSONObject locationJSONObject = resultlist.getJSONObject(i);

                            double lat = Double.parseDouble(locationJSONObject.getString("lat"));
                            double lng = Double.parseDouble(locationJSONObject.getString("lng"));
                            String venuename = locationJSONObject.getString("venuename");
                            String appid = locationJSONObject.getString("appid");
                            double rating = locationJSONObject.getDouble("rating");

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(venuename)
                                    .visible(true);

                            markerOptionses.add(markerOptions);
                            appidList.add(appid);

                            new LoadImage().execute("https://sappsuma.blob.core.windows.net/iconthumbs/ico_" + appid + "_144.png",
                                    String.valueOf(i));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "ExceptionOccured");
                    }

                }
            });
        } catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "ExceptionOccured");
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            String url = args[0];

            Log.d(TAG + "URL", url);

            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "BitmapException");
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if (image != null) {

                MarkerOptions tempMarkerOption = markerOptionses.get(bitmapList.size());

                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);
                tempMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(image)));

                Marker marker = map.addMarker(markerOptionses.get(bitmapList.size()));
                marker.setVisible(true);

                markerList.add(marker);

                bitmapList.add(image);
            }

            if (bitmapList.size() == markerOptionses.size())
                progressDialog.dismiss();
        }
    }

    private Bitmap getMarkerBitmapFromView(Bitmap bitmap) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker_image);
        markerImageView.setImageBitmap(bitmap);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromDetailView(Bitmap bitmap, String title) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker_detail, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker_detail_image);
        markerImageView.setImageBitmap(bitmap);
        TextView detailTextView = (TextView)customMarkerView.findViewById(R.id.marker_detail_text);
        detailTextView.setText(title);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        int position = -1;
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(bitmapList.get(i))));
            if (markerList.get(i).getPosition().latitude == marker.getPosition().latitude
                    && markerList.get(i).getPosition().longitude == marker.getPosition().longitude) {
                position = i;
            }
        }

        if (position == -1) {
            return false;
        }

        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromDetailView(bitmapList.get(position), marker.getTitle())));

        return true;
    }

    public void checkLocationSelected() {
        SharedPreferences pref = getSharedPreferences("SelectedLocation", MODE_PRIVATE);
        String latValueString = pref.getString("Lat", "");
        String longValueString = pref.getString("Long", "");

        if (latValueString.equals("") || longValueString.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Please choose a location first!")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.gotoTab(0);
                        }
                    }).create().show();
        } else {
            progressDialog = ProgressDialog.show(this, "", "Loading...", true);

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;

                    map.clear();
                    map.setOnMarkerClickListener(NearbyActivity.this);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(getSelectedLatLng(), 10));

                    showLocations();

                }
            });

            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public int calculateZoomLevel() {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;

        double equatorLength = 40075004; // in meters
        double widthInPixels = screenWidth;
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * widthInPixels) > 45000) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }

        return zoomLevel;
    }

    public LatLng getSelectedLatLng() {
        SharedPreferences pref = getSharedPreferences("SelectedLocation", MODE_PRIVATE);
        String latValueString = pref.getString("Lat", "");
        String longValueString = pref.getString("Long", "");

        double latValue = (!latValueString.equals(""))?Double.parseDouble(latValueString):0;
        double longValue = (!longValueString.equals(""))?Double.parseDouble(longValueString):0;

        return new LatLng(latValue, longValue);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationSelected();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
