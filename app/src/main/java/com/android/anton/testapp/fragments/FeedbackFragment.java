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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.MainActivity;
import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.NotificationConfigListAdapter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "FeedbackF";

    private OnFragmentInteractionListener mListener;
    private ViewGroup   mContainer;
    private EditText    feedbackf_edittext_name;
    private EditText    feedbackf_edittext_email;
    private EditText    feedbackf_edittext_phone;
    private EditText    feedbackf_edittext_comments;
    private RatingBar   feedbackf_ratingBar;

    public FeedbackFragment() {

    }

    public static FeedbackFragment newInstance() {
        FeedbackFragment fragment = new FeedbackFragment();
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

        View view = inflater.inflate(R.layout.feedback_fragment, null, true);
        view.setOnClickListener(this);
        /*Buttons----------------------------------------------------------------------*/
        TextView feedbackf_btn_back = (TextView)view.findViewById(R.id.feedbackf_btn_back);
        feedbackf_btn_back.setOnClickListener(this);
        TextView feedbackf_btn_submit = (TextView)view.findViewById(R.id.feedbackf_btn_submit);
        feedbackf_btn_submit.setOnClickListener(this);
        /*EditTexts-------------------------------------------------------------------------*/
        feedbackf_edittext_name = (EditText) view.findViewById(R.id.feedbackf_edittext_name);
        feedbackf_edittext_email = (EditText)view.findViewById(R.id.feedbackf_edittext_email);
        feedbackf_edittext_phone = (EditText)view.findViewById(R.id.feedbackf_edittext_phone);
        feedbackf_edittext_comments = (EditText)view.findViewById(R.id.feedbackf_edittext_comments);
        feedbackf_ratingBar = (RatingBar)view.findViewById(R.id.feedbackf_ratingBar);

        UserInfo userInfo = new UserInfo(getActivity());

        feedbackf_edittext_name.setText(userInfo.getName());
        feedbackf_edittext_email.setText(userInfo.getEmail());
        feedbackf_edittext_phone.setText(userInfo.getTelephone());

        return view;
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();

        if (v.getId() == R.id.feedbackf_btn_back) {
            mContainer.setVisibility(View.GONE);
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }

        if (v.getId() == R.id.feedbackf_btn_submit) {
            String name = feedbackf_edittext_name.getText().toString();
            String email = feedbackf_edittext_email.getText().toString();
            String phone = feedbackf_edittext_phone.getText().toString();
            String comments = feedbackf_edittext_comments.getText().toString();
            float rating = feedbackf_ratingBar.getRating();

            boolean error_flag = false;
            if (name.equals("")) {
                feedbackf_edittext_name.setError("enter your name");
                error_flag = true;
            }
            if (email.equals("")) {
                feedbackf_edittext_email.setError("enter valid email");
                error_flag = true;
            }
            if (phone.equals("")) {
                feedbackf_edittext_phone.setError("enter valid phonenumber");
                error_flag = true;
            }
            if (!isValidEmail(email)) {
                feedbackf_edittext_email.setError("enter valid email");
                error_flag = true;
            }
            if (!isValidMobile(phone)) {
                feedbackf_edittext_phone.setError("enter valid phonenumber");
                error_flag = true;
            }

            if (error_flag)
                return;

            if (rating == 0 && error_flag == false) {
                error_flag = true;
                new AlertDialog.Builder(getActivity())
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
                new AlertDialog.Builder(getActivity())
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

    public void submitFeedback(String name, String email, String phone, String comments, float rating) {
        String action = "feedbackreceived";
        String instanceid = "E658D06413D94F1EAF243D171BA4A26E";
        String appid = "a72cb1dd-7767-41ca-a3c1-f07af763f469";

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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContainer.setVisibility(View.GONE);
                            getActivity().getFragmentManager().beginTransaction().remove(FeedbackFragment.this).commit();

                            Toast toast = Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String res = response.body().string();
                    Log.d(TAG + "Result", res);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContainer.setVisibility(View.GONE);
                            getActivity().getFragmentManager().beginTransaction().remove(FeedbackFragment.this).commit();

                            Toast toast = Toast.makeText(getActivity(), "Thank you!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                }
            });
        } catch(Exception e) {

        }
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
