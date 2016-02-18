package kr.edcan.exchat.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

import kr.edcan.exchat.R;
import kr.edcan.exchat.adapter.DeveloperListViewAdapter;
import kr.edcan.exchat.data.DeveloperData;

public class DeveloperActivity extends AppCompatActivity {

    ListView listView;
    View header, footer;
    DeveloperListViewAdapter adapter;
    ArrayList<DeveloperData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        try {
            setDefault();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setActionBar(getSupportActionBar());

    }

    private void setDefault() throws PackageManager.NameNotFoundException {
        header = getLayoutInflater().inflate(R.layout.developer_header, null);
        footer = getLayoutInflater().inflate(R.layout.developer_footer, null);
        listView = (ListView) findViewById(R.id.developer_listview);
        arrayList = new ArrayList<>();
        arrayList.add(new DeveloperData("Version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        arrayList.add(new DeveloperData("User Support", "kotohana5706@edcan.kr"));
        arrayList.add(new DeveloperData("Homepage", "edcan.kr"));
        arrayList.add(new DeveloperData("Main Programmer", "KOTOHANA (malang.moe)"));
        arrayList.add(new DeveloperData("Main UI Designer", "Luminon (itsuka.me)"));
        arrayList.add(new DeveloperData("Thanks to", "LNTCS"));
        adapter = new DeveloperListViewAdapter(this, arrayList);
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("position", position + "");
                switch (position) {
                    case 1:
                        break;
                    case 2:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:kotohana5706@edcan.kr"));
                        startActivity(Intent.createChooser(emailIntent, "이메일 전송"));
                        break;
                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://edcan.kr")));
                        break;
                    case 4:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://malang.moe")));
                        break;
                    case 5:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://itsuka.me")));
                        break;
                    case 6:
                        break;
                }
            }
        });
    }

    private void setActionBar(ActionBar actionBar) {
        actionBar.setTitle("About Exchat");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}