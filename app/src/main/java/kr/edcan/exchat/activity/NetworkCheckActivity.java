package kr.edcan.exchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import kr.edcan.exchat.R;
import kr.edcan.exchat.utils.ExchatUtils;

public class NetworkCheckActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_check);
        TextView networkSettings = (TextView) findViewById(R.id.no_internet_finish);
        networkSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(new ExchatUtils().isNetworkAvailable(getApplicationContext())){
            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
            finish();
        }
    }
}
