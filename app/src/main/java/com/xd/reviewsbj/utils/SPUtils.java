package com.xd.reviewsbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hhhhwei on 16/3/1.
 */
public class SPUtils {

    public static boolean getBoolean(Context context, String name, boolean defau) {
        SharedPreferences sp = context.getSharedPreferences("myconfig", Context.MODE_PRIVATE);
        return sp.getBoolean(name, defau);
    }

    public static void putBoolean(Context context, String name, boolean values) {
        SharedPreferences sp = context.getSharedPreferences("myconfig", Context.MODE_PRIVATE);
        sp.edit().putBoolean(name, values).commit();
    }

    public static String getString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("myconfig", Context.MODE_PRIVATE);
        return sp.getString(key, value);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("myconfig", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
}
