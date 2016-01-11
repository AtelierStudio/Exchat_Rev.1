package kr.edcan.exchat.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;

import kr.edcan.exchat.R;
import kr.edcan.exchat.adapter.DrawerListViewAdapter;
import kr.edcan.exchat.data.HistoryData;
import kr.edcan.exchat.service.ClipBoardService;
import kr.edcan.exchat.utils.ExchatUtils;
import kr.edcan.exchat.utils.RecycleViewAdapter;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ArrayList<String> drawerList, title, sale;
    ArrayList<HistoryData> historyDatas;
    ListView drawerMenu;
    ExchatUtils utils;

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
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        historyDatas.add(new HistoryData(1 + "", "USD", 1200 + "", "KRW"));
        historyDatas.add(new HistoryData(1+"", "USD", 1200+"", "KRW"));
        historyDatas.add(new HistoryData(1+"", "USD", 1200+"", "KRW"));
        historyDatas.add(new HistoryData(1+"", "USD", 1200+"", "KRW"));
        RecycleViewAdapter mainAdapter = new RecycleViewAdapter(getApplicationContext(), historyDatas);
        recyclerView.setAdapter(mainAdapter);

        //Utils
        utils = new ExchatUtils(getApplicationContext());
        title = utils.getCurrencyList("title");
        sale = utils.getCurrencyList("sale");

        //Widgets
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
        LinearLayout backgroundLayout  = (LinearLayout)findViewById(R.id.background_layout);
        ViewGroup.LayoutParams params = backgroundLayout.getLayoutParams();
        params.height = (int) dpHeight;
    }

    private void setSupportActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

// Sync the toggle state after onRestoreInstanceState has occurred.
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
}
