package com.android.anton.testapp.SubActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.Manifest;
import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.Restaurant;
import com.android.anton.testapp.services.GeocodeAddressIntentService;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Locale;

public class RestaurantInfoActivity extends AppCompatActivity {
    private static final String TAG = "RestaurantInfo";

    private Intent callIntent;

    public double lat;
    public double lng;
    public String phone;
    public String website;
    public String facebook;
    public String twitter;
    public String email;

    private String appId;
    private AddressResultReceiver mResultReceiver;

    private TextView[] lbl_openHours;
    private TextView lbl_location;
    private TextView lbl_phone;
    private TextView lbl_website;
    private TextView lbl_facebook;
    private TextView lbl_twitter;
    private TextView lbl_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        appId = getIntent().getStringExtra("appId");

        lbl_openHours = new TextView[7];
        lbl_openHours[0] = (TextView) findViewById(R.id.activity_restaurant_info_label_openHours1);
        lbl_openHours[1] = (TextView) findViewById(R.id.activity_restaurant_info_label_openHours2);
        lbl_openHours[2] = (TextView) findViewById(R.id.activity_restaurant_info_label_openHours3);
        lbl_openHours[3] = (TextView) findViewById(R.id.activity_restaurant_info_label_openHours4);
        lbl_openHours[4] = (TextView) findViewById(R.id.activity_restaurant_info_label_openHours5);
        lbl_openHours[5] = (TextView) findViewById(R.id.activity_restaurant_info_label_openHours6);
        lbl_openHours[6] = (TextView) findViewById(R.id.activity_restaurant_info_label_openHours7);

        lbl_location = (TextView) findViewById(R.id.activity_restaurant_info_label_location);
        lbl_phone = (TextView) findViewById(R.id.activity_restaurant_info_label_phone);
        lbl_website = (TextView) findViewById(R.id.activity_restaurant_info_label_web);
        lbl_facebook = (TextView) findViewById(R.id.activity_restaurant_info_label_facebook);
        lbl_twitter = (TextView) findViewById(R.id.activity_restaurant_info_label_twitter);
        lbl_email = (TextView) findViewById(R.id.activity_restaurant_info_label_email);

        mResultReceiver = new AddressResultReceiver(null);

        getData();
    }

    public void getData() {
        String appid = appId;
        OkHttpClient client = new OkHttpClient();

        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appid", appid);

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(String.format("https://api.appmc2.net/appjson.aspx?appid=%s&version=5&section=%s", appid,"config"))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String res = response.body().string();
                    Log.d(TAG, res);
                    try {
                        JSONObject resJSONObject = new JSONObject(res);
                        parseData(resJSONObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void parseData(final JSONObject jsonObject) {
        Log.d("Debug", "-2");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Debug", "-1");
                    lat = Double.parseDouble(jsonObject.getString("lat"));
                    lng = Double.parseDouble(jsonObject.getString("long"));
                    phone = jsonObject.getString("telephone");
                    facebook = jsonObject.getString("facebook");
                    twitter = jsonObject.getString("twitter");
                    website = jsonObject.getString("www");
                    email = jsonObject.getString("email");

                    JSONObject hours = new JSONObject(jsonObject.getString("hours"));

                    for (int i = 0; i < 7; i++) {
                        JSONArray jsonArray = hours.getJSONArray(String.valueOf(i));
                        Log.d(TAG, jsonArray.toString());
                        JSONArray hour = jsonArray.getJSONArray(0);

                        String from = hour.toString().split(",")[0].substring(2, 7);
                        String to = hour.toString().split(",")[1].substring(1, 6);

                        lbl_openHours[i].setText(from + " - " + to);
                    }

                    lbl_phone.setText(phone);
                    lbl_website.setText(website);
                    lbl_facebook.setText(facebook);
                    lbl_twitter.setText(twitter);
                    lbl_email.setText(email);

                    /*start GeocodeAddressIntentService*/
                    Log.d("Debug", "0");
                    Intent intent = new Intent(RestaurantInfoActivity.this, GeocodeAddressIntentService.class);
                    intent.putExtra("receiver", mResultReceiver);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    startService(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onLocation(View v) {
        Intent intent = new Intent(this, RestInfoLocationActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);
    }

    public void onPhone(View v) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + lbl_phone.getText().toString()));
            startActivity(callIntent);
        }
    }

    public void onWeb(View v) {
        Intent intent = new Intent(this, RestInfoWebActivity.class);
        intent.putExtra("website", website);
        startActivity(intent);
    }

    public void onFacebook(View v) {
        Intent intent = new Intent(this, RestInfoFacebookActivity.class);
        intent.putExtra("facebook", facebook);
        startActivity(intent);
    }

    public void onTwitter(View v) {
        Intent intent = new Intent(this, RestInfoTwitterActivity.class);
        intent.putExtra("twitter", twitter);
        startActivity(intent);
    }

    public void onEmail(View v) {
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", lbl_email.getText().toString(), null));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"");

        try {
            startActivity(Intent.createChooser(emailIntent,
                    "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "No email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onBack(View v) {
        finish();
    }

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == 200) {
                Log.d("Debug", "4");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String address= resultData.getString("address");
                        lbl_location.setText(address);
                    }
                });
            }
        }
    }

}
