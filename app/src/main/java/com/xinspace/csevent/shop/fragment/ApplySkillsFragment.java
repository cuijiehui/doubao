package com.xinspace.csevent.shop.fragment;

import android.net.http.SslError;
import android.os.Build;
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
import com.xinspace.csevent.util.LogUtil;

/**
 * 试用技巧
 * Created by Android on 2017/6/14.
 */

public class ApplySkillsFragment extends Fragment{

    private View view;
    private WebView web_apply_skill;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fra_apply_skill , null);
        initView();
        return view;
    }

    private void initView() {

        web_apply_skill = (WebView) view.findViewById(R.id.web_apply_skill);
        web_apply_skill.getSettings().setUseWideViewPort(true);
        web_apply_skill.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web_apply_skill.getSettings().setLoadWithOverviewMode(true);
        web_apply_skill.setOverScrollMode(View.SCROLL_AXIS_NONE);
        web_apply_skill.getSettings().setJavaScriptEnabled(true);
        web_apply_skill.getSettings().setDomStorageEnabled(true);
        web_apply_skill.getSettings().setAllowFileAccess(true);
        web_apply_skill.getSettings().setAppCacheEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            web_apply_skill.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        web_apply_skill.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                ///handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed(); // 接受证书
                //handleMessage(Message msg); 其他处理
            }
        });

        String url = "https://wx.szshide.shop/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.skill.apply";

        //http://wx.szshide.shop/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.skill.apply



        LogUtil.i("url" + url);
        web_apply_skill.loadUrl(url);

    }

}
