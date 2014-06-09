package com.leoxiong.Smile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Leo on 6/14/2014.
 */
public class AlarmAdapter extends ArrayAdapter<Alarm> {
    private static final int LAYOUT_RESOURCE = R.layout.alarm_item;

    public AlarmAdapter(Context context, List<Alarm> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(LAYOUT_RESOURCE, parent, false);
        }
        Alarm alarm = getItem(position);
        ((TextView) convertView.findViewById(R.id.textView_hour)).setText(Integer.toString(alarm.getHour() % 12));
        ((TextView) convertView.findViewById(R.id.textView_minute)).setText(String.format("%02d", alarm.getMinute()));
        ((TextView) convertView.findViewById(R.id.textView_meridiem)).setText(alarm.getHour() >= 12 ? R.string.post_meridiem : R.string.ante_meridiem);
        ((Switch) convertView.findViewById(R.id.switch_enabled)).setChecked(alarm.isEnabled());
        return convertView;
    }
}
