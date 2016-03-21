package com.xd.reviewsbj.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.xd.reviewsbj.R;
import com.xd.reviewsbj.global.GlobalConstants;

/**
 * Created by hhhhwei on 16/3/4.
 */
public class ShowNewsActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView wvNews;
    private ProgressBar pbNews;
    private ImageButton ibSize;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownews);

        initViews();
        initDatas();
        setListeners();
    }

    private void initViews() {
        wvNews = (WebView) findViewById(R.id.wv_news);
        pbNews = (ProgressBar) findViewById(R.id.pb_news);
        ibSize = (ImageButton) findViewById(R.id.ib_size);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
    }

    private void initDatas() {
        WebSettings webSettings = wvNews.getSettings();
        //支持JS,查看全文,但是出现了一直加载不完的问题，实际上加载完了
//        webSettings.setJavaScriptEnabled(true);
        //支持双击缩放
        webSettings.setUseWideViewPort(true);

        //加载前，加载中回调的url,加载后，非常重要的一个方法
        wvNews.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbNews.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbNews.setVisibility(View.INVISIBLE);
                super.onPageFinished(view, url);
            }

            //可以在此拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        //更为强大的一个回调方法,见名知意，注意参数
        wvNews.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e("ggg", "progress:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });

        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            wvNews.loadUrl(GlobalConstants.SERVER_URL + url.substring(25));
        }
    }

    private void setListeners() {
        ibSize.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_size:
                setTheDialog();
                break;

            case R.id.ib_back:
                finish();
                break;
        }
    }

    private int choosedItem = 2;

    private void setTheDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");

        String[] items = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

        builder.setSingleChoiceItems(items, choosedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choosedItem = i;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WebSettings webSettings = wvNews.getSettings();
                switch (choosedItem) {
                    case 0:
                        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;

                    case 1:
                        webSettings.setTextSize(WebSettings.TextSize.LARGER);
                        break;

                    case 2:
                        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;

                    case 3:
                        webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;

                    case 4:
                        webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                }
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }
}
