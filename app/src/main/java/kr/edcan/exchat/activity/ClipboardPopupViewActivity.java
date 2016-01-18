package kr.edcan.exchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import kr.edcan.exchat.R;
import kr.edcan.exchat.data.ClipBoardData;
import kr.edcan.exchat.data.HistoryData;

public class ClipboardPopupViewActivity extends Activity {

    Intent intent;

    TextView prev, result;
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
        prev = (TextView) findViewById(R.id.dialog_prev);
        result = (TextView) findViewById(R.id.dialog_result);

        ClipBoardData data = new ClipBoardData(intent.getStringExtra("unit"),
                intent.getFloatExtra("value", (float) 0.0), intent.getFloatExtra("convertValue", (float) 0.0));

        prev.setText(data.getValue() + " " + data.getUnit());
        result.setText(data.getConvertValue() + " " + "원");
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
