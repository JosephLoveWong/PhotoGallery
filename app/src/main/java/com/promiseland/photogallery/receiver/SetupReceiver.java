package com.promiseland.photogallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.promiseland.photogallery.service.PollService;

/**
 * Created by joseph on 2016/8/13.
 */
public class SetupReceiver extends BroadcastReceiver {
    private static final String TAG = "SetupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "SetupReceiver onReceive");

        PollService.setServiceAlarm(context, true);
    }
}
