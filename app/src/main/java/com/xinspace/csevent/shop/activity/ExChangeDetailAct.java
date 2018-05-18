package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.shop.modle.ExGoodsBean;
import com.xinspace.csevent.shop.weiget.StandardPop;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * 积分兑换详情
 * Created by Android on 2017/6/16.
 */

public class ExChangeDetailAct extends BaseActivity{

    private Intent intent;
    private WebView web_convert_detail;
    private LinearLayout ll_convert_detail_back;
    private TextView tv_convert;
    private String gid;
    private SDPreference preference;
    private String openId;
    private ExGoodsBean bean;
    private String hasoption;
    private StandardPop standardPop;
    private int myIntegral;
    private int goodsIntegral;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    if (msg.obj != null) {

                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(ExChangeDetailAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_exchange_detail);

        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
        intent = getIntent();
        if (intent != null){
            gid = intent.getStringExtra("id");
            bean = (ExGoodsBean) intent.getSerializableExtra("bean");
            myIntegral = intent.getIntExtra("integral" , 0);
            hasoption = bean.getHasoption();
            goodsIntegral = Integer.valueOf(bean.getCredit());
        }
        initView();
    }

    private void initView() {
        ll_convert_detail_back = (LinearLayout) findViewById(R.id.ll_convert_detail_back);
        ll_convert_detail_back.setOnClickListener(onClickListener);

        tv_convert = (TextView) findViewById(R.id.tv_convert);
        tv_convert.setOnClickListener(onClickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        if (myIntegral > goodsIntegral){

            tv_convert.setText("立即兑换");
            tv_convert.setBackgroundColor(Color.parseColor("#ea5205"));

        }else{

            tv_convert.setText("积分不足");
            tv_convert.setBackgroundColor(Color.parseColor("#9f9f9f"));

        }

        web_convert_detail = (WebView) findViewById(R.id.web_convert_detail);

        web_convert_detail.getSettings().setUseWideViewPort(true);
        web_convert_detail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web_convert_detail.getSettings().setLoadWithOverviewMode(true);
        web_convert_detail.setOverScrollMode(View.SCROLL_AXIS_NONE);
        web_convert_detail.getSettings().setJavaScriptEnabled(true);
        web_convert_detail.getSettings().setDomStorageEnabled(true);
        web_convert_detail.getSettings().setAllowFileAccess(true);
        web_convert_detail.getSettings().setAppCacheEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            web_convert_detail.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        web_convert_detail.setWebViewClient(new WebViewClient() {
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

        LogUtil.i("url" + bean.getDetail_url());
        web_convert_detail.loadUrl(bean.getDetail_url());
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_convert_detail_back:
                    finish();
                    break;
                case R.id.tv_convert://立即兑换

                    if (myIntegral > goodsIntegral){
                        //1：表示有规格选项，要选规格 0：表示不用选规格
                        if (hasoption.equals("0")){
                            Intent intent = new Intent(ExChangeDetailAct.this , BuyExChangeAct.class);
                            intent.putExtra("bean" , bean);
                            startActivity(intent);
                        }else if (hasoption.equals("1")){
                            LogUtil.i("选规格 选规格");
                            showBuyGoodsPop();
                        }
                    }else{
                        ToastUtil.makeToast("您的积分不足");
                        return;
                    }
                    break;
            }
        }
    };

    private void applyData(){
//        GetDataBiz.applyTryData(openId, gid, new HttpRequestListener() {
//            @Override
//            public void onHttpRequestFinish(String result) throws JSONException {
//                LogUtil.i("申请试用返回" + result);
//                JSONObject jsonObject = new JSONObject(result);
//                if (jsonObject.getString("message").equals("success")){
//                    handler.obtainMessage(200).sendToTarget();
//                }
//                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
//            }
//
//            @Override
//            public void onHttpRequestError(String error) {
//
//            }
//        });
    }



    /**
     * 买东西选择规格
     *
     */
    private void showBuyGoodsPop(){

        if (standardPop == null) {
            standardPop = new StandardPop(ExChangeDetailAct.this , bean , openId);
            standardPop.setOnDismissListener(dismissListener);
        }

        if (!standardPop.isShowing()) {
            standardPop.showAtLocation(tv_convert, Gravity.BOTTOM, 0, 0);
            standardPop.isShowing();
        } else {
            standardPop.dismiss();
        }
    }


    PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            standardPop = null;
        }
    };

    @Override
    protected void onDestroy() {
        onClickListener = null;
        dismissListener = null;
        if (standardPop != null){
            standardPop.onDestory();
        }
        handler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        super.onDestroy();
    }
}
