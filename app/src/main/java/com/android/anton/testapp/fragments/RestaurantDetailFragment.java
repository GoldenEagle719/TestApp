package com.android.anton.testapp.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.MainActivity;
import com.android.anton.testapp.R;
import com.android.anton.testapp.RegistrationActivity;
import com.android.anton.testapp.classes.NotificationConfigListAdapter;
import com.android.anton.testapp.classes.Restaurant;
import com.android.anton.testapp.classes.RestaurantsListAdapter;
import com.android.anton.testapp.classes.UserInfo;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantDetailFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "RestaurantDetailF";

    private OnFragmentInteractionListener mListener;
    private ViewGroup   mContainer;

    public ArrayList<Restaurant> restaurantArrayList;
    public RestaurantsListAdapter adapter;

    private String category;

    public RestaurantDetailFragment() {

    }

    public static RestaurantDetailFragment newInstance() {
        RestaurantDetailFragment fragment = new RestaurantDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainer = container;
        String name = getArguments().getString("name");
        String distance = getArguments().getString("distance");
        String imageurl = getArguments().getString("imageurl");
        float rating = getArguments().getFloat("rating");
        category = getArguments().getString("category");

        View view = inflater.inflate(R.layout.fragment_restaurant_detail, null, true);
        view.setOnClickListener(this);

        /* Button---------------------------------------------------------------------------------------*/
        TextView fragment_restaurantdetail_btn_back = (TextView) view.findViewById(R.id.fragment_restaurantdetail_btn_back);
        fragment_restaurantdetail_btn_back.setOnClickListener(this);

        ImageView fragment_restaurant_detail_btn_viewmenu = (ImageView) view.findViewById(R.id.fragment_restaurant_detail_btn_viewmenu);
        ImageView fragment_restaurant_detail_btn_showspecials = (ImageView) view.findViewById(R.id.fragment_restaurant_detail_btn_showspecials);
        ImageView fragment_restaurant_detail_btn_restaurantinfo = (ImageView) view.findViewById(R.id.fragment_restaurant_detail_btn_restaurantinfo);
        ImageView fragment_restaurant_detail_raterestaurant = (ImageView) view.findViewById(R.id.fragment_restaurant_detail_raterestaurant);
        ImageView fragment_restaurant_detail_booktable = (ImageView) view.findViewById(R.id.fragment_restaurant_detail_booktable);

        /* Restaurant-------------------------------------------------------------------------*/
        ImageView fragment_restaurant_detail_imageview_logo = (ImageView) view.findViewById(R.id.fragment_restaurant_detail_imageview_logo);
        TextView fragment_restaurant_detail_textview_name = (TextView) view.findViewById(R.id.fragment_restaurant_detail_textview_name);
        TextView fragment_restaurant_detail_textview_distance = (TextView) view.findViewById(R.id.fragment_restaurant_detail_textview_distance);
        RatingBar fragment_restaurant_detail_ratingbar = (RatingBar)view.findViewById(R.id.fragment_restaurant_detail_ratingbar);

        Picasso.with(getActivity())
                .load(imageurl)
                .into(fragment_restaurant_detail_imageview_logo);
        fragment_restaurant_detail_textview_name.setText(name);
        fragment_restaurant_detail_textview_distance.setText(distance);
        fragment_restaurant_detail_ratingbar.setRating(rating);
        /*------------------------------------------------------------------------------------*/

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_restaurantdetail_btn_back) {
            RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", category); // Send Param to Fragment
            restaurantsFragment.setArguments(bundle);

            this.getFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment_container, restaurantsFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
