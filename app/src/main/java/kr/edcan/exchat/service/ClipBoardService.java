package kr.edcan.exchat.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Cipher;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import kr.edcan.exchat.R;
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

    final static int INTENT_KEY = 1208;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        startService(new Intent(getApplicationContext(), ClipBoardService.class));
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Exchat")
                .setContentText("Exchat 서비스가 실행중입니다.");
        Intent startMain = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(startMain);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        startForeground(INTENT_KEY, builder.build());
        SharedPreferences sharedPreferences = getSharedPreferences("Exchat", 0);
        boolean fastSearch = sharedPreferences.getBoolean("fastSearch", true);
        if (fastSearch) {
            final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    if (manager.getPrimaryClipDescription().toString().contains("text")) {
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
                }
            });
        }
        return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
