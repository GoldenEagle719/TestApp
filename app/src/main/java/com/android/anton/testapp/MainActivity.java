package com.android.anton.testapp;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.anton.testapp.classes.UserInfo;


public class MainActivity extends TabActivity {
    private static final String TAG = "MainActivity";
    public static TabHost tabHost;
    /** Called when the activity is first created. */


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserInfo userInfo = new UserInfo(this);
        String name = userInfo.getName();
        String email = userInfo.getEmail();
        String telephone = userInfo.getTelephone();

        if (name.equals("") || email.equals("") || telephone.equals("")) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.main);
        tabHost = getTabHost();
        setTabs();
    }

    private void setTabs()
    {
        addTab("Home", R.drawable.tab_home, HomeActivity.class);
        addTab("Nearby", R.drawable.tab_search, NearbyActivity.class);
        addTab("Profile", R.drawable.tab_profile, ProfileActivity.class);
        addTab("Prizes", R.drawable.tab_prizes, PrizesActivity.class);
        addTab("Share", R.drawable.tab_share, ShareActivity.class);
    }

    private void addTab(String labelId, int drawableId, Class<?> c)
    {
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    public static void gotoTab(int i) {
        if (tabHost == null)
            return;
        tabHost.setCurrentTab(i);
    }

}