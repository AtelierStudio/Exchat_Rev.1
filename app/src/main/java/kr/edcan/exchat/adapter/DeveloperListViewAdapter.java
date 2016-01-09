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
import kr.edcan.exchat.data.DeveloperData;

/**
 * Created by kotohana5706 on 2015. 11. 21.
 * Copyright by Sunrin Internet High School EDCAN
 * All rights reversed.
 */
public class DeveloperListViewAdapter extends ArrayAdapter<DeveloperData> {
    // 레이아웃 XML을 읽어들이기 위한 객체
    private LayoutInflater mInflater;

    public DeveloperListViewAdapter(Context context, ArrayList<DeveloperData> object) {
        // 상위 클래스의 초기화 과정
        // context, 0, 자료구조
        super(context, 0, object);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = null;
        // 현재 리스트의 하나의 항목에 보일 컨트롤 얻기
        if (v == null) {
            // XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
            view = mInflater.inflate(R.layout.developer_listview_content, null);
        } else {
            view = v;
        }
        // 자료를 받는다.
        final DeveloperData data = this.getItem(position);
        if (data != null) {
            //화면 출력
            TextView title = (TextView)view.findViewById(R.id.developer_listview_title);
            TextView content = (TextView)view.findViewById(R.id.developer_listview_content);
            title.setText(data.getTitle());
            content.setText(data.getContent());
        }
        return view;
    }
}

