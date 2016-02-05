package kr.edcan.exchat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import kr.edcan.exchat.R;
import kr.edcan.exchat.adapter.DrawerListViewAdapter;
import kr.edcan.exchat.data.HistoryData;
import kr.edcan.exchat.service.ClipBoardService;
import kr.edcan.exchat.utils.ExchatClipboardUtils;
import kr.edcan.exchat.utils.ExchatUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    long lastUpdateTime;
    private static final String TYPEFACE_NAME = "roboto_light.ttf";
    Typeface typeface = null;
    TextView shareCurrent;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ArrayList<String> drawerList, title, sale;
    ArrayList<HistoryData> historyDatas;
    ListView drawerMenu;
    ExchatUtils utils;
    Spinner previousSpinner, convertSpinner;
    Realm realm;
    ImageView spinnerTo;
    EditText mainOrigin;
    TextView originUnit, convertValue, convertUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTypeFace();
        setContentView(R.layout.activity_main);
        realm = Realm.getInstance(this);
        setDefault();
        setSupportActionBar();
        startService(new Intent(getApplicationContext(), ClipBoardService.class));
    }

    private void loadTypeFace() {
        if (typeface == null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
    }

    private void setDefault() {
        utils = new ExchatUtils(getApplicationContext());
        sharedPreferences = getSharedPreferences("Exchat", 0);
        editor = sharedPreferences.edit();
        //ArrayList
        drawerList = new ArrayList<>();
        title = new ArrayList<>();
        sale = new ArrayList<>();
        historyDatas = new ArrayList<>();

        utils.setFinanceStatus();
        title = utils.getFinanceFromDB(true);
        sale = utils.getFinanceFromDB(false);
        //Utils
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
                updateCalc(s);
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

        spinnerTo = (ImageView) findViewById(R.id.main_spinner_to_image);
        spinnerTo.getDrawable().setColorFilter(Color.parseColor("#7B8F9A"), PorterDuff.Mode.MULTIPLY);
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
                    case 0:
                        break;
                    case 1:
                        new MaterialDialog.Builder(MainActivity.this)
                                .content("최근 기록을 삭제합니다.")
                                .neutralText("취소")
                                .positiveText("확인")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        eraseDB();
                                    }
                                })
                                .show();
                        break;
                    case 2:
                        boolean b = sharedPreferences.getBoolean("fastSearch", true);
                        if (b) editor.putBoolean("fastSearch", false);
                        else editor.putBoolean("fastSearch", true);
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), DeveloperActivity.class));
                        break;
                }
            }

        });

        shareCurrent.setOnClickListener(this);
        previousSpinner.setSelection(1);
        convertSpinner.setSelection(0);
        previousSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                originUnit.setText(title.get(position).split(" ")[1]);
                updateCalc(mainOrigin.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        convertSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertUnit.setText(title.get(position).split(" ")[1]);
                updateCalc(mainOrigin.getText().toString());
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

    public void updateCalc(CharSequence s) {
        String result = "0.0";
        String value = s.toString().trim();
        if (!value.isEmpty()) {
            Log.e("selected", previousSpinner.getSelectedItemPosition() + "," + convertSpinner.getSelectedItemPosition());
            result = utils.calculateValues(Float.parseFloat(value), previousSpinner.getSelectedItemPosition()
                    , convertSpinner.getSelectedItemPosition()) + "";
        }
        convertValue.setText(result);
    }

    private void shareText() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "오늘 일본환율은 100 JPY = 1,020.58 KRW 입니다. #Exchat.");
        startActivity(Intent.createChooser(sharingIntent, "환율 정보 공유"));
    }


    private void eraseDB() {
        realm.beginTransaction();
        RealmResults<HistoryData> results = realm.where(HistoryData.class)
                .findAll();
        results.clear();
        realm.commitTransaction();
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
                shareText();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHistoryData();
    }

    private void setHistoryData() {
        ExchatClipboardUtils clipboardUtils = new ExchatClipboardUtils(getApplicationContext());
        realm.beginTransaction();
        RealmResults<HistoryData> results = realm.where(HistoryData.class)
                .findAll();
        results.sort("date", Sort.ASCENDING);
        realm.commitTransaction();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout cardOuter = (LinearLayout) findViewById(R.id.outer_view);
        cardOuter.removeAllViews();
        for (HistoryData data : results) {
            View view = layoutInflater.inflate(R.layout.main_cardview_content, null);
            TextView prevValue = (TextView) view.findViewById(R.id.prevValue);
            TextView prevUnit = (TextView) view.findViewById(R.id.prevUnit);
            TextView convertValue = (TextView) view.findViewById(R.id.convertValue);
            TextView convertUnit = (TextView) view.findViewById(R.id.convertUnit);
            prevUnit.setText(clipboardUtils.foreignmoneyUnits[data.getPrevUnit()]);
            prevValue.setText(data.getPrevValue() + "");
            convertValue.setText(data.getConvertValue() + "");
            convertUnit.setText(clipboardUtils.foreignmoneyUnits[data.getConvertUnit()]);
            cardOuter.addView(view);
        }
    }
}
