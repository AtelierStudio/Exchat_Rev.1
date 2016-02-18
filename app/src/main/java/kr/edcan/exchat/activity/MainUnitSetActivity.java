package kr.edcan.exchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.edcan.exchat.R;
import kr.edcan.exchat.data.FinanceCalcData;
import kr.edcan.exchat.data.HistoryData;

public class MainUnitSetActivity extends Activity {

    Realm realm;
    ArrayList<String> titleArr;
    TextView title, set;
    private Typeface typeface;
    private static final String FONT_NAME = "roboto_light.ttf";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(this);
        setContentView(R.layout.tuto_4);
        setDefault();
        setSpinner();
    }

    private void setSpinner() {
        final Spinner spinner = (Spinner)findViewById(R.id.tuto_4_spinner);
        if(titleArr.size()!=0){
            SpinnerAdapter units = new ArrayAdapter<String>(MainUnitSetActivity.this, R.layout.spinner_tutorial_textstyle, titleArr);
            spinner.setAdapter(units);
        }
        spinner.setSelection(sharedPreferences.getInt("mainUnit", 1));
        set.setText("설정");
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItemPosition()!=0) {
                    editor.putInt("mainUnit", spinner.getSelectedItemPosition());
                    editor.commit();
                    finish();
                }
                else Snackbar.make(v, "주요 환율로 KRW는 설정하실 수 없습니다", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setDefault() {
        sharedPreferences = getSharedPreferences("Exchat", 0);
        editor = sharedPreferences.edit();
        title = (TextView) findViewById(R.id.tuto_4_title);
        set = (TextView) findViewById(R.id.tutorial_page_next);
        title.setText("주요 환율 설정");
        titleArr = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<FinanceCalcData> realmResults = realm.where(FinanceCalcData.class).findAll();
        realm.commitTransaction();
        for(FinanceCalcData data : realmResults){
            titleArr.add(data.getContentTitle());
        }
    }
}
