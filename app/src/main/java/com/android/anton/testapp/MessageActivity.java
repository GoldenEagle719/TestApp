package com.android.anton.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
    }

    public void btn_back_messageClicked(View v) {
        finish();
    }
}
