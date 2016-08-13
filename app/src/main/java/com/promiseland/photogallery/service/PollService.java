package com.promiseland.photogallery.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.promiseland.photogallery.R;
import com.promiseland.photogallery.activity.PhotoGalleryActivity;
import com.promiseland.photogallery.bean.GalleryItem;
import com.promiseland.photogallery.utils.FlickrFetchr;

import java.util.ArrayList;

/**
 * Created by joseph on 2016/8/9.
 */
public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL = 5 * 1000;
    public static final String EXTRA_REQUEST_CODE = "REQUEST_CODE";
    public static final String EXTRA_NOTIFICATION = "NOTIFICATION";

    public static final String ACTION_SHOW_NOTIFICATION = "com.promiseland.photogallery.action.showNotification";
    public static final String PERM_PRIVATE = "com.promiseland.photogallery.PRIVATE";

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
    public static boolean isServiceAlarmOn(Context context){
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(EXTRA_REQUEST_CODE, requestCode);
        i.putExtra(EXTRA_NOTIFICATION, notification);

        sendOrderedBroadcast(i, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
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
            Resources resources = getResources();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, PhotoGalleryActivity.class),0);
            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            showBackgroundNotification(0, notification);
        }

        sp.edit().putString(FlickrFetchr.PREF_LAST_RESULT_ID, id).commit();
    }
}
