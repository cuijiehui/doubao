package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 试用详情
 * <p>
 * Created by Android on 2017/6/16.
 */

public class TrialDetailAct extends BaseActivity {

    private Intent intent;
    private WebView web_trial_detail;
    private LinearLayout ll_trial_detail_back;
    private TextView tv_apply_trial;
    private String gid;
    private SDPreference preference;
    private String openId;
    private String state;
    private String url;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    ToastUtil.makeToast("已申请");
                    tv_apply_trial.setText("已申请");
                    tv_apply_trial.setBackgroundColor(Color.parseColor("#9f9f9f"));
                    break;
                case 400:
                    ToastUtil.makeToast((String) msg.obj);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(TrialDetailAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_trial_detail);

        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
        intent = getIntent();
        if (intent != null) {
            gid = intent.getStringExtra("id");
            state = intent.getStringExtra("success");
            url = intent.getStringExtra("url");
        }

        LogUtil.i("TrialDetailAct----state" + state);


        initView();
    }

    private void initView() {
        ll_trial_detail_back = (LinearLayout) findViewById(R.id.ll_trial_detail_back);
        ll_trial_detail_back.setOnClickListener(onClickListener);

        tv_apply_trial = (TextView) findViewById(R.id.tv_apply_trial);
        tv_apply_trial.setOnClickListener(onClickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        if (state != null && !state.equals("")) {
            if (state.equals("0")) {
                tv_apply_trial.setText("申请中");
            } else if (state.equals("1")) {
                tv_apply_trial.setText("申请成功");
            } else if (state.equals("-1")) {
                tv_apply_trial.setText("申请失败");
            }
            tv_apply_trial.setBackgroundColor(Color.parseColor("#9f9f9f"));
        } else {
            tv_apply_trial.setText("免费试用");
            tv_apply_trial.setBackgroundColor(Color.parseColor("#ea5205"));
        }

        web_trial_detail = (WebView) findViewById(R.id.web_trial_detail);

        web_trial_detail.getSettings().setUseWideViewPort(true);
        web_trial_detail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web_trial_detail.getSettings().setLoadWithOverviewMode(true);
        web_trial_detail.setOverScrollMode(View.SCROLL_AXIS_NONE);
        web_trial_detail.getSettings().setJavaScriptEnabled(true);
        web_trial_detail.getSettings().setDomStorageEnabled(true);
        web_trial_detail.getSettings().setAllowFileAccess(true);
        web_trial_detail.getSettings().setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            web_trial_detail.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        web_trial_detail.setWebViewClient(new WebViewClient() {
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

        LogUtil.i("url" + url);
        web_trial_detail.loadUrl(url);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_trial_detail_back:
                    TrialDetailAct.this.finish();
                    break;
                case R.id.tv_apply_trial:
                    if (state != null && !state.equals("")) {

                        ToastUtil.makeToast("申请中");

                    } else {
                        applyData();
                    }
                    break;
            }
        }
    };

    private void applyData() {
        GetDataBiz.applyTryData(openId, gid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("申请试用返回" + result);
                if (result == null || result.equals("")) {
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")) {
                    handler.obtainMessage(200).sendToTarget();
                } else {
                    handler.obtainMessage(400, jsonObject.getString("message")).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        onClickListener = null;
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
