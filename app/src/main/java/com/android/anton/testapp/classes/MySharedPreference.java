package com.android.anton.testapp.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.Double2;

/**
 * Created by Administrator on 8/29/2016.
 */
public class MySharedPreference {
    private Activity mContext;

    public MySharedPreference(Activity context) {
        mContext = context;
    }

    public static MySharedPreference newInstance(Activity context) {
        return new MySharedPreference(context);
    }

    public LocationForCity getSelectedLocation() {
        SharedPreferences pref = mContext.getSharedPreferences("SelectedLocation", Context.MODE_PRIVATE);
        String name = pref.getString("Name", "");
        String lat = pref.getString("Lat", "");
        String lng = pref.getString("Long", "");

        if (name.equals("") || lat.equals("") || lng.equals(""))
            return  null;

        LocationForCity selectedLocation = new LocationForCity(name, Double.parseDouble(lat), Double.parseDouble(lng));
        return  selectedLocation;
    }

}
