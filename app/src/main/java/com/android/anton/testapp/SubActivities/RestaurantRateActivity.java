package com.android.anton.testapp.SubActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.UserInfo;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantRateActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "RestaurantRate";

    private EditText    txt_name;
    private EditText    txt_email;
    private EditText    txt_phone;
    private EditText    txt_comments;
    private RatingBar   ratingBar;

    private String appId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_rate);

        appId = getIntent().getStringExtra("appId");

        /*Buttons----------------------------------------------------------------------*/
        TextView btn_back = (TextView)findViewById(R.id.activity_restaurant_rate_btn_back);
        btn_back.setOnClickListener(this);
        TextView btn_submit = (TextView)findViewById(R.id.activity_restaurant_rate_btn_submit);
        btn_submit.setOnClickListener(this);
        /*EditTexts-------------------------------------------------------------------------*/
        txt_name = (EditText) findViewById(R.id.activity_restaurant_rate_txt_name);
        txt_email = (EditText) findViewById(R.id.activity_restaurant_rate_txt_email);
        txt_phone = (EditText) findViewById(R.id.activity_restaurant_rate_txt_phone);
        txt_comments = (EditText) findViewById(R.id.activity_restaurant_rate_txt_comments);
        ratingBar = (RatingBar)findViewById(R.id.activity_restaurant_rate_ratingBar);

        UserInfo userInfo = new UserInfo(this);

        txt_name.setText(userInfo.getName());
        txt_email.setText(userInfo.getEmail());
        txt_phone.setText(userInfo.getTelephone());
    }

    public void onClick(View v) {
        hideKeyboard();

        if (v.getId() == R.id.activity_restaurant_rate_btn_back) {
            finish();
        }

        if (v.getId() == R.id.activity_restaurant_rate_btn_submit) {
            String name = txt_name.getText().toString();
            String email = txt_email.getText().toString();
            String phone = txt_phone.getText().toString();
            String comments = txt_comments.getText().toString();
            float rating = ratingBar.getRating();

            boolean error_flag = false;
            if (name.equals("")) {
                txt_name.setError("enter your name");
                error_flag = true;
            }
            if (email.equals("")) {
                txt_email.setError("enter valid email");
                error_flag = true;
            }
            if (phone.equals("")) {
                txt_phone.setError("enter valid phonenumber");
                error_flag = true;
            }
            if (!isValidEmail(email)) {
                txt_email.setError("enter valid email");
                error_flag = true;
            }
            if (!isValidMobile(phone)) {
                txt_phone.setError("enter valid phonenumber");
                error_flag = true;
            }

            if (error_flag)
                return;

            if (rating == 0 && error_flag == false) {
                error_flag = true;
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Please leave rating!")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create().show();
            }

            if (comments.equals("") && error_flag == false) {
                error_flag = true;
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Please leave comments!")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create().show();
            }

            if (error_flag == false)
                submitFeedback(name, email, phone, comments, rating);
        }
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
        return PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void submitFeedback(String name, String email, String phone, String comments, float rating) {
        String action = "feedbackreceived";
        String instanceid = "E658D06413D94F1EAF243D171BA4A26E";
        String appid = appId;

        OkHttpClient client = new OkHttpClient();

        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", action);
            jsonObject.put("instanceid", instanceid);
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("phone", phone);
            jsonObject.put("comments", comments);
            jsonObject.put("appid", appid);
            jsonObject.put("rating", rating);

            Log.d(TAG, jsonObject.toString());

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("https://api.appmc2.net/postdata.aspx")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG + "MyFailure", request.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RestaurantRateActivity.this.finish();
                            Toast toast = Toast.makeText(RestaurantRateActivity.this, "Failed", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String res = response.body().string();
                    Log.d(TAG + "Result", res);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RestaurantRateActivity.this.finish();

                            Toast toast = Toast.makeText(RestaurantRateActivity.this, "Thank you!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
            });
        } catch(Exception e) {

        }
    }
}
