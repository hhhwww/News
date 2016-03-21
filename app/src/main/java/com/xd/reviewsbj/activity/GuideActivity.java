package com.xd.reviewsbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xd.reviewsbj.R;
import com.xd.reviewsbj.utils.DensityUtils;
import com.xd.reviewsbj.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/3/1.
 */
public class GuideActivity extends AppCompatActivity {

    private int[] mPicIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    private ViewPager vpGuide;
    private List<ImageView> mDatas;
    private GuideViewPagerAdapter guideViewPagerAdapter;

    private LinearLayout llAddView;
    private View redView;

    private Button btSure;

    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews();
        initDatas();
        setListeners();
    }

    private void initViews() {
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        mDatas = new ArrayList<>();
        guideViewPagerAdapter = new GuideViewPagerAdapter();

        llAddView = (LinearLayout) findViewById(R.id.ll_add);
        redView = findViewById(R.id.view_red);

        btSure = (Button) findViewById(R.id.bt_sure);
    }

    private void initDatas() {
        for (int i = 0; i < mPicIds.length; i++) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
            );
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mPicIds[i]);
            imageView.setLayoutParams(layoutParams);
            mDatas.add(imageView);
        }
        vpGuide.setAdapter(guideViewPagerAdapter);
/**
 * 重点关注，动态的设置点点的位置。
 * 注意xml文件中的嵌套关系，此时最外层可以是FrameLayout也可以是RelativeLayout
 * 得到Linelayout.LayoutParams&设置宽高 -> 设置背景图片setBackgroundResource -> 判断是否是第二个小圆点，设置layoutParams.leftMargin
 * -> 添加到父布局中
 *
 *
 */
        for (int i = 0; i < mPicIds.length; i++) {
            View view = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    (int) DensityUtils.dp2px(this, 10), (int) DensityUtils.dp2px(this, 10));
            view.setBackgroundResource(R.drawable.circle_gray);
            if (i > 0) {
                layoutParams.leftMargin = (int) DensityUtils.dp2px(this, 10);
            }
            Log.e("total", layoutParams.leftMargin + "");
            view.setLayoutParams(layoutParams);
            llAddView.addView(view);
        }
/**
 * 获取小圆点的宽度，在onCreate中获取不到，所以使用下面的做法
 */
        llAddView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                width = llAddView.getChildAt(1).getLeft() - llAddView.getChildAt(0).getLeft();
                Log.e("total", width + "");
                llAddView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setListeners() {
/**
 * 其实让小圆点动很简单，思考三个问题？什么时候会动，怎么动，动多少
 * 什么时候会动？  ->>>>> ViewPager滑动的时候，所以，去监听setOnPageChangeListener事件。
 * 怎么动?     ->>>>>> 可以去设置小红点的leftMargin去完成。
 * 动多少？     ->>>>>> 基础功的问题，观察去监听setOnPageChangeListener事件中的三个方法，牢记在心！！！
  */
        vpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) redView.getLayoutParams();
                layoutParams.leftMargin = (int) (width * positionOffset + position * width);
                redView.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mPicIds.length - 1)
                    btSure.setVisibility(View.VISIBLE);
                else
                    btSure.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/**
 * 点击了按钮之后去访问SharedPreference中的值
 */
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.putBoolean(GuideActivity.this, "is_first_enter", false);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private class GuideViewPagerAdapter extends PagerAdapter {

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
            container.addView(mDatas.get(position));
            return mDatas.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
