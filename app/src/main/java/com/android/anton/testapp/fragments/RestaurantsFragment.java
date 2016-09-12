package com.android.anton.testapp.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.HomeActivity;
import com.android.anton.testapp.MainActivity;
import com.android.anton.testapp.R;
import com.android.anton.testapp.RegistrationActivity;
import com.android.anton.testapp.SubActivities.RestaurantDetailActivity;
import com.android.anton.testapp.classes.MySharedPreference;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantsFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "RestaurantsF";

    private OnFragmentInteractionListener mListener;
    private ViewGroup   mContainer;
    private String category;

    public ArrayList<Restaurant> restaurantArrayList;
    public RestaurantsListAdapter adapter;
    public String[] nameAry;
    public ListView fragment_restaurants_listview;
    public ProgressDialog progressDialog;

    public RestaurantsFragment() {

    }

    public static RestaurantsFragment newInstance() {
        RestaurantsFragment fragment = new RestaurantsFragment();
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
        category = getArguments().getString("category");

        View view = inflater.inflate(R.layout.fragment_restaurants, null, true);
        view.setOnClickListener(this);

        /* ListView-------------------------------------------------------------------------------------*/
        fragment_restaurants_listview = (ListView) view.findViewById(R.id.fragments_restaurants_listview);
        restaurantArrayList = new ArrayList<Restaurant>();
        nameAry = new String[0];
        adapter = new RestaurantsListAdapter(getActivity(), nameAry, restaurantArrayList);
        fragment_restaurants_listview.setAdapter(adapter);
        fragment_restaurants_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragment_restaurants_listview_itemClicked(position);
            }
        });
        /* Button---------------------------------------------------------------------------------------*/
        TextView fragment_restaurants_btn_back = (TextView) view.findViewById(R.id.fragment_restaurants_btn_back);
        fragment_restaurants_btn_back.setOnClickListener(this);
        /*ProgressDialog-------------------------------------------------------------------------------------*/
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        /*--------------------------------------------------------------------------------------------------*/
        searchVenues();

        return view;
    }

    public void fragment_restaurants_listview_itemClicked(int position) {
//        RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("name", restaurantArrayList.get(position).getName());
//        bundle.putString("distance", restaurantArrayList.get(position).getDistance());
//        bundle.putString("imageurl", restaurantArrayList.get(position).getImgURL());
//        bundle.putFloat("rating", restaurantArrayList.get(position).getRating());
//        bundle.putString("category", category);
//
//        restaurantDetailFragment.setArguments(bundle);
//
//        this.getFragmentManager().beginTransaction()
//                .replace(R.id.home_fragment_container, restaurantDetailFragment, null)
//                .addToBackStack(null)
//                .commit();
        Intent intent = new Intent(this.getActivity(), RestaurantDetailActivity.class);
        intent.putExtra("name", restaurantArrayList.get(position).getName());
        intent.putExtra("distance", restaurantArrayList.get(position).getDistance());
        intent.putExtra("imageurl", restaurantArrayList.get(position).getImgURL());
        intent.putExtra("rating", restaurantArrayList.get(position).getRating());
        intent.putExtra("category", category);
        getActivity().startActivity(intent);

    }

    public void searchVenues() {
        String action = "searchvenues";
        String instanceid = "E658D06413D94F1EAF243D171BA4A26E";
        double lat = MySharedPreference.newInstance(getActivity()).getSelectedLocation().getLat();
        double lng = MySharedPreference.newInstance(getActivity()).getSelectedLocation().getLng();
        String appid = "a72cb1dd-7767-41ca-a3c1-f07af763f469";

        OkHttpClient client = new OkHttpClient();

        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", action);
            jsonObject.put("instanceid", instanceid);
            jsonObject.put("category", category);
            jsonObject.put("lat", lat);
            jsonObject.put("long", lng);
            jsonObject.put("appid", appid);

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("https://api.appmc2.net/postdata.aspx")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG + "Failure", request.toString());
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String res = response.body().string();
                    Log.d(TAG, res);

                    try {
                        JSONObject resJSONObject = new JSONObject(res);
                        JSONArray resultlist = resJSONObject.getJSONArray("resultlist");

                        restaurantArrayList.clear();
                        nameAry = new String[resultlist.length()];

                        for(int i = 0; i < resultlist.length(); i++) {
                            JSONObject tempObject = resultlist.getJSONObject(i);
                            String appid = tempObject.getString("appid");
                            String name = tempObject.getString("venuename");
                            String distance = tempObject.getString("distance");
                            String imageurl = tempObject.getString("imageurl");
                            float rating = (float)tempObject.getDouble("rating");

                            nameAry[i] = name;

                            Restaurant restaurant = new Restaurant(name, distance, rating, imageurl);
                            restaurantArrayList.add(restaurant);
                        }

                        refreshListView();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "ExceptionOccur");
                    }

                }
            });
        } catch(Exception e) {

        }

    }

    public void refreshListView() {
        getActivity().runOnUiThread(new RefreshListView());
    }

    private class RefreshListView implements Runnable
    {
        public RefreshListView()
        {
        }

        @Override
        public void run()
        {
            adapter = new RestaurantsListAdapter(getActivity(), nameAry, restaurantArrayList);
            fragment_restaurants_listview.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_restaurants_btn_back) {
            mContainer.setVisibility(View.GONE);
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
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
