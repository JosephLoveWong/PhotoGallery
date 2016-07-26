package com.promiseland.photogallery.utils;

import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlickrFetchr {
    public static final String TAG = "PhotoFetcher";

    private static final String ENDPOINT = "http://api.flickr.com/services/rest/";
    private static final String API_KEY = "2e40ba91b199589487862403593e4eb1";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String PARAM_EXTRAS = "extras";

    private static final String EXTRA_SMALL_URL = "url_s";

    private static final String XML_PHOTO = "photo";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        Log.d(TAG, "getUrlBytes : " + urlSpec);
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    
    public void fetchItems() {
        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                    .build().toString();
            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Failed to parse items", xppe);
        }
    }

//    void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws XmlPullParserException, IOException {
//        int eventType = parser.next();
//
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            if (eventType == XmlPullParser.START_TAG &&
//                XML_PHOTO.equals(parser.getName())) {
//                String id = parser.getAttributeValue(null, "id");
//                String caption = parser.getAttributeValue(null, "title");
//                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
//
//                GalleryItem item = new GalleryItem();
//                item.setId(id);
//                item.setCaption(caption);
//                item.setUrl(smallUrl);
//                items.add(item);
//            }
//
//            eventType = parser.next();
//        }
//    }
}