package com.xd.reviewsbj.domain.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by hhhhwei on 16/3/1.
 */
public class MenuHome extends MenuBase {

    public MenuHome(Context context) {
        super(context);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        tvTitleBase.setText("智慧北京");

        TextView textView = new TextView(contextBase);
        textView.setText("首页");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        setSlidingMenu(false);

        flAddViewBase.addView(textView);
    }
}
