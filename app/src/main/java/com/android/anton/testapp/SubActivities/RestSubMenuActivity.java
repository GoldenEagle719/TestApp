package com.android.anton.testapp.SubActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.SimpleListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestSubMenuActivity extends AppCompatActivity {

    private String itemsJSONAryString;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_sub_menu);

        itemsJSONAryString = getIntent().getStringExtra("items");

        final ArrayList<String> itemlist = new ArrayList<String>();
        final ArrayList<String> itemDataList = new ArrayList<String>();
        final ArrayList<String> itemOptionList = new ArrayList<String>();
        SimpleListAdapter adapter = new SimpleListAdapter(this, itemlist);

        listView = (ListView) findViewById(R.id.activity_rest_sub_menu_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestSubMenuActivity.this, RestMenuDetailActivity.class);
                intent.putExtra("title", itemlist.get(position));
                intent.putExtra("Data", itemDataList.get(position));
                intent.putExtra("Option", itemOptionList.get(position));
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);

        try {
            JSONArray jsonArray = new JSONArray(itemsJSONAryString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                itemlist.add(jsonObject.getString("Title"));
                itemDataList.add(jsonObject.getString("Data"));
                itemOptionList.add(jsonObject.getString("Options"));
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onBack(View v) {
        finish();
    }
}
