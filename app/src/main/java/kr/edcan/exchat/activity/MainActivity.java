package kr.edcan.exchat.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.Collections;

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

    public static Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String TYPEFACE_NAME = "roboto_light.ttf";
    Typeface typeface = null;
    TextView shareCurrent, originUnit, convertValue, convertUnit, majorFinanceFrom, majorFinanceTo;
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
    Intent service;
    int majorFinance;
    MaterialDialog loading;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTypeFace();
        context = this;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            setPackage();
        sharedPreferences = getSharedPreferences("Exchat", 0);
        editor = sharedPreferences.edit();
        boolean isFirst = sharedPreferences.getBoolean("isFirst", true);
        if (isFirst) {
            if (!new ExchatUtils().isNetworkAvailable(getApplicationContext()))
                startActivity(new Intent(getApplicationContext(), NetworkCheckActivity.class));
            else startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);
            realm = Realm.getInstance(this);
            setDefault();
            setMajorCalc();
            setSupportActionBar();
            if (sharedPreferences.getBoolean("fastSearch", true))
                startService(service);
        }
    }

    private void setMajorCalc() {
        if (title.size() != 0) {
            majorFinanceFrom = (TextView) findViewById(R.id.main_major_finance_from);
            majorFinanceTo = (TextView) findViewById(R.id.main_major_finance_to);
            majorFinance = sharedPreferences.getInt("mainUnit", 1);
            int origin = 1;
            if (majorFinance == 3 || majorFinance == 30 || majorFinance == 40) origin = 100;
            majorFinanceFrom.setText(origin + " " + title.get(majorFinance).split(" ")[1]);
            majorFinanceTo.setText(utils.convertToKRW(majorFinance, origin) + " KRW");
            majorFinanceFrom.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.ic_main_mainexchange_to), null);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setPackage() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(MainActivity.this, "Exchat을 실행하기 위해 권한을 허용해주세요!", Toast.LENGTH_SHORT).show();
                setPackage();
            }
        }
    }

    private void loadTypeFace() {
        if (typeface == null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
    }

    private void setDefault() {
        loading = new MaterialDialog.Builder(MainActivity.this)
                .content("환율 정보 로딩중")
                .progress(true, 0)
                .show();
        service = new Intent(MainActivity.this, ClipBoardService.class);
        utils = new ExchatUtils(getApplicationContext());
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

        String list[] = new String[]{"주요 환율 수정", "최근 내역 삭제", "부팅시 자동 실행", "빠른 검색", "빠른 검색시 진동", "개발자 정보", "앱 종료"};
        Collections.addAll(drawerList, list);
        DrawerListViewAdapter adapter = new DrawerListViewAdapter(this, drawerList);
        drawerMenu.setAdapter(adapter);
        drawerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Switch sw = (Switch) view.findViewById(R.id.drawer_switch);
                sw.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(Switch view, boolean checked) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });
                switch (position) {
                    case 0:
                        // 주요 환율 수정
                        startActivity(new Intent(getApplicationContext(), MainUnitSetActivity.class));
                        break;
                    case 1:
                        // 최근 내역 삭제
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("최근 기록")
                                .content("최근 기록이 전부 삭제됩니다.\n계속하시겠습니까?")
                                .neutralText("취소")
                                .positiveText("확인")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        eraseDB();
                                        setHistoryData();
                                    }
                                })
                                .show();
                        break;
                    case 2:
                        //부팅시 자동 실행
                        if (sw.isChecked()) {
                            editor.putBoolean("onBoot", false);
                            sw.setChecked(false);
                        } else {
                            editor.putBoolean("onBoot", true);
                            sw.setChecked(true);
                        }
                        editor.commit();
                        break;
                    case 3:
                        //빠른 검색
                        if (sw.isChecked()) {
                            stopService(service);
                            ClipBoardService.service.stopForeground(true);
                            ClipBoardService.service.stopSelf();
                            editor.putBoolean("fastSearch", false);
                            sw.setChecked(false);
                        } else {
                            startService(service);
                            editor.putBoolean("fastSearch", true);
                            sw.setChecked(true);
                        }
                        editor.commit();
                        break;
                    case 4:
                        //빠른 검색시 진동
                        if (sw.isChecked()) {
                            editor.putBoolean("fastSearchVibrate", false);
                            sw.setChecked(false);
                        } else {
                            editor.putBoolean("fastSearchVibrate", true);
                            sw.setChecked(true);
                        }
                        editor.commit();
                        break;
                    case 5:
                        // 개발자 정보
                        startActivity(new Intent(getApplicationContext(), DeveloperActivity.class));
                        break;
                    case 6:
                        //앱 종료
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("앱 종료")
                                .content("FastSearch를 포함한 Exchat 서비스가 완전히 종료됩니다.")
                                .neutralText("취소")
                                .positiveText("확인")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        ActivityManager am
                                                = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                                        am.restartPackage(getPackageName());
                                        stopService(service);
                                        finish();
                                    }
                                })
                                .show();
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
                //Log.e("position", position + "");
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
        loading.dismiss();
    }

    private void setSupportActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.app_name, R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.btn_navdrawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    public void updateCalc(CharSequence s) {
        String result = "0.0";
        String value = s.toString().trim();
        if (!value.isEmpty()) {
            result = utils.calculateValues(Float.parseFloat(value), previousSpinner.getSelectedItemPosition()
                    , convertSpinner.getSelectedItemPosition()) + "";
        }
        convertValue.setText(result);
    }

    private void shareText() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "오늘 " + title.get(majorFinance).split(" ")[0] + " 환율은 " +
                majorFinanceFrom.getText().toString() + " = " + majorFinanceTo.getText().toString() + " 입니다. #Exchat.");
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

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        setMajorCalc();
    }

    private void setHistoryData() {
        ExchatClipboardUtils clipboardUtils = new ExchatClipboardUtils(getApplicationContext());
        realm.beginTransaction();
        RealmResults<HistoryData> results = realm.where(HistoryData.class)
                .findAll();
        results.sort("date", Sort.DESCENDING);
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
