package com.leoxiong.Smile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Leo on 6/8/2014.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startActivity(new Intent(context, OpenCVActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
