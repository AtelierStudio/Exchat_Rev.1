package kr.edcan.exchat.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Junseok on 2016. 1. 10..
 */
public class ClipBoardService extends Service {

    @Override
    public void onCreate() {
        Log.e("clip", "create");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ClipboardManager manager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Log.e("clip", manager.getPrimaryClip().getItemAt(0).getText().toString());
                String capturedString = manager.getPrimaryClip().getItemAt(0).getText().toString();
                if (capturedString.contains("엔")) {
//                    Log.e("clip", "Converted String : "+getNumFromString(capturedString));
//                    200엔
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
