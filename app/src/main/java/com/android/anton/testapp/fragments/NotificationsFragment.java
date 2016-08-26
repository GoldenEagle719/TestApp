package com.android.anton.testapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anton.testapp.R;
import com.android.anton.testapp.classes.NotificationConfigListAdapter;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private ListView notifications_config_listView;
    private ViewGroup mContainer;
    public NotificationsFragment() {

    }

    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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

        final String[] itemTextAry = new String[] {"asian", "banting", "breakfast", "burgers", "child friendly", "cookery classes",
                "coffee", "desert", "drinks", "indian", "italian", "luxury dining", "meat lover", "mexican", "nightlife", "pet friendly",
                "recipes", "seafood", "streetfood", "sushi", "vegetarian", "vegan"};

        int[] switchValueAry = new int[itemTextAry.length];
        for (int i = 0; i < itemTextAry.length; i++) {
            switchValueAry[i] = getActivity().getSharedPreferences("NotificationConfig", Context.MODE_PRIVATE)
                    .getInt("NC" + String.valueOf(i), 0);
        }

        View view = inflater.inflate(R.layout.notifications_fragment, null, true);
        notifications_config_listView = (ListView)view.findViewById(R.id.notificationf_listview);
        notifications_config_listView.setAdapter(new NotificationConfigListAdapter(getActivity(), itemTextAry, switchValueAry));
        notifications_config_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String notficationID = getActivity().getSharedPreferences("NotificationConfig", Context.MODE_PRIVATE)
                        .getString("NCID" + String.valueOf(position), "");

                Toast toast = Toast.makeText(getActivity(), notficationID, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        TextView notificationf_btn_back = (TextView)view.findViewById(R.id.notificationf_btn_back);
        notificationf_btn_back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.notificationf_btn_back) {
            mContainer.setVisibility(View.GONE);
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
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
