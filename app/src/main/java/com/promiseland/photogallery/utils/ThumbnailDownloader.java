package com.promiseland.photogallery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.promiseland.photogallery.listener.ThumbnailDownloaderListener;

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
    private Handler mResponseHandler;
    private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    private ThumbnailDownloaderListener mListener;

    public ThumbnailDownloader(Handler handler) {
        super(TAG);
        mResponseHandler = handler;
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

    private void handleRequest(final Token token) {
        final String url = requestMap.get(token);
        if(url == null){
            return;
        }

        try {
            byte[] data = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            String curUrl = requestMap.get(token);
            if(!url.equals(curUrl)){
                return;
            }
            requestMap.remove(token);
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mListener != null){
                        mListener.onThumbnailDownloaded(token, bitmap);
                    }
                }
            });

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

    public void setListener(ThumbnailDownloaderListener listener){
        mListener = listener;
    }

    public void cleanQueue(){
        mHandler.removeMessages(MESSAGE_REQUEST);
        requestMap.clear();
    }
}
