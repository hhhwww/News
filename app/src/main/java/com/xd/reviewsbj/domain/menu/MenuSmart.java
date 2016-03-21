package com.xd.reviewsbj.domain.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by hhhhwei on 16/3/1.
 */
public class MenuSmart extends MenuBase {

    public MenuSmart(Context context) {
        super(context);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        tvTitleBase.setText("生活");

        TextView textView = new TextView(contextBase);
        textView.setText("生活内容");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        setSlidingMenu(true);

        flAddViewBase.addView(textView);
    }
}
