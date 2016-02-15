package kr.edcan.exchat.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import kr.edcan.exchat.R;

public class MainUnitSetActivity extends Activity {

    TextView textView;
    private Typeface typeface;
    private static final String FONT_NAME = "roboto_light.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unit_set);
        setDefault();

    }

    private void setDefault() {
    }

}
