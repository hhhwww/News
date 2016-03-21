package com.xd.reviewsbj.domain.tabtab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;
import com.xd.reviewsbj.R;
import com.xd.reviewsbj.activity.MainActivity;
import com.xd.reviewsbj.activity.ShowNewsActivity;
import com.xd.reviewsbj.bean.NewsData;
import com.xd.reviewsbj.bean.RealContentsData;
import com.xd.reviewsbj.global.GlobalConstants;
import com.xd.reviewsbj.utils.CacheUtils;
import com.xd.reviewsbj.utils.SPUtils;
import com.xd.reviewsbj.view.PagerTabTabRealContentsViewPager;
import com.xd.reviewsbj.view.RefreshListView;

import java.util.ArrayList;

/**
 * Created by hhhhwei on 16/3/2.
 * 用的最多的是，直接把JSON数据存储到SP中
 */
public class TabTabRealContents {
    public View viewReal;
    public Context contextReal;

    private PagerTabTabRealContentsViewPager vpReal;
    private TextView tvTitleReal;
    private CirclePageIndicator circlePageIndicator;

    private String url;
    private NewsData.NewsTabData newsTabData;
    private RealContentsData realContentsData;

    //ViewPager的数据源
    private ArrayList<RealContentsData.RealTopNewsData> topnews;
    //ListView的数据源
    private ArrayList<RealContentsData.RealNewsData> newsList;

    private RefreshListView lvReal;
    private MyViewAdapter myViewAdapter;
    private String moreUrl;


    //    一个Handler构成轮播条效果
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int current = vpReal.getCurrentItem();
            if (current == topnews.size() - 1) {
                current = 0;
            } else
                current++;
            vpReal.setCurrentItem(current);
            //循环开始
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    public TabTabRealContents(Context contextReal, NewsData.NewsTabData newsTabData) {
        this.contextReal = contextReal;
        this.newsTabData = newsTabData;
        url = GlobalConstants.SERVER_URL + newsTabData.url;
        viewReal = initViews();

        handler.sendEmptyMessageDelayed(0, 3000);

    }

    private View initViews() {
        View view = View.inflate(contextReal, R.layout.pager_tab_tab_real_contents, null);
        View headerView = View.inflate(contextReal, R.layout.header_pager_tab, null);

        vpReal = (PagerTabTabRealContentsViewPager) headerView.findViewById(R.id.vp_real);
        tvTitleReal = (TextView) headerView.findViewById(R.id.tv_title_real);
        circlePageIndicator = (CirclePageIndicator) headerView.findViewById(R.id.cpi_real);

        lvReal = (RefreshListView) view.findViewById(R.id.lv_real);

        lvReal.addHeaderView(headerView);

        //缓存逻辑
        String cache = CacheUtils.getCache(contextReal, url, "");
        if (!TextUtils.isEmpty(cache))
            parseData(cache, false);
        getDataFromSever(url);

        //下拉刷新从服务器重新获取的逻辑 && 加载更多的逻辑
        lvReal.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                String cache = CacheUtils.getCache(contextReal, url, "");
                if (!TextUtils.isEmpty(cache))
                    parseData(cache, false);
                getDataFromSever(url);
            }

            @Override
            public void onRefreshingMore() {
                String cache = CacheUtils.getCache(contextReal
                        , GlobalConstants.SERVER_URL + moreUrl, "");
                if (!TextUtils.isEmpty(cache))
                    parseData(cache, false);
                getMoreDataFromSever(GlobalConstants.SERVER_URL + moreUrl);
            }
        });

        //设置各种监听器
        setListeners();
        return view;
    }

    //给ListView的item项设置是否为已读的状态
    private void setListeners() {
        lvReal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String read_ids = SPUtils.getString(contextReal, "read_ids", "");
                String id = newsList.get(i).id;
                if (!read_ids.contains(id)) {
                    read_ids = read_ids + id + ",";
                    SPUtils.putString(contextReal, "read_ids", read_ids);
                }
                setTheCheckedState(view);
                Intent intent = new Intent(contextReal, ShowNewsActivity.class);
                intent.putExtra("url", newsList.get(i).url);
                contextReal.startActivity(intent);

            }
        });
    }

    //实现局部刷新,这个view就是被点击的item对象
    private void setTheCheckedState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_real_title);
        tvTitle.setTextColor(Color.GRAY);
    }

    private void getMoreDataFromSever(final String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parseData(responseInfo.result, true);
                CacheUtils.getCache(contextReal, url, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                lvReal.recoverTheFootState();
            }
        });
    }

    private void getDataFromSever(final String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parseData(responseInfo.result, false);
                CacheUtils.setCache(contextReal, url, responseInfo.result);

                //模拟数据睡眠
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        MainActivity mainActivity = (MainActivity) contextReal;
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lvReal.recoverTheState();
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("total", s);
                lvReal.recoverTheState();
            }
        });
    }

    private void parseData(String result, boolean isLoadingMore) {
        Gson gson = new Gson();
        realContentsData = gson.fromJson(result, RealContentsData.class);

        moreUrl = realContentsData.data.more;
        myViewAdapter = new MyViewAdapter();

        if (!isLoadingMore) {
            //赋值给ListView的数据源
            newsList = realContentsData.data.news;

            //赋值给ViewPager的数据源
            topnews = realContentsData.data.topnews;
            lvReal.setAdapter(myViewAdapter);
        } else {
            ArrayList<RealContentsData.RealNewsData> newsMore = realContentsData.data.news;
            newsList.addAll(newsMore);
            lvReal.recoverTheFootState();
            myViewAdapter.notifyDataSetChanged();
            lvReal.setSelection(myViewAdapter.getCount() - 1);
        }

        vpReal.setAdapter(new MyPagerAdapter());
        circlePageIndicator.setViewPager(vpReal);
        circlePageIndicator.setSnap(true);

        //第一次初始化
        tvTitleReal.setText(topnews.get(0).title);


        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitleReal.setText(topnews.get(position).title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {

        //BitmapUtils自动缓存了
        private final BitmapUtils bitmapUtils;

        public MyPagerAdapter() {
            bitmapUtils = new BitmapUtils(contextReal);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(contextReal);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            RealContentsData.RealTopNewsData realTopNewsData = topnews.get(position);
            bitmapUtils.display(imageView, GlobalConstants.SERVER_URL
                    + realTopNewsData.topimage.substring(25));
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private boolean isPlaying = true;

    private class MyViewAdapter extends BaseAdapter {

        private final BitmapUtils bitmapUtils;

        public MyViewAdapter() {
            bitmapUtils = new BitmapUtils(contextReal);
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int i) {
            return newsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder myViewHolder = null;
            if (view == null) {
                view = View.inflate(contextReal, R.layout.item_tab_real, null);
                myViewHolder = new MyViewHolder();
                myViewHolder.ivPic = (ImageView) view.findViewById(R.id.iv_real_pic);
                myViewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_real_title);
                myViewHolder.tvDate = (TextView) view.findViewById(R.id.tv_real_date);

                view.setTag(myViewHolder);
            } else
                myViewHolder = (MyViewHolder) view.getTag();

            ImageView ivPic = myViewHolder.ivPic;
            bitmapUtils.display(ivPic, GlobalConstants.SERVER_URL
                    + newsList.get(i).listimage.substring(25));

            myViewHolder.tvTitle.setText(newsList.get(i).title);
            myViewHolder.tvDate.setText(newsList.get(i).pubdate);

            //设置被点击的item特殊的效果
            String read_ids = SPUtils.getString(contextReal, "read_ids", "");
            if (read_ids.contains(newsList.get(i).id))
                myViewHolder.tvTitle.setTextColor(Color.GRAY);
            return view;
        }

        private class MyViewHolder {
            public ImageView ivPic;
            public TextView tvTitle;
            public TextView tvDate;
        }
    }
}
