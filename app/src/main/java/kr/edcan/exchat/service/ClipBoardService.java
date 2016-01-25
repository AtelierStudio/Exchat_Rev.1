package kr.edcan.exchat.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Cipher;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import kr.edcan.exchat.activity.ClipboardPopupViewActivity;
import kr.edcan.exchat.activity.MainActivity;
import kr.edcan.exchat.data.ClipBoardData;
import kr.edcan.exchat.data.HistoryData;
import kr.edcan.exchat.utils.ExchatClipboardUtils;
import kr.edcan.exchat.utils.ExchatUtils;

/**
 * Created by Junseok on 2016. 1. 10..
 */
public class ClipBoardService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences("Exchat", 0);
        boolean fastSearch = sharedPreferences.getBoolean("fastSearch", true);
        if (fastSearch) {
            final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    String capturedString = manager.getPrimaryClip().getItemAt(0).getText().toString();
                    ExchatClipboardUtils utils = new ExchatClipboardUtils(getApplicationContext());
                    ClipBoardData clipData = utils.getResult(capturedString);
                    if (clipData != null) {
                        startActivity(new Intent(getApplicationContext(), ClipboardPopupViewActivity.class)
                                .putExtra("unit", clipData.getUnit())
                                .putExtra("value", clipData.getValue())
                                .putExtra("convertValue", clipData.getConvertValue())
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
                        Realm realm = Realm.getInstance(getApplicationContext());
                        realm.beginTransaction();
                        HistoryData data = realm.createObject(HistoryData.class);
                        Date date = new Date(System.currentTimeMillis());
                        data.setConvertUnit(0);
                        data.setPrevUnit(clipData.getUnit());
                        data.setConvertValue(clipData.getConvertValue());
                        data.setPrevValue(clipData.getValue());
                        data.setDate(date);
                        realm.commitTransaction();
                    }
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
