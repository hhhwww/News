package com.xd.reviewsbj.domain.tab;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hhhhwei on 16/3/1.
 */
public class TabTopic extends TabBase {

    public TabTopic(Context contextBase) {
        super(contextBase);
    }

    @Override
    public View initViews() {
        TextView textView = new TextView(contextBase);
        textView.setText("2");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
