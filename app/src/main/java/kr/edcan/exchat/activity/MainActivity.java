package kr.edcan.exchat.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import java.util.ArrayList;
import java.util.Collections;

import kr.edcan.exchat.R;
import kr.edcan.exchat.adapter.DrawerListViewAdapter;
import kr.edcan.exchat.data.HistoryData;
import kr.edcan.exchat.service.ClipBoardService;
import kr.edcan.exchat.utils.ExchatUtils;
import kr.edcan.exchat.utils.RecycleViewAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView shareCurrent;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ArrayList<String> drawerList, title, sale;
    ArrayList<HistoryData> historyDatas;
    ListView drawerMenu;
    ExchatUtils utils;
    Spinner previousSpinner, convertSpinner;

    EditText mainOrigin;
    TextView originUnit, convertValue, convertUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefault();
        setSupportActionBar();
        startService(new Intent(getApplicationContext(), ClipBoardService.class));
    }

    private void setDefault() {
        //ArrayList
        drawerList = new ArrayList<>();
        title = new ArrayList<>();
        sale = new ArrayList<>();
        historyDatas = new ArrayList<>();

        //Main RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        historyDatas.add(new HistoryData(1 + "", "USD", 1200 + "", "KRW"));
        historyDatas.add(new HistoryData(1 + "", "USD", 1200 + "", "KRW"));
        historyDatas.add(new HistoryData(1 + "", "USD", 1200 + "", "KRW"));
        historyDatas.add(new HistoryData(1 + "", "USD", 1200 + "", "KRW"));
        RecycleViewAdapter mainAdapter = new RecycleViewAdapter(getApplicationContext(), historyDatas);
        recyclerView.setAdapter(mainAdapter);
        RecyclerViewHeader header = RecyclerViewHeader.fromXml(getApplicationContext(), R.layout.main_header);
        header.attachTo(recyclerView);

        //Utils
        utils = new ExchatUtils(getApplicationContext());
        title = utils.getTitles();
        sale = utils.getValues();
        //Header Widgets
        mainOrigin = (EditText) findViewById(R.id.header_prevValue);
        originUnit = (TextView) findViewById(R.id.header_prevUnit);
        convertValue = (TextView) findViewById(R.id.header_convertValue);
        convertUnit = (TextView) findViewById(R.id.header_convertUnit);
        mainOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String result = "0.0";
                String value = mainOrigin.getText().toString().trim();
                if(!value.isEmpty()){
                    Log.e("selected", previousSpinner.getSelectedItemPosition() + "," + convertSpinner.getSelectedItemPosition());
                    result = utils.calculateValues(Float.parseFloat(value), previousSpinner.getSelectedItemPosition()
                    , convertSpinner.getSelectedItemPosition())+"";
                }
                convertValue.setText(result);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Widgets
        previousSpinner = (Spinner) findViewById(R.id.main_previous_spinner);
        convertSpinner = (Spinner) findViewById(R.id.main_convert_spinner);
        SpinnerAdapter units = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_textstyle, title);
        previousSpinner.setAdapter(units);
        convertSpinner.setAdapter(units);

        shareCurrent = (TextView) findViewById(R.id.main_share);
        drawerMenu = (ListView) findViewById(R.id.drawer_listview);
        String list[] = new String[]{"주요 환율 수정", "최근 내역 초기화", "빠른 검색 비활성화", "개발자 정보"};
        Collections.addAll(drawerList, list);
        DrawerListViewAdapter adapter = new DrawerListViewAdapter(this, drawerList);
        drawerMenu.setAdapter(adapter);
        drawerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 3:
                        startActivity(new Intent(getApplicationContext(), DeveloperActivity.class));
                        break;
                }
            }
        });
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.background_layout);
        ViewGroup.LayoutParams params = backgroundLayout.getLayoutParams();
        params.height = (int) dpHeight;

        //OnClickListener
        shareCurrent.setOnClickListener(this);
        previousSpinner.setSelection(0);
        convertSpinner.setSelection(1);
        previousSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                originUnit.setText(title.get(position).split(" ")[1]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        convertSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertUnit.setText(title.get(position).split(" ")[1]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSupportActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.app_name, R.string.app_name);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_share:
                break;
        }
    }

}
