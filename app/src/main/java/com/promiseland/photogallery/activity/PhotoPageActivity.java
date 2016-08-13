package com.promiseland.photogallery.activity;

import android.support.v4.app.Fragment;

import com.promiseland.photogallery.fragment.PhotoPageFragment;

/**
 * Created by joseph on 2016/8/13.
 */
public class PhotoPageActivity extends SingleFragmentActivity {
    private static final String TAG = "PhotoPageActivity";

    @Override
    protected Fragment createFragment() {
        return new PhotoPageFragment();
    }
}
