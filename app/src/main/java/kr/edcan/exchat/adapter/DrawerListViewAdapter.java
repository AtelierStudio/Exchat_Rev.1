package kr.edcan.exchat.adapter;

/**
 * Created by Junseok on 2016. 1. 10..
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rey.material.widget.Spinner;
import com.rey.material.widget.Switch;

import java.util.ArrayList;

import kr.edcan.exchat.R;
import kr.edcan.exchat.activity.MainActivity;

/**
 * Created by kotohana5706 on 2015. 11. 21.
 * Copyright by Sunrin Internet High School EDCAN
 * All rights reversed.
 */
public class DrawerListViewAdapter extends ArrayAdapter<String> {
    private LayoutInflater mInflater;
    Context context;

    public DrawerListViewAdapter(Context context, ArrayList<String> object) {
        super(context, 0, object);
        this.context = context;
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
            final SharedPreferences sharedPreferences = context.getSharedPreferences("Exchat", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            final Switch swit = (Switch) view.findViewById(R.id.drawer_switch);
            swit.setEnabled(false);
            switch (s) {
                case "부팅시 자동 실행":
                    swit.setVisibility(View.VISIBLE);
                    if (sharedPreferences.getBoolean("onBoot", true)) {
                        swit.setChecked(true);
                        editor.putBoolean("onBoot", true);
                        editor.commit();
                    } else {
                        swit.setChecked(false);
                    }
                    break;
                case "빠른 검색":
                    swit.setVisibility(View.VISIBLE);
                    if (sharedPreferences.getBoolean("fastSearch", true)) {
                        swit.setChecked(true);
                        editor.putBoolean("fastSearch", true);
                        editor.commit();
                    } else {
                        swit.setChecked(false);
                    }
                    break;
                case "빠른 검색 알림":
                    swit.setVisibility(View.VISIBLE);
                    if(sharedPreferences.getBoolean("fastSearchAlert", true)){
                        swit.setChecked(true);
                        editor.putBoolean("fastSearchAlert", true);
                        editor.commit();
                    } else{
                        swit.setChecked(false);
                    }
                case "빠른 검색시 진동":
                    swit.setVisibility(View.VISIBLE);
                    if (sharedPreferences.getBoolean("fastSearchVibrate", true)) {
                        swit.setChecked(true);
                        editor.putBoolean("fastSearchVibrate", true);
                        editor.commit();
                    } else {
                        swit.setChecked(false);
                    }
                    break;
            }
        }
        TextView title = (TextView) view.findViewById(R.id.drawer_listview_text);
        title.setText(s);
        return view;
    }
}
