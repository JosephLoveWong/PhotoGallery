package com.promiseland.photogallery.receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.promiseland.photogallery.service.PollService;

/**
 * Created by joseph on 2016/8/13.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(getResultCode() != Activity.RESULT_OK){
            return;
        }

        int requestCode = intent.getIntExtra(PollService.EXTRA_REQUEST_CODE, Activity.RESULT_OK);
        Notification notification = intent.getParcelableExtra(PollService.EXTRA_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, notification);

    }
}
