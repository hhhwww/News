package com.xd.reviewsbj.domain.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by hhhhwei on 16/3/1.
 */
public class MenuSetting extends MenuBase {

    public MenuSetting(Context context) {
        super(context);
    }

    @Override
    public void initDatas() {
        super.initDatas();
        tvTitleBase.setText("设置");

        TextView textView = new TextView(contextBase);
        textView.setText("设置内容");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        setSlidingMenu(false);

        flAddViewBase.addView(textView);
    }
}
