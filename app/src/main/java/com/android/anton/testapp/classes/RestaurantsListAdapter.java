package com.android.anton.testapp.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.anton.testapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 8/17/2016.
 */
public class RestaurantsListAdapter extends ArrayAdapter<String>{
    private static final String TAG = "NCListAdapter";

    private Activity mContext;
    private ArrayList<Restaurant> restaurantArrayList;

    //Here End
    public RestaurantsListAdapter (Activity c, String[] nameAry, ArrayList<Restaurant> restaurantArrayList) {
        super(c, R.layout.item_list, nameAry);
        mContext    = c;
        this.restaurantArrayList = restaurantArrayList;

    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.restaurants_list_item, null, true);

        TextView restaurants_list_item_textview_name = (TextView) rowView.findViewById(R.id.restaurants_list_item_textview_name);
        TextView restaurants_list_item_textview_distance = (TextView) rowView.findViewById(R.id.restaurants_list_item_textview_distance);
        RatingBar restaurants_list_item_ratingbar = (RatingBar) rowView.findViewById(R.id.restaurants_list_item_ratingbar);
        ImageView restaurants_list_item_imageview = (ImageView) rowView.findViewById(R.id.restaurants_list_item_imageview);

        restaurants_list_item_textview_name.setText(restaurantArrayList.get(position).getName());
        restaurants_list_item_textview_distance.setText(String.valueOf(restaurantArrayList.get(position).getDistance()));
        restaurants_list_item_ratingbar.setRating(restaurantArrayList.get(position).getRating());
        Picasso.with(mContext)
                .load(restaurantArrayList.get(position).getImgURL())
                .into(restaurants_list_item_imageview);

        return rowView;
    }


}
