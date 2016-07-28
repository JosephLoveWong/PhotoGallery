package com.promiseland.photogallery.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.promiseland.photogallery.R;
import com.promiseland.photogallery.bean.GalleryItem;
import com.promiseland.photogallery.utils.FlickrFetchr;

import java.util.ArrayList;

/**
 * Created by joseph on 2016/7/26.
 */
public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        return rootView;
    }

    private void setupAdapter(){
        if(getActivity() == null || mGridView == null){
            return;
        }
        if(mItems != null){
            mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(), android.R.layout.simple_gallery_item, mItems));
        } else {
            mGridView.setAdapter(null);
        }
    }

    private class FetchItemsTask extends AsyncTask<String, Integer, ArrayList<GalleryItem>>{// 输入类型，进度更新类型，返回值类型

        @Override
        protected ArrayList<GalleryItem> doInBackground(String... params) {
            ArrayList<GalleryItem> items = new FlickrFetchr().fetchItems();
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setupAdapter();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
