package com.promiseland.photogallery.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.promiseland.photogallery.bean.GalleryItem;
import com.promiseland.photogallery.utils.FlickrFetchr;

import java.util.ArrayList;

/**
 * Created by joseph on 2016/8/9.
 */
public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL = 5 * 1000;

    public PollService() {
        super(TAG);
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
            am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pendingIntent);
        }else{
            am.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
    public static boolean isServiceAlaemOn(Context context){
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if(activeNetworkInfo == null){
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String query = sp.getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
        String lastQueryId = sp.getString(FlickrFetchr.PREF_LAST_RESULT_ID, null);

        ArrayList<GalleryItem> items;
        if(query != null){
            items = new FlickrFetchr().search(query);
        }else{
            items = new FlickrFetchr().fetchItems();
        }

        if(items.size() == 0){
            return;
        }

        String id = items.get(0).getId();
        if(id.equals(lastQueryId)){
            Log.d(TAG, "get an old result " + id);
        }else{
            Log.d(TAG, "get a new result " + id);
        }

        sp.edit().putString(FlickrFetchr.PREF_LAST_RESULT_ID, id).commit();
    }
}
