package com.xd.reviewsbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by hhhhwei on 16/3/16.
 * 本地缓存的思想，把url经过md5变换作为文件名，然后把图片存入（在SD卡中）
 */
public class LocalCacheUtils {

    public static final String CACHE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/zhbj_cache";

    public Bitmap getBitmapFromLocal(String url) {
        Bitmap bitmap = null;
        try {
            String fileName = MD5Utils.encouder(url);
            File file = new File(CACHE_PATH, fileName);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void setBitmapToLocal(String url, Bitmap bitmap) {
        String fileName = MD5Utils.encouder(url);
        File file = new File(CACHE_PATH, fileName);

        //判断文件夹是否存在
        File parentFile = file.getParentFile();
        //如果不存在则需要创建
        //mkdir,只能在已经存在的目录中创建创建文件夹。
        //mkdirs()可以在不存在的目录中创建文件夹。
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
