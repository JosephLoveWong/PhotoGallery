package com.promiseland.photogallery.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.promiseland.photogallery.service.PollService;

/**
 * Created by joseph on 2016/8/13.
 */
public class VisibleFragment extends Fragment {
    private static final String TAG = "VisibleFragment";

    private class ShowNotification extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "后台数据更新", Toast.LENGTH_SHORT).show();
            setResultCode(Activity.RESULT_CANCELED);
        }
    }

    private ShowNotification mReceiver = new ShowNotification();
    private IntentFilter mIntentFilter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver,  mIntentFilter, PollService.PERM_PRIVATE, null);
    }
}
