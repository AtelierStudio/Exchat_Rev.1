package kr.edcan.exchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import kr.edcan.exchat.R;
import kr.edcan.exchat.data.HistoryData;

public class MainUnitSetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unit_set);

    }
}
