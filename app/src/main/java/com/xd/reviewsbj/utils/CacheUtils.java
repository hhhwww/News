package com.xd.reviewsbj.utils;

import android.content.Context;

/**
 * Created by hhhhwei on 16/3/4.
 */
public class CacheUtils {

    //以url作为key,原始的json数据作为values
    public static void setCache(Context context, String key, String values) {
        SPUtils.putString(context, key, values);
    }

    public static String getCache(Context context, String key, String value) {
        return SPUtils.getString(context, key, value);
    }
}
