package com.xd.reviewsbj.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by hhhhwei on 16/3/16.
 * 三级缓存
 * <p/>
 * 进入后，先从内存中读取数据，若有，设置好后直接return,若没有,
 * 在从本地读取数据，若有，设置后直接return,若没有，
 * 从网络中下载数据
 * <p/>
 * ~~内存缓存用的是LruCache
 * ~~本地缓存是存储到了sd卡中，将url经过md5转换后作为文件名，使用{
 * String fileName = MD5Utils.encouder(url);
 * File file = new File(CACHE_PATH, fileName);
 * bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
 * 获取bitmap
 * }
 * 使用{
 * String fileName = MD5Utils.encouder(url);
 * File file = new File(CACHE_PATH, fileName);
 * bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
 * 存储Bitmap
 * }
 * <p/>
 * ~~网络缓存是用AsyncTask + HttpURLConnection,获取完数据后记得存储到内存中和SD卡中，所有需要有
 * LocalCacheUtils&MemoryCacheUtils的引用{
 * //图片压缩和编码模式，节省内存
 * BitmapFactory.Options options = new BitmapFactory.Options();
 * options.inSampleSize = 2;
 * options.inPreferredConfig = Bitmap.Config.ARGB_4444;
 * bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream(), null, options);
 * }
 */
public class MyBitmapUtils {

    public NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
    }

    public void display(ImageView ivP, String url) {
        //从网络中读取,先看看本地有没有
        Bitmap bitmap = null;

        bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);

        if (bitmap != null && (ivP.getTag().toString()).equals(url)) {
            ivP.setImageBitmap(bitmap);
            Log.e("fuck", "从内存中读取的");
            return;
        }

        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if (bitmap != null && (ivP.getTag().toString()).equals(url)) {
            ivP.setImageBitmap(bitmap);
            mMemoryCacheUtils.setBitmapToMemory(url, bitmap);
            Log.e("fuck", "从本地方法中读取的");
            return;
        }

        mNetCacheUtils.getBitmapFromNet(ivP, url);
    }

}
