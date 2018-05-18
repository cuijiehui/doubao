package com.xinspace.csevent.ui.activity;

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
import android.widget.RelativeLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;

/**
 * 首页广告web界面
 */
public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private String link;
    private LinearLayout ll_back;
    private RelativeLayout rel_data_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.activity_web_view);
        getData();
        setView();
        setSettings();
        setListener();
    }

    //设置监听
    private void setListener() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //获取上个页面传递过来的数据
    private void getData() {
        Intent intent = getIntent();
        link = intent.getStringExtra("data");

        LogUtil.i("---------www----------" + link);

    }

    private void setSettings() {
        WebSettings settings = webView.getSettings();
        //设置可以使用javascript
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("gb2312");
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // 这两行代码一定加上否则效果不会出现 加入SSH
        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                ///handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed(); // 接受证书
                //handleMessage(Message msg); 其他处理
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                rel_data_load.setVisibility(View.GONE);
            }
        });
        //加载页面
        webView.loadUrl(link);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });

    }

    //初始化组件
    private void setView() {
        webView = (WebView) findViewById(R.id.adv_webView);
        ll_back = (LinearLayout) findViewById(R.id.ll_webview_back);
        ll_back.setOnClickListener(clickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
        this.setContentView(R.layout.empty_view);
    }
}
