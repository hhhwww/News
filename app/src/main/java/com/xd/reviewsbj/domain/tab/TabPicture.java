package com.xd.reviewsbj.domain.tab;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xd.reviewsbj.R;
import com.xd.reviewsbj.bean.PictureData;
import com.xd.reviewsbj.global.GlobalConstants;
import com.xd.reviewsbj.utils.CacheUtils;
import com.xd.reviewsbj.utils.MyBitmapUtils;

import java.util.ArrayList;

/**
 * Created by hhhhwei on 16/3/1.
 * 标题由中转站MenuNews管理，
 * 图片的三级缓存
 * 单击按钮的布局变换
 * 传递进来的两个View
 */
public class TabPicture extends TabBase implements View.OnClickListener {

    private ListView lvTabP;
    private GridView gvTabP;

    private ImageView ivList;
    private ImageView ivGrid;

    //数据源
    private ArrayList<PictureData.NewsData> newsList;

    /**
     * 因为MenuNews负责的是标题，但是标题的title他能负责，标题两边的图标它无法负责。
     * 所以把它当做参数传递进来，进行具体的逻辑操作
     */
    public TabPicture(Context contextBase, ImageView ivList, ImageView ivGrid) {
        super(contextBase);
        this.ivList = ivList;
        this.ivGrid = ivGrid;
        setListeners();
    }

    @Override
    public View initViews() {
        View view = View.inflate(contextBase, R.layout.pager_tab_picture, null);

        lvTabP = (ListView) view.findViewById(R.id.lv_pic);
        gvTabP = (GridView) view.findViewById(R.id.gv_pic);

        initDatas();
        return view;
    }

    private void setListeners() {
        ivList.setOnClickListener(this);
        ivGrid.setOnClickListener(this);
    }

    private void initDatas() {
        String cache = CacheUtils.getCache(contextBase, GlobalConstants.PHOTO_URL, "");
        if (!TextUtils.isEmpty(cache))
            parseData(cache);
        getDataFromSever();
    }

    private void getDataFromSever() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parseData(responseInfo.result);
                CacheUtils.setCache(contextBase, GlobalConstants.PHOTO_URL, responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void parseData(String values) {
        Gson gson = new Gson();
        PictureData pictureData = gson.fromJson(values, PictureData.class);

        //数据源
        newsList = pictureData.data.news;

        if (newsList != null) {
            MyViewApapter myViewApapter = new MyViewApapter();
            lvTabP.setAdapter(myViewApapter);
            gvTabP.setAdapter(myViewApapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cccc_list:
                ivList.setVisibility(View.INVISIBLE);
                ivGrid.setVisibility(View.VISIBLE);
                lvTabP.setVisibility(View.INVISIBLE);
                gvTabP.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_cccc_grid:
                ivList.setVisibility(View.VISIBLE);
                ivGrid.setVisibility(View.INVISIBLE);
                lvTabP.setVisibility(View.VISIBLE);
                gvTabP.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private class MyViewApapter extends BaseAdapter {

//        private final BitmapUtils bitmapUtils;

        private MyBitmapUtils myBitmapUtils;

        public MyViewApapter() {
//            bitmapUtils = new BitmapUtils(contextBase);
//            bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
            myBitmapUtils = new MyBitmapUtils();
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
                myViewHolder = new MyViewHolder();
                view = View.inflate(contextBase, R.layout.item_pager_tab_picture, null);
                myViewHolder.ivP = (ImageView) view.findViewById(R.id.iv_p);
                myViewHolder.tvD = (TextView) view.findViewById(R.id.tv_d);
                view.setTag(myViewHolder);
            } else
                myViewHolder = (MyViewHolder) view.getTag();
            String s = GlobalConstants.SERVER_URL + newsList.get(i).listimage.substring(25);

            ImageView imageView = myViewHolder.ivP;
            imageView.setImageResource(R.drawable.topnews_item_default);
            imageView.setTag(s);

            myBitmapUtils.display(imageView, s);

            myViewHolder.tvD.setText(newsList.get(i).title);
            return view;
        }

        private class MyViewHolder {
            public ImageView ivP;
            public TextView tvD;
        }
    }
}
