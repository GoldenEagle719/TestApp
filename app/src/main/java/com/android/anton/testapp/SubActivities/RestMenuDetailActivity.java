package com.android.anton.testapp.SubActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.RestOptionsListAdapter;
import com.android.anton.testapp.classes.SimpleListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestMenuDetailActivity extends AppCompatActivity {

    private String dataJSONString;
    private String optionJSONString;
    private String title;

    private TextView    lbl_title;
    private TextView    lbl_content;
    private TextView    lbl_price;
    private ImageView   imgView;
    private ListView    listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_menu_detail);

        title = getIntent().getStringExtra("title");
        dataJSONString = getIntent().getStringExtra("Data");
        optionJSONString = getIntent().getStringExtra("Option");

        lbl_title = (TextView) findViewById(R.id.activity_rest_menu_detail_title);
        lbl_content = (TextView) findViewById(R.id.activity_rest_menu_detail_content);
        lbl_price = (TextView) findViewById(R.id.activity_rest_menu_detail_price);
        imgView = (ImageView) findViewById(R.id.activity_rest_menu_detail_image);

        ArrayList<String> optionList = new ArrayList<>();
        ArrayList<String> amountList = new ArrayList<>();
        RestOptionsListAdapter adapter = new RestOptionsListAdapter(this, optionList, amountList);

        listView = (ListView) findViewById(R.id.activity_rest_menu_detail_listview);
        listView.setAdapter(adapter);

        try {
            JSONObject dataJSONObject = new JSONObject(dataJSONString);
            lbl_title.setText(title);
            lbl_content.setText(dataJSONObject.getString("description"));
            lbl_price.setText("Price: " + dataJSONObject.getString("price"));

            Picasso.with(this)
                    .load(dataJSONObject.getString("image"))
                    .into(imgView);

            JSONArray optionsJSONAry = new JSONArray(optionJSONString);
            for (int i = 0; i < optionsJSONAry.length(); i++) {
                JSONObject jsonObject = optionsJSONAry.getJSONObject(i);
                optionList.add(jsonObject.getString("Text"));
                amountList.add(jsonObject.getString("Amount"));
            }

        } catch (Exception e) {

        }

    }

    public void onBack(View v) {
        finish();
    }
}
