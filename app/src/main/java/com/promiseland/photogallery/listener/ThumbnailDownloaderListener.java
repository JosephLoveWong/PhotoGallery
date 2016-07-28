package com.promiseland.photogallery.listener;

import android.graphics.Bitmap;

/**
 * Created by joseph on 2016/7/28.
 */
public interface ThumbnailDownloaderListener<Token> {
    public static final String TAG = "ThumbnailDownloaderListener";

    void onThumbnailDownloaded(Token token, Bitmap bitmap);
}
