package com.xinspace.csevent.ui.fragment;


import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.login.activity.LoginActivity;

/**
 * Created by Android on 2017/3/14.
 */

public class AirFragment extends Fragment {

    private View view;
    private WebView web_air;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_air , null);
        boolean isLogin = CoresunApp.isLogin;
        if (!isLogin){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        initView();
        return view;
    }

    private void initView() {
        web_air = (WebView) view.findViewById(R.id.web_air);

        WebSettings settings = web_air.getSettings();
        //设置可以使用javascript
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("gb2312");

        // 这两行代码一定加上否则效果不会出现 加入SSH
        web_air.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                ///handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed(); // 接受证书
                //handleMessage(Message msg); 其他处理
            }
        });

        //加载页面
        web_air.loadUrl("http://m.mianshui365.com");

    }
}
