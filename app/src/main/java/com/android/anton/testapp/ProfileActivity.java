package com.android.anton.testapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.anton.testapp.fragments.NotificationsFragment;
import com.android.anton.testapp.fragments.RegistrationFragment;

public class ProfileActivity extends Activity {

    private static final String TAG = "ProfileActivity";

    private LinearLayout profile_component_container;
    private FrameLayout fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        fragment_container = (FrameLayout)findViewById(R.id.profile_fragment_container);
        fragment_container.setVisibility(View.GONE);
    }

    public void profile_btn_registrationClicked(View v) {
        RegistrationFragment fragment = new RegistrationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.profile_fragment_container, fragment).commit();

        fragment_container.setVisibility(View.VISIBLE);
    }

    public void profile_btn_notificationClicked(View v) {
        NotificationsFragment fragment = new NotificationsFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.profile_fragment_container, fragment).commit();

        fragment_container.setVisibility(View.VISIBLE);
    }
}
