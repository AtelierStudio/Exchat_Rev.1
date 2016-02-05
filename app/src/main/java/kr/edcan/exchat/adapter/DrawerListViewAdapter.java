package kr.edcan.exchat.adapter;

/**
 * Created by Junseok on 2016. 1. 10..
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.edcan.exchat.R;

/**
 * Created by kotohana5706 on 2015. 11. 21.
 * Copyright by Sunrin Internet High School EDCAN
 * All rights reversed.
 */
public class DrawerListViewAdapter extends ArrayAdapter<String> {
    private LayoutInflater mInflater;

    public DrawerListViewAdapter(Context context, ArrayList<String> object) {
        super(context, 0, object);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = null;
        if (v == null) {
            view = mInflater.inflate(R.layout.drawer_listview_content, null);
        } else {
            view = v;
        }
        String s = this.getItem(position);
        if (s != null) {
            TextView title = (TextView)view.findViewById(R.id.drawer_listview_text);
            title.setText(s);
        }
        return view;
    }
}

