package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.GroupFailBean;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * 我的拼团详情页
 *
 * Created by Android on 2017/6/12.
 */

public class MyCroupDetailAct extends BaseActivity{

    private Intent intent;
    private GroupFailBean bean;
    private WebView web_group_detail;
    private LinearLayout ll_group_detail_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_my_group_detail);

        intent = getIntent();
        if (intent != null){
            bean = (GroupFailBean) intent.getSerializableExtra("bean");
        }
        initView();
    }

    private void initView() {
        ll_group_detail_back = (LinearLayout) findViewById(R.id.ll_group_detail_back);
        ll_group_detail_back.setOnClickListener(onClickListener);

        web_group_detail = (WebView) findViewById(R.id.web_group_detail);

        web_group_detail.getSettings().setUseWideViewPort(true);
        web_group_detail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web_group_detail.getSettings().setLoadWithOverviewMode(true);
        web_group_detail.setOverScrollMode(View.SCROLL_AXIS_NONE);
        web_group_detail.getSettings().setJavaScriptEnabled(true);
        web_group_detail.getSettings().setDomStorageEnabled(true);
        web_group_detail.getSettings().setAllowFileAccess(true);
        web_group_detail.getSettings().setAppCacheEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            web_group_detail.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        web_group_detail.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                ///handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed(); // 接受证书
                //handleMessage(Message msg); 其他处理
            }
        });

        LogUtil.i("url" + bean.getUrl());
        web_group_detail.loadUrl(bean.getUrl());
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_group_detail_back:
                    MyCroupDetailAct.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        onClickListener = null;
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
