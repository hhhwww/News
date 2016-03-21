package com.xd.reviewsbj.domain.tab;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.xd.reviewsbj.R;
import com.xd.reviewsbj.activity.MainActivity;
import com.xd.reviewsbj.bean.NewsData;
import com.xd.reviewsbj.domain.tabtab.TabTabRealContents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/3/1.
 * 第一层ViewPager和SlidingMenu的解决方案,先把SlidingMenu设置为不可用，监听ViewPager的滑动事件
 * ，当到第一页是，在让SlidingMenu可以出来
 */
public class TabNews extends TabBase {

    private ImageButton ibNext;
    private TabPageIndicator tpiIndictor;

    private ViewPager vpTabNews;
    private List<TabTabRealContents> mDatas;

    ArrayList<NewsData.NewsTabData> children;

    public TabNews(Context contextBase, ArrayList<NewsData.NewsTabData> children) {
        super(contextBase);
        //initViews()在这里都执行完了，注意在父类构造函数中调用的方法的执行顺序
        this.children = children;
        initDatas();
    }

    //
    @Override
    public View initViews() {
        View view = View.inflate(contextBase, R.layout.pager_tab_news, null);

        ibNext = (ImageButton) view.findViewById(R.id.ib_next_page);
        tpiIndictor = (TabPageIndicator) view.findViewById(R.id.tpi);
        vpTabNews = (ViewPager) view.findViewById(R.id.vp_pager_tab_news);
        mDatas = new ArrayList<>();

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = vpTabNews.getCurrentItem();
                vpTabNews.setCurrentItem(++currentItem);
            }
        });
        /**
         * TabPageIndicator和ViewPager组合时，监听事件监听TabPageIndicator的。
         */
        tpiIndictor.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity activity = (MainActivity) contextBase;
                SlidingMenu slidingMenu = activity.getSlidingMenu();
                if (position == 0)
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                else
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void initDatas() {
        //真正的页面展示
        for (int i = 0; i < children.size(); i++)
            mDatas.add(new TabTabRealContents(contextBase, children.get(i)));

        vpTabNews.setAdapter(new MyViewPager());
//设置TabIndicator的一句代码
        tpiIndictor.setViewPager(vpTabNews);
    }

    private class MyViewPager extends PagerAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabTabRealContents tabTabRealContents = mDatas.get(position);
            container.addView(tabTabRealContents.viewReal);
            return tabTabRealContents.viewReal;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).title;
        }
    }
}
