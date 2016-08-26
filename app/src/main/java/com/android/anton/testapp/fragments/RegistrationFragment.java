package com.android.anton.testapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "RegistrationFragment";

    private OnFragmentInteractionListener mListener;

    private EditText registrationf_edit_name;
    private EditText registrationf_edit_email;
    private EditText registrationf_edit_address1;
    private EditText registrationf_edit_address2;
    private EditText registrationf_edit_town;
    private EditText registrationf_edit_postcode;
    private EditText registrationf_edit_telephone;

    private ViewGroup mContainer;

    public RegistrationFragment() {

    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mContainer = container;

        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        TextView registrationf_btn_back = (TextView)view.findViewById(R.id.registrationf_btn_back);
        registrationf_btn_back.setOnClickListener(this);
        TextView registrationf_btn_save= (TextView)view.findViewById(R.id.registrationf_btn_save);
        registrationf_btn_save.setOnClickListener(this);

        registrationf_edit_name = (EditText)view.findViewById(R.id.registrationf_edit_name);
        registrationf_edit_email = (EditText)view.findViewById(R.id.registrationf_edit_email);
        registrationf_edit_address1 = (EditText)view.findViewById(R.id.registrationf_edit_address1);
        registrationf_edit_address2 = (EditText)view.findViewById(R.id.registrationf_edit_address2);
        registrationf_edit_town = (EditText)view.findViewById(R.id.registrationf_edit_town);
        registrationf_edit_postcode = (EditText)view.findViewById(R.id.registrationf_edit_postcode);
        registrationf_edit_telephone = (EditText)view.findViewById(R.id.registrationf_edit_telephone);

        SharedPreferences pref;
        pref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email = pref.getString("email", "");
        String address1 = pref.getString("address1", "");
        String address2 = pref.getString("address2", "");
        String town = pref.getString("town", "");
        String postcode = pref.getString("postcode", "");
        String telephone = pref.getString("telephone", "");

        registrationf_edit_name.setText(name);
        registrationf_edit_email.setText(email);
        registrationf_edit_address1.setText(address1);
        registrationf_edit_address2.setText(address2);
        registrationf_edit_town.setText(town);
        registrationf_edit_postcode.setText(postcode);
        registrationf_edit_telephone.setText(telephone);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registrationf_btn_back) {
            mContainer.setVisibility(View.GONE);

            hideKeyboard();

            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }

        if (v.getId() == R.id.registrationf_btn_save) {
            String name = registrationf_edit_name.getText().toString();
            String email = registrationf_edit_email.getText().toString();
            String address1 = registrationf_edit_address1.getText().toString();
            String address2 = registrationf_edit_address2.getText().toString();
            String town = registrationf_edit_town.getText().toString();
            String postcode = registrationf_edit_postcode.getText().toString();
            String telephone = registrationf_edit_telephone.getText().toString();

            boolean error_flag = false;
            if (name.equals("")) {
                registrationf_edit_name.setError("enter your name");
                error_flag = true;
            }
            if (email.equals("")) {
                registrationf_edit_email.setError("enter valid email");
                error_flag = true;
            }
            if (telephone.equals("")) {
                registrationf_edit_telephone.setError("enter valid phonenumber");
                error_flag = true;
            }
            if (!isValidEmail(email)) {
                registrationf_edit_email.setError("enter valid email");
                error_flag = true;
            }
            if (!isValidMobile(telephone)) {
                registrationf_edit_telephone.setError("enter valid phonenumber");
                error_flag = true;
            }

            if (error_flag)
                return;

            UserInfo userInfo = new UserInfo(name, email, telephone);
            userInfo.setAddress1(address1);
            userInfo.setAddress2(address2);
            userInfo.setTown(town);
            userInfo.setPostcode(postcode);

            SharedPreferences.Editor editor = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE).edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.putString("address1", address1);
            editor.putString("address2", address2);
            editor.putString("town", town);
            editor.putString("postcode", postcode);
            editor.putString("telephone", telephone);
            editor.commit();

            Toast toast = Toast.makeText(getActivity(), "Saved successfully", Toast.LENGTH_LONG);
            toast.show();

            hideKeyboard();

            mContainer.setVisibility(View.GONE);
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
