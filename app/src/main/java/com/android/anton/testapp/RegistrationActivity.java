package com.android.anton.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.anton.testapp.classes.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends Activity {

    private EditText registration_edit_name;
    private EditText registration_edit_email;
    private EditText registration_edit_address1;
    private EditText registration_edit_address2;
    private EditText registration_edit_town;
    private EditText registration_edit_postcode;
    private EditText registration_edit_telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        registration_edit_name = (EditText)findViewById(R.id.registration_edit_name);
        registration_edit_email = (EditText)findViewById(R.id.registration_edit_email);
        registration_edit_address1 = (EditText)findViewById(R.id.registration_edit_address1);
        registration_edit_address2 = (EditText)findViewById(R.id.registration_edit_address2);
        registration_edit_town = (EditText)findViewById(R.id.registration_edit_town);
        registration_edit_postcode = (EditText)findViewById(R.id.registration_edit_postcode);
        registration_edit_telephone = (EditText)findViewById(R.id.registration_edit_telephone);
    }

    public void registration_btn_skipClicked(View v) {
        finish();
    }

    public void registration_btn_saveClicked(View v) {
        String name = registration_edit_name.getText().toString();
        String email = registration_edit_email.getText().toString();
        String address1 = registration_edit_address1.getText().toString();
        String address2 = registration_edit_address2.getText().toString();
        String town = registration_edit_town.getText().toString();
        String postcode = registration_edit_postcode.getText().toString();
        String telephone = registration_edit_telephone.getText().toString();

        boolean error_flag = false;
        if (name.equals("")) {
            registration_edit_name.setError("enter your name");
            error_flag = true;
        }
        if (email.equals("")) {
            registration_edit_email.setError("enter valid email");
            error_flag = true;
        }
        if (telephone.equals("")) {
            registration_edit_telephone.setError("enter valid phonenumber");
            error_flag = true;
        }
        if (!isValidEmail(email)) {
            registration_edit_email.setError("enter valid email");
            error_flag = true;
        }
        if (!isValidMobile(telephone)) {
            registration_edit_telephone.setError("enter valid phonenumber");
            error_flag = true;
        }

        if (error_flag)
            return;

        UserInfo userInfo = new UserInfo(this);
        userInfo.setName(name);
        userInfo.setEmail(email);
        userInfo.setTelephone(telephone);
        userInfo.setAddress1(address1);
        userInfo.setAddress2(address2);
        userInfo.setTown(town);
        userInfo.setPostcode(postcode);

        Toast toast = Toast.makeText(this, "Saved successfully", Toast.LENGTH_LONG);
        toast.show();

        finish();

    }

    public void registration_btn_termsClicked(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Terms")
                .setMessage("Terms apply")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}
