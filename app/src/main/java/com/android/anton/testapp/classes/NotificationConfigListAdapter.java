package com.android.anton.testapp.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.anton.testapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 8/17/2016.
 */
public class NotificationConfigListAdapter extends ArrayAdapter<String>{
    private static final String TAG = "NCListAdapter";

    private Activity mContext;

    String[]    txtAry;
    int[]       valueAry;


    public NotificationConfigListAdapter(Activity c, String[] itemTextAry, int[] switchValueAry) {
        super(c, R.layout.item_list, itemTextAry);

        mContext    = c;
        txtAry     = itemTextAry;
        valueAry    = switchValueAry;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.notification_config_list_item, null, true);

        TextView txtView = (TextView) rowView.findViewById(R.id.notification_item_text);
        Switch switchView = (Switch)rowView.findViewById(R.id.notification_item_switch);

        txtView.setText(txtAry[position]);
        switchView.setChecked((valueAry[position] == 1)?true:false);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String actionName = "pushswitchchanged";
                String instanceid = "E658D06413D94F1EAF243D171BA4A26E";
                String appid = "a72cb1dd-7767-41ca-a3c1-f07af763f469";
                String pushswitchid = ids[position];
                String switchvalue = ((isChecked == true)?"on":"off");

                OkHttpClient client = new OkHttpClient();

                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("action", actionName);
                    jsonObject.put("instanceid", instanceid);
                    jsonObject.put("appid", appid);
                    jsonObject.put("tagid", pushswitchid);
                    jsonObject.put("tag", switchvalue);

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
                        }

                        @Override
                        public void onResponse(final Response response) throws IOException {
                            String res;
                            res = response.body().string();

                            Log.d(TAG + "MyResult", res);

                        }
                    });
                } catch(Exception e) {

                }

                SharedPreferences.Editor editor = mContext.getSharedPreferences("NotificationConfig", Context.MODE_PRIVATE).edit();
                editor.putInt("NC" + String.valueOf(position), ((isChecked == true)?1:0));
                editor.putString("NCID" + String.valueOf(position), ids[position]);
                editor.commit();

            }
        });

        return rowView;
    }

    public String[] ids = {
        "0f319ade-c17c-40e0-8af2-45e956222b14","16250897-8a11-4046-96e4-82c7db296fc7",
        "1d271b49-259f-42d2-97c0-7d325903fdfc","2042ac6d-a2cd-4337-ae14-c08a55d83c1e",
        "25afb326-66c8-46cb-a9bd-b989e30357aa","27ea5990-86b2-4276-9c3c-3a28c57782a9",
        "2ae666bc-f806-4e5a-958f-528fc7adcfa9","2e258108-53c9-40d7-b163-9ff24d3b3fe6",
        "3044dced-f24e-4eaa-9d1e-2c49ec27f1e7","4369da09-4d17-4600-9b87-fab346f36bb4",
        "54b6cca7-6814-4563-8d52-67b5abc03964","5b816080-01ec-4927-bf25-4a5126927b24",
        "5da28449-0c07-49d6-a885-e8ca68562454","6f2c7c52-9b26-463a-a76a-b1fdebfa764e",
        "73e4f362-cca3-40fb-8466-8173e818076d","7dab6470-6ff0-4a4f-a7ff-733e950106df",
        "a269adf0-56eb-47cd-9036-68c9c450c4c4","aa47d581-289b-41dc-8f9a-e0155b7844de",
        "ab7caf52-0faf-443c-be29-b2db619068dc","ba3808f3-2a53-4a6e-83f3-63a06861385d",
        "ef03891b-cda5-4261-bcd8-2267fa5cf5d7","f1f54515-2315-4881-a26c-25e6dc85bf33",
    };

}
