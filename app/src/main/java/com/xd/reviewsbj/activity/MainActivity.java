package com.xd.reviewsbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xd.reviewsbj.R;
import com.xd.reviewsbj.fragment.ContentFragment;
import com.xd.reviewsbj.fragment.LeftFragment;

public class MainActivity extends SlidingFragmentActivity {
    private static final String LEFT_FRAGMENT = "left_fragment";
    private static final String CONTENT_FRAGMENT = "content_fragment";
    private LeftFragment leftFragment;
    private ContentFragment contentFragment;

    private FragmentManager fragmentManager;
    private SlidingMenu slidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_sliding);
/**
 * 设置SlidingMenu
 */
        setBehindContentView(R.layout.fragment_left_sliding);
        slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(300);
        slidingMenu.setFadeDegree(0.35f);

        initViews();
        addFragments();
    }

    private void initViews() {
        leftFragment = new LeftFragment();
        contentFragment = new ContentFragment();

        fragmentManager = getSupportFragmentManager();
    }

    private void addFragments() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_left, leftFragment, LEFT_FRAGMENT);
        fragmentTransaction.replace(R.id.fl_content, contentFragment, CONTENT_FRAGMENT);
        fragmentTransaction.commit();
    }

    public LeftFragment getLeftFragment() {
        LeftFragment leftFragment = (LeftFragment) fragmentManager.findFragmentByTag(LEFT_FRAGMENT);
        return leftFragment;
    }

    public ContentFragment getContentFragment() {
        ContentFragment contentFragment = (ContentFragment) fragmentManager.findFragmentByTag(CONTENT_FRAGMENT);
        return contentFragment;
    }
}
