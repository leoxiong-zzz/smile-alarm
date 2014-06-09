package com.leoxiong.Smile;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends Activity {
    private static final String TAG = "AlarmActivity";
    private Preferences mPrefs;
    private ListView mListViewAlarms;
    private AlarmAdapter mAlarmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms_activity);

        mPrefs = Preferences.getInstance(this);
        mListViewAlarms = (ListView) findViewById(R.id.listView_alarms);
        ArrayList<Alarm> alarms = mPrefs.get(Preferences.ALARMS, new TypeToken<ArrayList<Alarm>>() {
        });
        if (alarms == null) alarms = new ArrayList<Alarm>();
        mAlarmAdapter = new AlarmAdapter(this, alarms);
        mListViewAlarms.setAdapter(mAlarmAdapter);
        mListViewAlarms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "click");
                mAlarmAdapter.remove(mAlarmAdapter.getItem(i));
            }
        });
        mPrefs.remove(Preferences.ALARMS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarms_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.alarms_activity_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        ArrayList<Alarm> alarms = mPrefs.get(Preferences.ALARMS, new TypeToken<ArrayList<Alarm>>() {
                        });
                        if (alarms == null) alarms = new ArrayList<Alarm>();
                        Alarm alarm = new Alarm(hourOfDay, minute, null, true);
                        alarms.add(alarm);
                        mAlarmAdapter.add(alarm);
                        mPrefs.put(Preferences.ALARMS, alarms);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
                return true;
            case R.id.test_alarm:
                startActivity(new Intent(this, OpenCVActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
