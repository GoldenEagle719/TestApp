package com.android.anton.testapp.SubActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.UserInfo;
import com.android.anton.testapp.fragments.RegistrationFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RestaurantRequestActivity extends Activity {

    private static final String TAG = "RestaurantRequest";
    private SimpleDateFormat    dateFormatter;
    private SimpleDateFormat    timeFormatter;
    private static final int    DATE_DIALOG_ID = 1;
    private static final int    TIME_DIALOG_ID = 2;
    private static final int    NUMBER_DIALOG_ID = 3;

    private TextView                lbl_date;
    private TextView                lbl_time;
    private TextView                lbl_peoples;
    private FrameLayout             container_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_request);

        lbl_date = (TextView) findViewById(R.id.activity_restaurantrequest_label_date);
        lbl_time = (TextView) findViewById(R.id.activity_restaurantrequest_label_time);
        lbl_peoples = (TextView) findViewById(R.id.activity_restaurantrequest_label_peoples);
        container_fragment = (FrameLayout) findViewById(R.id.activity_restaurantrequest_container_fragment);
        container_fragment.setVisibility(View.GONE);

        UserInfo userInfo = new UserInfo(this);
        if (userInfo.getName().equals("") && userInfo.getEmail().equals("") && userInfo.getTelephone().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Please complete your profile")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RegistrationFragment fragment = new RegistrationFragment();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.add(R.id.activity_restaurantrequest_container_fragment, fragment).commit();

                            container_fragment.setVisibility(View.VISIBLE);
                        }
                    }).show();
        }

        dateFormatter = new SimpleDateFormat("EEE, MMMM d, yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm aa");

        hideKeyboard();
    }

    public void hideKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void activity_restaurantrequest_btn_date_clicked(View v) {
        showDialog(DATE_DIALOG_ID);
    }

    public void activity_restaurantrequest_btn_time_clicked(View v) {
        showDialog(TIME_DIALOG_ID);
    }

    public void activity_restaurantrequest_btn_peoples_clicked(View v) {
        showDialog(NUMBER_DIALOG_ID);
    }

    public void activity_restaurantrequest_btn_back_clicked(View v) {
        finish();
    }

    public void activity_restaurantrequest_btn_send_clicked(View v) {
        UserInfo userInfo = new UserInfo(this);
        if (userInfo.getName().equals("") && userInfo.getEmail().equals("") && userInfo.getTelephone().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Please complete your profile")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RegistrationFragment fragment = new RegistrationFragment();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.add(R.id.activity_restaurantrequest_container_fragment, fragment).commit();

                            container_fragment.setVisibility(View.VISIBLE);
                        }
                    }).show();

        } else {
            // TODO: 9/15/2016 : SendData
        }
    }

    public void activity_restaurantrequest_btn_profileedit_clicked(View v) {
        RegistrationFragment fragment = new RegistrationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.activity_restaurantrequest_container_fragment, fragment).commit();

        container_fragment.setVisibility(View.VISIBLE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        String dateString = formatter.format(date);
        int year = Integer.parseInt(dateString.split("-")[0]);
        int month = Integer.parseInt(dateString.split("-")[1]);
        int day = Integer.parseInt(dateString.split("-")[2]);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener,
                        year, month - 1, day);

            case TIME_DIALOG_ID:
               return new TimePickerDialog(this, timePickerListener, date.getHours(), date.getMinutes(), false);
            case NUMBER_DIALOG_ID:
                showNumberPickerDialog();
        }
        return null;
    }

    public void showNumberPickerDialog() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_number_picker);
        Button btn_set = (Button) d.findViewById(R.id.dialog_numberpicker_btn_set);
        Button btn_cancel = (Button) d.findViewById(R.id.dialog_numberpicker_btn_cancel);
        final NumberPicker picker = (NumberPicker) d.findViewById(R.id.dialog_numberpicker_picker);
        picker.setMaxValue(10);
        picker.setMinValue(0);
        picker.setWrapSelectorWheel(false);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lbl_peoples.setText(String.valueOf(picker.getValue()));
                d.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener () {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            lbl_date.setText(dateFormatter.format(calendar.getTime()));

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hour,
                              int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.YEAR, calendar.MONTH, calendar.DAY_OF_MONTH, hour, minute);
            lbl_time.setText(timeFormatter.format(calendar.getTime()));
        }
    };


}
