package com.android.anton.testapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.classes.CustomAdapter;
import com.android.anton.testapp.classes.GPSTracker;
import com.android.anton.testapp.fragments.FeedbackFragment;


public class HomeActivity extends Activity {

    private ListView    listView;
    private FrameLayout fragment_container;

    private TextView    txtView_latLong;
    private int         SEARCH_REQUEST = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        /* ListView------------------------------------------------------------------------------------*/
        final String[] itemTextAry = new String[] {"asian", "banting", "breakfast", "burgers", "child friendly", "cookery classes",
            "coffee", "desert", "drinks", "indian", "italian", "luxury dining", "meat lover", "mexican", "nightlife", "pet friendly",
            "recipes", "seafood", "streetfood", "sushi", "vegetarian", "vegan"};

        int[] itemImgIDAry= {R.drawable.asian, R.drawable.banting, R.drawable.breakfast, R.drawable.burgers,
                             R.drawable.childfriendly, R.drawable.cookeryclasses, R.drawable.coffee, R.drawable.desert,
                             R.drawable.drinks, R.drawable.indian, R.drawable.italian, R.drawable.luxurydining,
                             R.drawable.meatlover, R.drawable.mexican, R.drawable.nightlife, R.drawable.petfriendly,
                             R.drawable.recipes, R.drawable.seafood, R.drawable.streetfood, R.drawable.sushi,
                             R.drawable.vegetarian, R.drawable.vegan};

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new CustomAdapter(this, itemTextAry, itemImgIDAry));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* ItemClickListener */
//                Toast toast = Toast.makeText(HomeActivity.this, itemTextAry[position], Toast.LENGTH_LONG);
//                toast.show();
                FeedbackFragment fragment = new FeedbackFragment();
                Bundle bundle = new Bundle();
                bundle.putString("param1", itemTextAry[position]);
                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.home_fragment_container,fragment).commit();

                fragment_container.setVisibility(View.VISIBLE);

            }
        });

        /* TextView------------------------------------------------------------------------------------*/
        txtView_latLong = (TextView)findViewById(R.id.txtView_latLong);
        String selectedLocationName = getSharedPreferences("SelectedLocation", MODE_PRIVATE).getString("Name", "");
        txtView_latLong.setText(selectedLocationName);
        /* FragmentContainer---------------------------------------------------------------------------*/
        fragment_container = (FrameLayout)findViewById(R.id.home_fragment_container);
        fragment_container.setVisibility(View.GONE);
    }

    public String getLocationInfo() {
        String ret = "";

        GPSTracker gps = new GPSTracker(this);
        String userLatString = String.format("%.4f", gps.getLatitude());
        String userLongString = String.format("%.4f", gps.getLongitude());

        ret = "Lat:" + userLatString + ", Long:" + userLongString;

        return ret;
    }

    public void btn_pinClicked(View v) {
        txtView_latLong.setText(getLocationInfo());

        SharedPreferences.Editor editor = getSharedPreferences("SelectedLocation", MODE_PRIVATE).edit();
        editor.putString("Name", "Near me");
        editor.putString("Lat", String.valueOf(new GPSTracker(this).getLatitude()));
        editor.putString("Long", String.valueOf(new GPSTracker(this).getLongitude()));
        editor.commit();
    }

    public void btn_searchClicked(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_REQUEST);
    }

    public void imgView_messageClicked(View v) {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SEARCH_REQUEST) {
            SharedPreferences pref = getSharedPreferences("SelectedLocation", MODE_PRIVATE);
            String name = pref.getString("Name", "");
            txtView_latLong.setText(name);
        }
    }

}