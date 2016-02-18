package kr.edcan.exchat.service;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.Date;

import io.realm.Realm;
import kr.edcan.exchat.R;
import kr.edcan.exchat.activity.ClipboardPopupViewActivity;
import kr.edcan.exchat.activity.MainActivity;
import kr.edcan.exchat.data.ClipBoardData;
import kr.edcan.exchat.data.HistoryData;
import kr.edcan.exchat.utils.ExchatClipboardUtils;
import kr.edcan.exchat.utils.ExchatTextView;
import kr.edcan.exchat.utils.KillProcess;

/**
 * Created by Junseok on 2016. 1. 10..
 */
public class ClipBoardService extends Service {

    final static int INTENT_KEY = 1208;
    Intent startMain;
    MaterialDialog materialDialog;
    public static Service service;
    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TaskStackBuilder killbuild = TaskStackBuilder.create(this);
        killbuild.addNextIntent(new Intent(getApplicationContext(), KillProcess.class));
        PendingIntent killProcess = killbuild.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_status_running)
                .setContentTitle("Exchat")
                .setContentText("Exchat 서비스가 실행중입니다.")
                .addAction(R.drawable.btn_status_deleteapp, "종료", killProcess);
        startMain = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(startMain);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        startForeground(INTENT_KEY, builder.build());
        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                final SharedPreferences sharedPreferences = getSharedPreferences("Exchat", 0);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                final boolean fastSearch = sharedPreferences.getBoolean("fastSearch", true);
                if (fastSearch) {

                    if (System.currentTimeMillis() - sharedPreferences.getLong("lastFastSearchTime", System.currentTimeMillis() - 201) > 200) {
                        if (manager.getPrimaryClipDescription().toString().contains("text")) {
                            String capturedString = manager.getPrimaryClip().getItemAt(0).getText().toString();
                            ExchatClipboardUtils utils = new ExchatClipboardUtils(getApplicationContext());
                            ClipBoardData clipData = utils.getResult(capturedString);
                            if (clipData != null) {
                                if (sharedPreferences.getBoolean("fastSearchVibrate", true)) {
                                    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    vb.vibrate(500);
                                }
                                materialDialog = new MaterialDialog.Builder(getBaseContext())
                                        .customView(getView(clipData), false)
                                        .theme(Theme.LIGHT)
                                        .build();
                                materialDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                WindowManager.LayoutParams wmlp = materialDialog.getWindow().getAttributes();
                                wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                                materialDialog.show();

                                Realm realm = Realm.getInstance(getApplicationContext());
                                realm.beginTransaction();
                                HistoryData data = realm.createObject(HistoryData.class);
                                Date date = new Date(System.currentTimeMillis());
                                data.setConvertUnit(0);
                                data.setPrevUnit(clipData.getUnit());
                                data.setConvertValue(clipData.getConvertValue());
                                data.setPrevValue(clipData.getValue());
                                data.setDate(date);
                                //Log.e("asdf", "saved " + clipData.getConvertValue());
                                realm.commitTransaction();
                                editor.putLong("lastFastSearchTime", System.currentTimeMillis());
                                editor.commit();
                            }
                        }
                    }  //else Log.e("asdf", "crashed cuz 200ms");
                }
            }
        });
        return START_NOT_STICKY;
    }

    private View getView(ClipBoardData clipData) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_clipboard_popup_view, null);
        final ExchatTextView prev, result, share, launch;
        prev = (ExchatTextView) view.findViewById(R.id.dialog_prev);
        result = (ExchatTextView) view.findViewById(R.id.dialog_result);
        share = (ExchatTextView) view.findViewById(R.id.popup_share);
        launch = (ExchatTextView) view.findViewById(R.id.popup_launch);
        ExchatClipboardUtils utils = new ExchatClipboardUtils(getApplicationContext());
        ClipBoardData data = new ClipBoardData(clipData.getUnit(), clipData.getValue(), clipData.getConvertValue());
        prev.setText(data.getValue() + " " + utils.foreignmoneyUnits[data.getUnit()]);
        result.setText(data.getConvertValue() + " " + "KRW");
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, prev.getText().toString() + " = " + result.getText().toString() + "입니다. #Exchat.");
                materialDialog.dismiss();
                startActivity(Intent.createChooser(sharingIntent, "환율 정보 공유").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                startActivity(startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return view;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
