package com.xd.reviewsbj.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xd.reviewsbj.R;
import com.xd.reviewsbj.activity.MainActivity;
import com.xd.reviewsbj.bean.NewsData;
import com.xd.reviewsbj.domain.menu.MenuNews;

import java.util.ArrayList;

/**
 * Created by hhhhwei on 16/3/1.
 * 文字被点击后变色，并且要保持状态android:state_enabled="true"
 */
public class LeftFragment extends Fragment {

    private ListView lvLeftFragment;
    private ArrayList<NewsData.NewsLeftData> mDatas;
    private MyViewAdapter myViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        lvLeftFragment = (ListView) view.findViewById(R.id.lv_fragment_left);
        myViewAdapter = new MyViewAdapter();
        return view;
    }

    //从MenuNews中给LeftFragment赋值
    public void setDatas(ArrayList<NewsData.NewsLeftData> mDatas) {
        this.mDatas = mDatas;

        lvLeftFragment.setAdapter(myViewAdapter);

        setListener();
    }

    private int currentPositon;

    private void setListener() {
        lvLeftFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentPositon = i;
                toggle();
                changeMenuNews(i);
                myViewAdapter.notifyDataSetChanged();
            }
        });
    }

    //通过点击侧边栏的ListView而去改变MenuNews中的title和flAddView
    private void changeMenuNews(int position) {
        MainActivity mainActivity = (MainActivity) getActivity();
        ContentFragment contentFragment = mainActivity.getContentFragment();
        MenuNews menuNews = contentFragment.getMenuNews();
        menuNews.setCurrentPager(position);
    }

    private void toggle() {
        MainActivity mainActivity = (MainActivity) getActivity();
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }

    private class MyViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(getContext(), R.layout.item_fragment_left, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title_item);
            tvTitle.setText(mDatas.get(i).title);

            /**
             * 设置文字的颜色变换,重要的是还需要保持状态
             */
            if (currentPositon == i)
                tvTitle.setEnabled(true);
            return view;
        }
    }
}
