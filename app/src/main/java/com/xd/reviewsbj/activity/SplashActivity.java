package com.xd.reviewsbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.xd.reviewsbj.R;
import com.xd.reviewsbj.utils.SPUtils;

/**
 * 添加动画
 * 进入下一页的逻辑，先从SharedPreference中读取存储的数据，判断是否进入下一页
 */

public class SplashActivity extends AppCompatActivity {
    private ImageView ivSplashPic;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化控件
        initViews();
        //添加动画
        addAnimations();
        //进入下一个页面的逻辑
        enterNextPage();
    }

    private void initViews() {
        ivSplashPic = (ImageView) findViewById(R.id.iv_splash);
    }

    /**
     * 添加动画
     */
    private void addAnimations() {
        animationSet = new AnimationSet(false);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);

        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        ivSplashPic.startAnimation(animationSet);
    }

    /**
     * 进入下一页的逻辑，先从SharedPreference中读取存储的数据，判断是否进入下一页
     */
    private void enterNextPage() {
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean is_first_enter = SPUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
                if (is_first_enter) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
