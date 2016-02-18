package kr.edcan.exchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import kr.edcan.exchat.R;
import kr.edcan.exchat.data.ClipBoardData;
import kr.edcan.exchat.data.HistoryData;
import kr.edcan.exchat.utils.ExchatClipboardUtils;
import kr.edcan.exchat.utils.ExchatTextView;

public class ClipboardPopupViewActivity extends Activity {

    Intent intent;

    ExchatTextView prev, result, share, launch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clipboard_popup_view);
        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        setDefault();
    }

    private void setDefault() {
        intent = getIntent();

    }

    private void shareText() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, prev.getText().toString() + " = " + result.getText().toString() + "입니다. #Exchat.");
        startActivity(Intent.createChooser(sharingIntent, "환율 정보 공유"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        finish();
    }
}
