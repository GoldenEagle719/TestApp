package com.android.anton.testapp.SubActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.SimpleListAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RestMenuActivity extends AppCompatActivity {
    private static final String TAG = "RestMenu";

    private ListView    listView;
    private String      appId;
    ArrayList<String>   itemlist;
    ArrayList<String>   subItemStringList;
    SimpleListAdapter   adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_menu);

        appId = getIntent().getStringExtra("appId");

        itemlist = new ArrayList<String>();
        subItemStringList = new ArrayList<>();
        adapter = new SimpleListAdapter(this, itemlist);
        listView = (ListView) findViewById(R.id.activity_rest_menu_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestMenuActivity.this, RestSubMenuActivity.class);
                Log.d(TAG, subItemStringList.get(position));
                intent.putExtra("items", subItemStringList.get(position));
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);

        getData();
    }

    public void getData() {
        String appid = appId;
        OkHttpClient client = new OkHttpClient();

        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder()
                    .url(String.format("https://api.appmc2.net/appjson.aspx?appid=%s&version=5&section=%s", appid,"menu"))
                    .build();

            Log.d(TAG, String.format("https://api.appmc2.net/appjson.aspx?appid=%s&version=5&section=%s", appid,"menu"));

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String res = response.body().string();
                    parseData(res);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void parseData(final String res) {

        if (res.equals("")) {
            return;
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray itemJSONAry = new JSONArray(res);

                    if (itemJSONAry.length() > 0) {
                        itemlist.clear();
                        subItemStringList.clear();

                        for (int i = 0; i < itemJSONAry.length(); i++) {
                            JSONObject itemJSONObject= itemJSONAry.getJSONObject(i);
                            String title = itemJSONObject.getString("Title");
                            String subItemString = itemJSONObject.getString("Items");

                            Log.d(TAG, "Title:" + title);
                            Log.d(TAG, "subItem:" + subItemString);

                            itemlist.add(title);
                            subItemStringList.add(subItemString);
                        }

                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onBack(View v) {
        finish();
    }
}
