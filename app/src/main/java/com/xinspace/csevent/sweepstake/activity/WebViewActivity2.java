package com.xinspace.csevent.sweepstake.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * web界面
 */
public class WebViewActivity2 extends BaseActivity {
    private WebView webView;
    private String link;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view2);
        getData();
        setView();
        setSettings();
        setListener();
    }

    //设置监听
    private void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
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
    }

    private void setSettings() {
        WebSettings settings = webView.getSettings();
        //设置可以使用javascript
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("gb2312");

        // 这两行代码一定加上否则效果不会出现 加入SSH
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                ///handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed(); // 接受证书
                //handleMessage(Message msg); 其他处理

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
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(WebViewActivity2.this, OpenDoorActivity.class);
//            startActivity(intent);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(WebViewActivity2.this, OpenDoorActivity.class);
//        startActivity(intent);
        finish();
    }
}
