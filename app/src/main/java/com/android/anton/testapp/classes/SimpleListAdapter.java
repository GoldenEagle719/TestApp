package com.android.anton.testapp.classes;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.anton.testapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anton on 9/30/2016.
 */
public class SimpleListAdapter extends ArrayAdapter<String> {

    private Activity mContext;
    ArrayList<String> itemList;
    public SimpleListAdapter(Activity c, ArrayList<String> itemList) {
        super(c, R.layout.layout_simple_list_item, itemList);

        mContext = c;
        this.itemList = itemList;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_simple_list_item, null, true);

        TextView txtView = (TextView) rowView.findViewById(R.id.layout_simple_list_item_label);
        txtView.setText(itemList.get(position));

        return rowView;
    }
}
