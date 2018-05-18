package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.alipay.PayResult;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.GetOrderBean;
import com.xinspace.csevent.sweepstake.activity.PayOrderAct;
import com.xinspace.csevent.sweepstake.modle.AlipayInfo;
import com.xinspace.csevent.sweepstake.modle.FirstEvent;
import com.xinspace.csevent.sweepstake.modle.WeChatPayInfo;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.util.CheckApp;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.ToastUtils;
import com.xinspace.csevent.util.WXPayUtil2;
import com.xinspace.csevent.util.WXPayUtil3;
import com.xinspace.csevent.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.xinspace.csevent.wxapi.Constants.ALIPAY_SUCCESSED;

/**
 * 支付页面
 *
 *  type：0   正常产品支付
 *  type: 1   积分产品支付
 *  type:2    团购产品支付
 * <p>
 * Created by Android on 2017/5/10.
 */

public class PayOrderActivity extends BaseActivity {

    private static final String PAY_TYPE_ALIPAY = "2";
    private static final String PAY_TYPE_WECHAT = "1";
    private static final String PAY_TYPE_CARD = "3";

    private String orderId;
    private SDPreference preference;
    private String openId;
    private String teamId;
    private int type;

    private String ordersn;
    private String price;
    private GetOrderBean getOrderBean;

    private RelativeLayout rel_chat;
    private TextView tv_pay_bg;
    private RelativeLayout rel_alipay;
    private RelativeLayout rel_yinlian;
    //WXPayUtil2 wxpay ;
    private int codeResult;

    private static final int WX_PAY_SUCCESS = 1;
    private static final int WX_PAY_ERROR = 2;
    private static final int ALIPAY_STATUS= 3;

    private boolean isClickWChact = false;
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case WX_PAY_ERROR:
                    ToastUtil.makeToast("支付失败");
                    EventBus.getDefault().post(new FirstEvent("3"));
                    PayOrderActivity.this.finish();
                    break;
                case WX_PAY_SUCCESS:
                    ToastUtil.makeToast("支付成功");
                    EventBus.getDefault().post(new FirstEvent("4"));
                    Intent intent1 = new Intent(PayOrderActivity.this, StoreOrderAct.class);
                    intent1.putExtra("flag" , "0");
                    startActivity(intent1);
                    PayOrderActivity.this.finish();
                    break;

                case ALIPAY_STATUS:
                    PayResult result = new PayResult((Map<String, String>) msg.obj);
                    String resultStatus = result.getResultStatus();
                    String resultinfo = result.getResult();
                    if (TextUtils.equals(resultStatus, ALIPAY_SUCCESSED)){
                        EventBus.getDefault().post(new FirstEvent("4"));
                        switch (type){
                            case 0://进入正常订单详情
                                Intent normalIntent = new Intent(PayOrderActivity.this, StoreOrderAct.class);
                                normalIntent.putExtra("flag" , "0");
                                startActivity(normalIntent);
                                break;

                            case 1://进入积分购物详情
                                Intent exChangIntent = new Intent(PayOrderActivity.this, ConvertRecordAct.class);
                                startActivity(exChangIntent);

                                break;

                            case 2://进入团购详情
                                Intent groupIntent = new Intent(PayOrderActivity.this, MyGroupAct.class);
                                startActivity(groupIntent);
                                break;
                        }
                        LogUtil.e("支付成功");
                        ToastUtil.makeToast("支付成功");
                        PayOrderActivity.this.finish();
                    }else{
                        LogUtil.e("resultCode:" + resultStatus + "\nresultInfo:" + resultinfo);
                        ToastUtil.makeToast("支付失败");
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(PayOrderActivity.this , R.color.app_bottom_color);
        setContentView(R.layout.act_pay_goods);
        ToastUtils.init(this);

        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
        orderId = getIntent().getStringExtra("orderId");
        teamId = getIntent().getStringExtra("teamId");
        type = getIntent().getIntExtra("type", 0);

        initView();
        initData(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeResult = WXPayEntryActivity.codeResult;
        Log.i("www", "onResume   code" + codeResult);
        if (isClickWChact){
            if (codeResult != 0){
                if (codeResult == 1) {
                    // 微信支付成功,检测支付成功接口
                    handler.obtainMessage(WX_PAY_SUCCESS).sendToTarget();
                } else {
                    // 微信支付失败
                    handler.obtainMessage(WX_PAY_ERROR).sendToTarget();
                }
            }
        }
    }

    private void initData(int type) {

        switch (type){

            //正常订单
            case 0:
                GetDataBiz.getOrderData(orderId, openId, new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        LogUtil.i("获取订单返回的结果" + result + "\nThread >> " + Thread.currentThread().getName());
                        if (result == null || result.equals("")) return;
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("200")) {
                            JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                            GetOrderBean getOrderBean = gson.fromJson(dataJsonObject.toString(), GetOrderBean.class);
                            ordersn = getOrderBean.getOrdersn();
                            price = getOrderBean.getPrice();
                        }
                    }

                    @Override
                    public void onHttpRequestError(String error) {
                        ToastUtil.makeToast("网络连接失败");
                    }
                });
                break;
        }

    }

    private void initView() {
        rel_chat = (RelativeLayout) findViewById(R.id.rel_chat);
        rel_chat.setOnClickListener(onClickListener);

        rel_alipay = (RelativeLayout) findViewById(R.id.rel_alipay);
        rel_alipay.setOnClickListener(onClickListener);

        tv_pay_bg = (TextView) findViewById(R.id.tv_pay_bg);
        tv_pay_bg.setOnClickListener(onClickListener);

        rel_yinlian = (RelativeLayout) findViewById(R.id.rel_yinlian);
        rel_yinlian.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_chat:

                    switch (type){

                        //正常购
                        case 0:
                            createWeachatPay();//发起微信支付
                            break;

                        //积分购
                        case 1:
                            getExChangeOrderInfo(PAY_TYPE_WECHAT);
                            break;

                        //团购
                        case 2:
                            createGroupBuyWechatPay();
                            break;
                    }

                    break;
                case R.id.rel_alipay:
                    switch (type){
                        case 0:
                            getAlipayOrderInfo();
                            break;

                        case 1:
                            getExChangeOrderInfo(PAY_TYPE_ALIPAY);
                            break;

                        case 2:
                            getGroupOrderInfo();
                            break;

                    }

                    break;


                case R.id.rel_yinlian:
                    ToastUtil.makeToast("此支付方式暂时无法使用");
                    break;
                case R.id.tv_pay_bg:
                    finish();
                    break;
            }
        }
    };

    /**
     * 团购支付宝支付
     */
    private void getGroupOrderInfo() {
        GetDataBiz.getGroupAlipayOrderInfo(teamId, orderId, openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.e("获取团购支付宝订单 result" + result);
                createAlipay(result);
            }

            @Override
            public void onHttpRequestError(String error) {
                ToastUtil.makeToast("网络出错啦");
            }
        });
    }

    /**
     * 发起微信支付
     */
    private void createWeachatPay() {
        if (CheckApp.isWeixinAvilible(PayOrderActivity.this)) {
            isClickWChact = true;
            WXPayUtil2 wxpay = new WXPayUtil2(PayOrderActivity.this);
            wxpay.pay(ordersn, price);
        } else {
            ToastUtil.makeToast("您未安装微信");
        }
    }

    private void createGroupBuyWechatPay(){
        if (CheckApp.isWeixinAvilible(PayOrderActivity.this)) {
            isClickWChact = true;
            WXPayUtil3 wxpay = new WXPayUtil3(PayOrderActivity.this);
            wxpay.pay(openId, orderId, teamId);
        } else {
            ToastUtil.makeToast("您未安装微信");
        }
    }


    private void getExChangeOrderInfo(final String payType) {
        GetDataBiz.getExChangePayInfo(orderId, openId, payType, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.e("获取积分订单 result" + result);
                if (payType.equals(PAY_TYPE_ALIPAY)){
                    createAlipay(result);
                }
                else if (payType.equals(PAY_TYPE_WECHAT)){
                    setWeChatPayInfo(result);
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    /**
     * 设置积分商城微信支付信息
     * @param result
     */
    private void setWeChatPayInfo(String result) {
        LogUtil.e("微信订单数据 result --> " + result);
        WeChatPayInfo info = gson.fromJson(result, WeChatPayInfo.class);
        if (info.getCode().equals("200")){
            new WXPayUtil2(PayOrderActivity.this).payExChange(info);
        }
    }

    private void getAlipayOrderInfo() {
        GetDataBiz.getAlipayInfo(orderId, openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.e("获取支付宝订单 result" + result);
                createAlipay(result);
            }
            @Override
            public void onHttpRequestError(String error) {

            }
        });

    }

    private void createAlipay(String result) {
        AlipayInfo alipayInfo = gson.fromJson(result, AlipayInfo.class);
        if (TextUtils.equals(alipayInfo.getCode(), "200")){
            final String info = alipayInfo.getData().getOrderinfo();
            if (TextUtils.equals(info, "400")) return;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(PayOrderActivity.this);
                    LogUtil.e("current alipay version -->" + payTask.getVersion());
                    Map<String, String> result = payTask.payV2(info, true);
                    handler.obtainMessage(ALIPAY_STATUS, result).sendToTarget();
                }
            };
            Thread payThread = new Thread(runnable);
            payThread.start();
        }
        else{
            ToastUtils.showToast("获取订单失败");
        }
    }


    @Override
    protected void onDestroy() {
        onClickListener = null;
        handler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        isClickWChact = false;
        System.gc();
        super.onDestroy();
    }

}
