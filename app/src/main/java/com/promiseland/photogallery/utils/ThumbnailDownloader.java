package com.promiseland.photogallery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 960100 on 2016/7/28.
 */
public class ThumbnailDownloader<Token> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_REQUEST = 0;
    private Handler mHandler;
    private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    public ThumbnailDownloader() {
        super(TAG);
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MESSAGE_REQUEST:
                        Token token = (Token) msg.obj;
                        handleRequest(token);
                        break;
                }
            }
        };
    }

    private void handleRequest(Token token) {
        final String url = requestMap.get(token);
        if(url == null){
            return;
        }

        try {
            byte[] data = new FlickrFetchr().getUrlBytes(url);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void queueThumbnail(Token token, String url){
        requestMap.put(token, url);

        Message message = mHandler.obtainMessage();
        message.what = MESSAGE_REQUEST;
        message.obj = token;
        message.sendToTarget();
    }
}
