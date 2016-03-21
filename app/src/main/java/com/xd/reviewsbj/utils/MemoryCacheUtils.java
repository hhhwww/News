package com.xd.reviewsbj.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by hhhhwei on 16/3/16.
 */
public class MemoryCacheUtils {

    //第二层解决方案，终极,LruCache
    private LruCache<String, Bitmap> mCaches;

    public MemoryCacheUtils() {
        int memory = (int) (Runtime.getRuntime().maxMemory() / 8);
        mCaches = new LruCache<String, Bitmap>(memory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public Bitmap getBitmapFromMemory(String url) {
        return mCaches.get(url);
    }

    public void setBitmapToMemory(String url, Bitmap bitmap) {
        mCaches.put(url, bitmap);
    }

}
