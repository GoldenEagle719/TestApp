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

/**
 * Created by Administrator on 8/17/2016.
 */
public class CustomAdapter extends ArrayAdapter<String>{

    private Activity mContext;

    String[]    txtAry;
    int[]       imgIDAry;


    public CustomAdapter(Activity c, String[] itemTextAry, int[] itemImgIDAry) {
        super(c, R.layout.item_list, itemTextAry);

        mContext    = c;
        txtAry     = itemTextAry;
        imgIDAry    = itemImgIDAry;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_list, null, true);

        TextView txtView = (TextView) rowView.findViewById(R.id.item_text);
        ImageView imgView = (ImageView)rowView.findViewById(R.id.item_img);

        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/JennaSue.ttf");
        txtView.setTypeface(typeFace);

        txtView.setText(txtAry[position]);

        Picasso.with(mContext)
                .load(imgIDAry[position])
                .placeholder(R.drawable.empty)
                .error(R.drawable.empty)
                .into(imgView);

        return rowView;
    }

}
