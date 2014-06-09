package com.leoxiong.Smile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Leo on 6/11/2014.
 */
public class ModifyAlarmActivity extends Activity {
    private static final String PREFERENS_ALARMS = "alarms";

    private TimePicker mTimePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_alarm_activity);

        mTimePicker = (TimePicker) findViewById(R.id.timePicker_alarm);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_alarm_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}