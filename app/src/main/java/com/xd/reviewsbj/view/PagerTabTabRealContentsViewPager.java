package com.xd.reviewsbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by hhhhwei on 16/3/3.
 */
public class PagerTabTabRealContentsViewPager extends ViewPager {
    public PagerTabTabRealContentsViewPager(Context context) {
        super(context);
    }

    public PagerTabTabRealContentsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int startX;
    private int startY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 这句话的含义这样理解：你在所有情况下都别拦截我，当且仅当：
                 * 1.我向右滑，并且getCurrentItem = 0
                 * 2.我向左滑，并且getCurrentItem = count - 1;
                 */
                getParent().requestDisallowInterceptTouchEvent(true);

                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                Log.e("cc", "onTouchEvent~ACTION_DOWN");

                break;

            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {//左右滑动
                    if (endX - startX > 0) {//右
                        if (getCurrentItem() == 0)
                            getParent().requestDisallowInterceptTouchEvent(false);
//                        getParent().requestDisallowInterceptTouchEvent(true);很强
                    } else {//左
                        if (getCurrentItem() == getAdapter().getCount() - 1)
                            getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {

                }
                Log.e("cc", "onTouchEvent~ACTION_MOVE");
                break;
        }
        return super.onTouchEvent(ev);
    }

}
