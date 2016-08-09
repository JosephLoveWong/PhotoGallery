package com.promiseland.photogallery.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by joseph on 2016/8/9.
 */
public class PollService extends IntentService {
    private static final String TAG = "PollService";

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
    }
}
