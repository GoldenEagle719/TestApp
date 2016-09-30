package com.android.anton.testapp.SubActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.Restaurant;
import com.squareup.picasso.Picasso;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantDetail";
    private String category;
    private String appId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        appId = getIntent().getStringExtra("appId");

        String name = getIntent().getStringExtra("name");
        String distance = getIntent().getStringExtra("distance");
        String imageurl = getIntent().getStringExtra("imageurl");
        float rating = getIntent().getFloatExtra("rating", 0);
        category = getIntent().getStringExtra("category");

        /* Button---------------------------------------------------------------------------------------*/
        TextView activity_restaurantdetail_btn_back = (TextView) findViewById(R.id.activity_restaurantdetail_btn_back);

        ImageView activity_restaurant_detail_btn_viewmenu = (ImageView) findViewById(R.id.activity_restaurant_detail_btn_viewmenu);
        ImageView activity_restaurant_detail_btn_showspecials = (ImageView) findViewById(R.id.activity_restaurant_detail_btn_showspecials);
        ImageView activity_restaurant_detail_btn_restaurantinfo = (ImageView) findViewById(R.id.activity_restaurant_detail_btn_restaurantinfo);
        ImageView activity_restaurant_detail_raterestaurant = (ImageView) findViewById(R.id.activity_restaurant_detail_raterestaurant);
        ImageView activity_restaurant_detail_booktable = (ImageView) findViewById(R.id.activity_restaurant_detail_booktable);

        /* Restaurant-------------------------------------------------------------------------*/
        ImageView activity_restaurant_detail_imageview_logo = (ImageView) findViewById(R.id.activity_restaurant_detail_imageview_logo);
        TextView activity_restaurant_detail_textview_name = (TextView) findViewById(R.id.activity_restaurant_detail_textview_name);
        TextView activity_restaurant_detail_textview_distance = (TextView) findViewById(R.id.activity_restaurant_detail_textview_distance);
        RatingBar activity_restaurant_detail_ratingbar = (RatingBar) findViewById(R.id.activity_restaurant_detail_ratingbar);

        Picasso.with(this)
                .load(imageurl)
                .into(activity_restaurant_detail_imageview_logo);
        activity_restaurant_detail_textview_name.setText(name);
        activity_restaurant_detail_textview_distance.setText(distance);
        activity_restaurant_detail_ratingbar.setRating(rating);
        /*------------------------------------------------------------------------------------*/
    }

    public void onBack(View v) {
        finish();
    }

    public void onViewMenu(View v) {
        Intent intent = new Intent(this, RestMenuActivity.class);
        intent.putExtra("appId", appId);
        startActivity(intent);
    }

    public void onInfo(View v) {
        Intent intent = new Intent(this, RestaurantInfoActivity.class);
        intent.putExtra("appId", appId);
        startActivity(intent);
    }

    public void onBookTable(View v) {
        Intent intent = new Intent(this, RestaurantRequestActivity.class);
        intent.putExtra("appId", appId);
        startActivity(intent);
    }

    public void onRate(View v) {
        Intent intent = new Intent(this, RestaurantRateActivity.class);
        intent.putExtra("appId", appId);
        startActivity(intent);
    }

    public void onSpecials(View v) {
        Intent intent = new Intent(this, RestaurantSpecialsActivity.class);
        intent.putExtra("appId", appId);
        startActivity(intent);
    }

}
