package com.android.anton.testapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
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
import java.util.List;


public class SearchActivity extends Activity {

    private static final String TAG = "SearchActivity";
    private EditText        edit_locationName;
    private ListView        search_listview_result;

    public ArrayAdapter<String> searchResultListViewAdapter;
    public List<String>     locationList;
    public List<Double>     locationLatList;
    public List<Double>     locationLongList;

    private boolean searchClicked = false;
    public String res;
    public int      SEARCH_RESULT = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_actvity);
        setResult(SEARCH_RESULT);

        edit_locationName = (EditText)findViewById(R.id.edit_locationName);
        edit_locationName.addTextChangedListener(textWatcher);

        String[] locations = new String[0];
        locationList = new ArrayList<String>(Arrays.asList(locations));
        locationLatList = new ArrayList<Double>();
        locationLongList = new ArrayList<Double>();
        search_listview_result = (ListView)findViewById(R.id.search_listview_result);
        searchResultListViewAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, locationList);
        search_listview_result.setAdapter(searchResultListViewAdapter);
        search_listview_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences("SelectedLocation", MODE_PRIVATE).edit();
                editor.putString("Name", locationList.get(position));
                editor.putString("Lat", String.valueOf(locationLatList.get(position)));
                editor.putString("Long", String.valueOf(locationLongList.get(position)));
                editor.commit();

                finish();
            }
        });
    }

    public void btn_backClicked(View v) {
        finish();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count > 2) {
                String actionName = "searchlocations";
                String instanceid = "E658D06413D94F1EAF243D171BA4A26E";
                String searchText = s.toString();
                String boundaryString = "----------------Boundary------------------";

                OkHttpClient client = new OkHttpClient();

                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("action", actionName);
                    jsonObject.put("instanceid", instanceid);
                    jsonObject.put("searchid", 0);
                    jsonObject.put("searchtext", searchText);
                    jsonObject.put("maxresult", 10);

                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder()
                            .url("https://api.appmc2.net/postdata.aspx")
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d(TAG + "MyFailure", request.toString());
                        }

                        @Override
                        public void onResponse(final Response response) throws IOException {
                            res = response.body().string();
                            Log.d(TAG, res);

                            try {
                                JSONObject resJSONObject = new JSONObject(res);
                                JSONArray resultlist = resJSONObject.getJSONArray("resultlist");

                                locationList.clear();
                                locationLatList.clear();
                                locationLongList.clear();
                                for (int i = 0; i < resultlist.length(); i++) {
                                    JSONObject tempJSONObject = resultlist.getJSONObject(i);

                                    locationList.add(tempJSONObject.getString("name"));
                                    locationLatList.add(tempJSONObject.getDouble("lat"));
                                    locationLongList.add(tempJSONObject.getDouble("lng"));
                                }

                                refreshListView();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "ExceptionOccur");
                            }

                        }
                    });
                } catch(Exception e) {

                }
            } else {
                locationList.clear();
                refreshListView();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void refreshListView() {
        this.runOnUiThread(new RefreshListView());
    }

    private class RefreshListView implements Runnable
    {

        public RefreshListView()
        {
        }

        @Override
        public void run()
        {
            searchResultListViewAdapter.notifyDataSetChanged();
        }
    }



//    public void btn_searchLocationClicked(View v) {
//
//        if (searchClicked == false) {
//            String searchText = edit_locationName.getText().toString();
//
//            if (searchText.equals("") || searchText.length() < 3) {
//                Toast toast = Toast.makeText(SearchActivity.this, "Enter correct location", Toast.LENGTH_LONG);
//                toast.show();
//
//                return;
//            }
//
//            ((ImageButton)(v)).setImageResource(R.mipmap.cancel);
//            searchClicked = true;
//
//            String actionName = "searchlocations";
//            String instanceid = "E658D06413D94F1EAF243D171BA4A26E";
//            String boundaryString = "----------------Boundary------------------";
//
//            OkHttpClient client = new OkHttpClient();
//
//            try {
//                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("action", actionName);
//                jsonObject.put("instanceid", instanceid);
//                jsonObject.put("searchid", 0);
//                jsonObject.put("searchtext", searchText);
//                jsonObject.put("maxresult", 10);
//
//                Log.d(TAG, jsonObject.toString());
//
//
//                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//                Request request = new Request.Builder()
//                        .url("https://api.appmc2.net/postdata.aspx")
//                        .post(body)
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//                        Log.d("MyFailure", request.toString());
//                    }
//
//                    @Override
//                    public void onResponse(final Response response) throws IOException {
//                        res = response.body().string();
//
//                        Log.d("MyResult", res);
//
//                    }
//                });
//            } catch(Exception e) {
//
//            }
//        } else {
//            searchClicked = false;
//            ((ImageButton)(v)).setImageResource(R.mipmap.search_normal);
//        }
//
//    }

}
