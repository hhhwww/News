package com.xd.reviewsbj.domain.menu;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xd.reviewsbj.R;
import com.xd.reviewsbj.activity.MainActivity;

/**
 * Created by hhhhwei on 16/3/1.
 */
public class MenuBase {
    protected Context contextBase;
    public View viewBase;

    protected TextView tvTitleBase;
    public ImageView ivCCCCCGrid;
    public ImageView ivCCCCCList;
    protected ImageButton ibMenuBase;

    protected FrameLayout flAddViewBase;


    public MenuBase(Context context) {
        contextBase = context;
        viewBase = initView();
    }

    private View initView() {
        View view = View.inflate(contextBase, R.layout.base_menu, null);
        tvTitleBase = (TextView) view.findViewById(R.id.tv_title_base);
        ibMenuBase = (ImageButton) view.findViewById(R.id.ib_menu_base);
        flAddViewBase = (FrameLayout) view.findViewById(R.id.fl_add_view_base);

        ivCCCCCList = (ImageView) view.findViewById(R.id.iv_cccc_list);
        ivCCCCCGrid = (ImageView) view.findViewById(R.id.iv_cccc_grid);

        ibMenuBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        return view;
    }

    public void initDatas() {

    }

    public void setSlidingMenu(boolean isExist) {
        SlidingMenu slidingMenu = ((MainActivity) contextBase).getSlidingMenu();
        if (isExist) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            ibMenuBase.setVisibility(View.VISIBLE);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            ibMenuBase.setVisibility(View.INVISIBLE);
        }
    }

    private void toggle() {
        SlidingMenu slidingMenu = ((MainActivity) contextBase).getSlidingMenu();
        slidingMenu.toggle();
    }
}
