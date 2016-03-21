package com.xd.reviewsbj.domain.tab;

import android.content.Context;
import android.view.View;

/**
 * Created by hhhhwei on 16/3/2.
 * 标题的变化是由MenuNews来控制的，所以，界面没有什么共性
 * 但是还要定义两个在最终要的变量
 * public View viewBase;
 * public Context contextBase;
 */
public abstract class TabBase {

    public View viewBase;
    public Context contextBase;

    public TabBase(Context contextBase) {
        this.contextBase = contextBase;
        viewBase = initViews();
    }

    public abstract View initViews();
}
