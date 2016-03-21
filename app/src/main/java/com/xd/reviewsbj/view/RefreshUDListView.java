package com.xd.reviewsbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xd.reviewsbj.R;

/**
 * Created by hhhhwei on 16/3/4.
 */
public class RefreshUDListView extends ListView {

    private Context context;

    public static final int ST_PULL = 0;
    public static final int ST_RELEASE = 1;
    public static final int ST_ING = 2;

    public RefreshUDListView(Context context) {
        this(context, null);
    }

    public RefreshUDListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshUDListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        context = getContext();
        initHeaderView();
    }

    private View headerView;
    private ImageView ivHeaderPic;
    private ProgressBar pbHeaderPic;
    private TextView tvHeaderTitle;
    private TextView tvHeaderDate;

    private int headerViewHeight;

    //初始化HeaderView及其中的控件并且先行隐藏
    public void initHeaderView() {
        headerView = View.inflate(context, R.layout.header_refresh, null);

        ivHeaderPic = (ImageView) headerView.findViewById(R.id.iv_refresh);
        pbHeaderPic = (ProgressBar) headerView.findViewById(R.id.pb_progress);
        tvHeaderTitle = (TextView) headerView.findViewById(R.id.tv_refresh_title);
        tvHeaderDate = (TextView) headerView.findViewById(R.id.tv_refresh_date);

        //测量headerView的Height
        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();

        headerView.setPadding(0, -headerViewHeight, 0, 0);
    }

    private int startY;

    //监听滑动事件，处理向下滑时HeaderView的各种状态
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                //判断,如果在滑动的过程中,就直接跳出来
                if (currentState == ST_ING)
                    break;

                int endY = (int) ev.getRawY();

                int dy = endY - startY;

                //向下滑动
                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    int padding = dy - headerViewHeight;
                    headerView.setPadding(0, padding, 0, 0);
                    //判断目前headerView的状态
                    setTheCurrentState(padding);
                }
                break;

            case MotionEvent.ACTION_UP:
                changeThePositonByCurrentState();
                break;
        }
        return super.onTouchEvent(ev);
    }

    //当前的状态
    private int currentState;

    private void setTheCurrentState(int padding) {
        if (padding < 0)
            currentState = ST_PULL;
        else
            currentState = ST_RELEASE;
    }

    //改变这个headerView的位置,通过currentState
    private void changeThePositonByCurrentState() {
        //先行设置每一个状态具有的布局
        changTheLayout();
        if (currentState == ST_PULL) {
            headerView.setPadding(0, -headerViewHeight, 0, 0);
        } else if (currentState == ST_RELEASE) {
            headerView.setPadding(0, 0, 0, 0);
            currentState = ST_ING;
            //注意
            changTheLayout();
            onRefreshListener.onRefreshUpData();
        } else {
            onRefreshListener.onRefreshUpData();
        }
    }

    //处理各个状态下具有的布局
    private void changTheLayout() {
        if (currentState == ST_PULL) {

        } else if (currentState == ST_RELEASE) {

        } else {

        }
    }

    //处理关闭的逻辑

    //处理加FootView的逻辑

    //回调接口
    public OnRefreshListener onRefreshListener;

    public interface OnRefreshListener {
        public void onRefreshUpData();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}
