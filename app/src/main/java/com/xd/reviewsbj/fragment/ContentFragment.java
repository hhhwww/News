package com.xd.reviewsbj.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.xd.reviewsbj.R;
import com.xd.reviewsbj.domain.menu.MenuBase;
import com.xd.reviewsbj.domain.menu.MenuHome;
import com.xd.reviewsbj.domain.menu.MenuNews;
import com.xd.reviewsbj.domain.menu.MenuSetting;
import com.xd.reviewsbj.domain.menu.MenuSmart;
import com.xd.reviewsbj.domain.menu.MenuZW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/3/1.
 * ContentFragment的布局是一个ViewPager和一个RadioGroup
 * RadioGroup的布局文件得仔细研究一下android:state_checked="true"
 *
 * 最关键的一环到了，ViewPager的每一个元素是一个页面，所以，请永远记下这个思想：
 * MenuBase是五个页面的基类，它抽取出来了那五个页面的一个共性。并且完成了都有的逻辑操作，并且将控件
 * 定义为子类可以访问的，因此，此类可以操控控件动态改变以符合自己，这些操作是在父类定义的空方法initDatas()
 * 中，并且父类提供了两个特别重要是绝对不可缺少的成员变量：
 *    protected Context contextBase;//这个用于和自己的父布局联系，不可能是孤立的
 *    public View viewBase; //这个用于把自己作为页面可以添加到ViewPager中
 *
 * 更为重要的是，考虑到子类的复杂性，很有可能自己定制更为复杂的界面，所以父布局根据情况定义了一个
 * FrameLayout，并且已经在父布局中初始化完毕，子类直接用就可以了
 *
 *
 * 事件拦截:
 * 注意ViewPager和SlidingMenu产生了事件拦截冲突
 * 解决办法：
 *  禁用ViewPager的滑动处理见NoScrollViewPager
 *      ？？？？？？？？？？？？？？
 */
public class ContentFragment extends Fragment {

    //MainActivity的Context
    private Context context;

    private ViewPager vpContent;
    private List<MenuBase> mDatas;
    private MyContentPagerAdapter myContentPagerAdapter;

    private RadioGroup rgContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        vpContent = (ViewPager) view.findViewById(R.id.vp_content);
        mDatas = new ArrayList<>();
        myContentPagerAdapter = new MyContentPagerAdapter();

        rgContent = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
        setListeners();
    }

    private void initDatas() {
        mDatas.add(new MenuHome(context));
        mDatas.add(new MenuNews(context));
        mDatas.add(new MenuSmart(context));
        mDatas.add(new MenuZW(context));
        mDatas.add(new MenuSetting(context));

        vpContent.setAdapter(myContentPagerAdapter);
/**
 * 别忘了 initDatas是由用户调用的，可以控制界面的切换
  */
        //第一次初始化
        mDatas.get(0).initDatas();
        rgContent.check(R.id.rb_home);
    }

    private void setListeners() {
/**
 * 如何跟随着ViewPager的滑动去切换页面
 * setOnPageChangeListener事件中的onPageSelected方法，调用对象的initDatas()方法操控
 * 页面，注意第一次时这个方法不会调用！
 *
  */
        vpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDatas.get(position).initDatas();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/**
 * 与ViewPager进行联动，监听setOnCheckedChangeListener事件
 */
        rgContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home:
                        vpContent.setCurrentItem(0);
                        break;

                    case R.id.rb_news:
                        vpContent.setCurrentItem(1);
                        break;

                    case R.id.rb_smart:
                        vpContent.setCurrentItem(2);
                        break;

                    case R.id.rb_zw:
                        vpContent.setCurrentItem(3);
                        break;

                    case R.id.rb_setting:
                        vpContent.setCurrentItem(4);
                        break;
                }
            }
        });
    }

    //给LeftFragment提供的MenuNews方法
    public MenuNews getMenuNews() {
        return (MenuNews) mDatas.get(1);
    }

    private class MyContentPagerAdapter extends PagerAdapter {

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
            MenuBase menuBase = mDatas.get(position);
            container.addView(menuBase.viewBase);
            return menuBase.viewBase;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
