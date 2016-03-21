package com.xd.reviewsbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.reviewsbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hhhhwei on 16/3/3.
 * 一个可以上拉刷新，下拉加载更多的ListView
 */
public class RefreshListView extends ListView implements AdapterView.OnItemClickListener {

    private static final int STATE_PULL_REFRESH = 0;
    private static final int STATE_RELEASE_REFRESH = 1;
    private static final int STATE_REFRESHING = 2;

    private int currentState;

    private int measuredHeight;

    private View view;

    private ImageView ivRefresh;
    private ProgressBar pbRefresh;
    private TextView tvTitle;
    private TextView tvDate;

    private RotateAnimation rotateAnimationDown;
    private RotateAnimation rotateAnimationUp;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArrowAnm();
        view = View.inflate(getContext(), R.layout.header_refresh, null);

        ivRefresh = (ImageView) view.findViewById(R.id.iv_refresh);
        pbRefresh = (ProgressBar) view.findViewById(R.id.pb_progress);
        tvTitle = (TextView) view.findViewById(R.id.tv_refresh_title);
        tvDate = (TextView) view.findViewById(R.id.tv_refresh_date);
        initHeaderView();
        initFootView();
    }

    private void initHeaderView() {
        this.addHeaderView(view);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                measuredHeight = view.getMeasuredHeight();
                view.setPadding(0, -measuredHeight, 0, 0);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private int startY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (currentState == STATE_REFRESHING)
                    break;

                int endY = (int) ev.getRawY();

                int dy = endY - startY;
                int padding = dy - measuredHeight;

                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    view.setPadding(0, padding, 0, 0);
                    if (padding > 0 && currentState != STATE_RELEASE_REFRESH)
                        currentState = STATE_RELEASE_REFRESH;
                    else if (padding <= 0 && currentState != STATE_PULL_REFRESH)
                        currentState = STATE_PULL_REFRESH;
                    refreshState(currentState);
                    return true;//什么叫避免被别的事件拦截
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentState == STATE_RELEASE_REFRESH) {
                    currentState = STATE_REFRESHING;
                    view.setPadding(0, 0, 0, 0);
                    refreshState(currentState);
                } else if (currentState == STATE_PULL_REFRESH) {
                    view.setPadding(0, -measuredHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshState(int currentState) {
        switch (currentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivRefresh.setVisibility(VISIBLE);
                pbRefresh.setVisibility(INVISIBLE);
                ivRefresh.startAnimation(rotateAnimationDown);
                break;

            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivRefresh.setVisibility(VISIBLE);
                pbRefresh.setVisibility(INVISIBLE);
                ivRefresh.clearAnimation();
                break;

            case STATE_REFRESHING:
                tvTitle.setText("正在刷新");
                ivRefresh.setVisibility(INVISIBLE);
                pbRefresh.setVisibility(VISIBLE);
                ivRefresh.startAnimation(rotateAnimationUp);
                onRefreshListener.onRefreshing();
                setCurrentTime();
                break;
        }
    }

    private void initArrowAnm() {
        rotateAnimationDown = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationDown.setDuration(200);
        rotateAnimationDown.setFillAfter(true);

        rotateAnimationUp = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationDown.setDuration(200);
        rotateAnimationDown.setFillAfter(true);
    }

    public OnRefreshListener onRefreshListener;

    public interface OnRefreshListener {
        public void onRefreshing();

        public void onRefreshingMore();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    //恢复状态
    public void recoverTheState() {
        if (currentState == STATE_REFRESHING) {
            currentState = STATE_PULL_REFRESH;
            ivRefresh.setVisibility(VISIBLE);
            pbRefresh.setVisibility(INVISIBLE);

            view.setPadding(0, -measuredHeight, 0, 0);
        }
    }

    //设置刷新时间
    public void setCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvDate.setText("最后刷新时间:" + simpleDateFormat.format(new Date()));
    }

    /**
     * 加载脚布局
     */

    private View mFootView;
    private int footViewHeight;

    private boolean isLoadingMore = false;

    private void initFootView() {
        mFootView = View.inflate(getContext(), R.layout.footer_refresh, null);
        this.addFooterView(mFootView);

        mFootView.measure(0, 0);
        footViewHeight = mFootView.getMeasuredHeight();

        mFootView.setPadding(0, -footViewHeight, 0, 0);

        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (!isLoadingMore) {
                    if ((i == SCROLL_STATE_IDLE || i == SCROLL_STATE_FLING) && getLastVisiblePosition()
                            == getCount() - 1) {
                        mFootView.setPadding(0, 0, 0, 0);
                        if (onRefreshListener != null) {
                            onRefreshListener.onRefreshingMore();
                        }
                        isLoadingMore = true;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    public void recoverTheFootState() {
        if (isLoadingMore) {
            Toast.makeText(getContext(), "已经到达最后一页", Toast.LENGTH_SHORT).show();
            mFootView.setPadding(0, -footViewHeight, 0, 0);
            isLoadingMore = false;
        }
    }

    //自定义的OnItemClickListener
    private OnItemClickListener onItemClickListener;

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        this.onItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        onItemClickListener.onItemClick(adapterView, view, i - getHeaderViewsCount(), l);
    }
}
