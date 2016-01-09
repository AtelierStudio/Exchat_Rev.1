package kr.edcan.exchat.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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

        setDefault();
        setActionBar(getSupportActionBar());
    }

    private void setDefault() {
        header = getLayoutInflater().inflate(R.layout.developer_header, null);
        footer = getLayoutInflater().inflate(R.layout.developer_footer, null);
        listView = (ListView)findViewById(R.id.developer_listview);
        arrayList = new ArrayList<>();
        arrayList.add(new DeveloperData("Exchat Version", "1.0"));
        arrayList.add(new DeveloperData("Develop Team", "Exchat team. by EDCAN"));
        adapter = new DeveloperListViewAdapter(this, arrayList);
        listView.setAdapter(adapter);
        listView.addHeaderView(header);
        listView.addFooterView(footer);
    }

    private void setActionBar(ActionBar actionBar) {
        actionBar.setTitle("About Exchat");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
