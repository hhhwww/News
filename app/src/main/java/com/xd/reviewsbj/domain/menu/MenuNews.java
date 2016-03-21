package com.xd.reviewsbj.domain.menu;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xd.reviewsbj.activity.MainActivity;
import com.xd.reviewsbj.bean.NewsData;
import com.xd.reviewsbj.domain.tab.TabBase;
import com.xd.reviewsbj.domain.tab.TabComm;
import com.xd.reviewsbj.domain.tab.TabNews;
import com.xd.reviewsbj.domain.tab.TabPicture;
import com.xd.reviewsbj.domain.tab.TabTopic;
import com.xd.reviewsbj.fragment.LeftFragment;
import com.xd.reviewsbj.global.GlobalConstants;
import com.xd.reviewsbj.utils.CacheUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/3/1.
 * 此界面并不是一个现实数据的界面
 * 而是一个过渡界面，用于交互
 * 当侧边栏点到哪个tab时,这个类负责切换到相应的tab中
 *
 *
 * 采用了本地缓存，因为是一个整体的东西，采取的是SharedPreference缓存，url作为了key~~~~~~!!
 */
public class MenuNews extends MenuBase {

    //重要的数据
    private NewsData newsData;
    private List<TabBase> mDatas;

    public MenuNews(Context context) {
        super(context);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        setSlidingMenu(true);

        //先从缓存中读取一些内容
        String cache = CacheUtils.getCache(contextBase, GlobalConstants.CATEGORIES_URL, "");
        if (!TextUtils.isEmpty(cache))
            parseData(cache);
        //从后台默默的看有没有更新
        getDatasFromSever();
    }

    private void getDatasFromSever() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORIES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parseData(responseInfo.result);
                CacheUtils.setCache(contextBase, GlobalConstants.CATEGORIES_URL, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("hw", s);
                e.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        newsData = gson.fromJson(result, NewsData.class);
        setNewsData(newsData);

        mDatas = new ArrayList<>();

        /**
         * 这个children是indicator上的指示的文字
         */
        ArrayList<NewsData.NewsTabData> children = newsData.data.get(0).children;
        mDatas.add(new TabNews(contextBase, children));
        mDatas.add(new TabTopic(contextBase));
        mDatas.add(new TabPicture(contextBase,ivCCCCCList,ivCCCCCGrid));
        mDatas.add(new TabComm(contextBase));

        /**
         * 永远也别忘了初始化界面
         */
        setCurrentPager(0);
    }

    /**
     * 交互！！！   -》》》》》》LeftFagment
     * 和LeftFragment进行交互,利用Activity
     * @param newsData
     */
    private void setNewsData(NewsData newsData) {
        MainActivity mainActivity = (MainActivity) contextBase;
        LeftFragment leftFragment = mainActivity.getLeftFragment();
        leftFragment.setDatas(newsData.data);
    }

    /**
     * 交互   LeftFragment ------->>>>>>
     * @param position
     */
    public void setCurrentPager(int position) {
        if (position == 2) {
            ivCCCCCList.setVisibility(View.VISIBLE);
        } else {
            ivCCCCCList.setVisibility(View.INVISIBLE);
            ivCCCCCGrid.setVisibility(View.INVISIBLE);
        }

        tvTitleBase.setText(newsData.data.get(position).title);

        flAddViewBase.removeAllViews();

        flAddViewBase.addView(mDatas.get(position).viewBase);
    }
}
