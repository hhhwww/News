package com.xd.reviewsbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hhhhwei on 16/3/16.
 */

//网络缓存的思想，没啥思想```` 注意第26行代码就好，还有一是辨别标志位tag的使用
public class NetCacheUtils {

    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils mLocalCacheUtils, MemoryCacheUtils mMemoryCacheUtils) {
        this.mLocalCacheUtils = mLocalCacheUtils;
        this.mMemoryCacheUtils = mMemoryCacheUtils;
    }

    public void getBitmapFromNet(ImageView ivP, String url) {
        new BitmpAsyncTask().execute(ivP, url);
    }

    class BitmpAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView ivPicture;
        private String url;

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            ivPicture = (ImageView) params[0];
            url = (String) params[1];
            return downloadPictureFromNet(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if ((ivPicture.getTag().toString()).equals(url)) {
                ivPicture.setImageBitmap(bitmap);

                /**
                 * 在这里，将从网络中下载的图片存入到本地和内存中去
                 */
                mLocalCacheUtils.setBitmapToLocal(url, bitmap);
                mMemoryCacheUtils.setBitmapToMemory(url, bitmap);
            }
        }
    }

    private Bitmap downloadPictureFromNet(String url) {
        HttpURLConnection httpURLConnection = null;
        Bitmap bitmap = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);

            //图片压缩和编码模式，节省内存
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;

            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream(), null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return bitmap;
    }

}
