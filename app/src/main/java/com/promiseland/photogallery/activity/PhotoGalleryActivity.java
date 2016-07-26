package com.promiseland.photogallery.activity;

import android.support.v4.app.Fragment;

import com.promiseland.photogallery.fragment.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PhotoGalleryFragment();
    }
}
