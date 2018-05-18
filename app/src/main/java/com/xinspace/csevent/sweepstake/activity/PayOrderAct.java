package com.xinspace.csevent.sweepstake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xinspace.csevent.R;
import com.xinspace.csevent.alipay.PayResult;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.biz.GetProfileBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.data.entity.ProfileDataEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.sweepstake.modle.OrderNumBean;

import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.util.CheckApp;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.WXPayUtil2;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.wxapi.Constants;
import com.xinspace.csevent.wxapi.WXPayEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by Android on 2016/10/10.
 * <p/>
 * 众筹全价购买
 * <p/>
 * 抽奖购买
 */
public class PayOrderAct extends BaseActivity {

    private TextView tv_goods_name;
    private TextView tv_user_message;
    private LinearLayout ll_back;
    private TextView tv_all_price;
    private TextView tv_go_pay;

    private RelativeLayout rel_rel1;

    private CheckBox cb_gold, cb_wechat, cb_alipay;

    private Intent intent;
    private GetAddressEntity getAddressEntity;
    private String goodsName;
    private String totlaPrice;
    private String userMsg;
    private String flag;
    //private int gold;

    private String actId;
    private String buyCount;

    private IWXAPI msgApi;

    private int codeResult;

    private boolean isClick = false;

    private String payFlag;

    private TextView tv_surplus_time;

    private int surplusTime = 45; // 倒计时剩余时间

    private Timer timer;

    private String issue;

    String order_number1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: //查询元宝
                    int gold1 = (int) msg.obj;
                    if (Integer.valueOf(totlaPrice) > gold1) {
                        ToastUtil.makeToast("账户余额不足");
                    } else {
                        if (flag.equals("full")) {
                            getGoldFullBuyMsg("gold");
                        } else if (flag.equals("pop")) {
                            getCrowdBuyBuyMsg("gold");
                        }
                    }
                    break;
                case 11:  // 金币购买成功
                    if (flag.equals("full")) {
                        Intent intent = new Intent(PayOrderAct.this, PayFinishAct.class);
                        intent.putExtra("buyCount", buyCount);
                        intent.putExtra("totlaPrice", totlaPrice);
                        intent.putExtra("goodsName", goodsName);
                        startActivity(intent);
                        finish();
                    } else if (flag.equals("pop")) {
                        Intent intent = new Intent(PayOrderAct.this, PayFinishAct.class);
                        intent.putExtra("buyCount", buyCount);
                        intent.putExtra("totlaPrice", totlaPrice);
                        intent.putExtra("goodsName", goodsName);
                        startActivity(intent);
                        finish();
                    }
                    break;
                case 12: // 微信购买成功 支付宝购买成功
                    if (flag.equals("full")){
                        Intent intent = new Intent(PayOrderAct.this, PayFinishAct.class);
                        intent.putExtra("buyCount", buyCount);
                        intent.putExtra("totlaPrice", totlaPrice);
                        intent.putExtra("goodsName", goodsName);
                        intent.putExtra("flag" , "success");
                        startActivity(intent);
                        finish();
                    }else if (flag.equals("pop")){
                        Intent intent = new Intent(PayOrderAct.this, PayCrowdFinishAct.class);
                        intent.putExtra("buyCount", buyCount);
                        intent.putExtra("totlaPrice", totlaPrice);
                        intent.putExtra("goodsName", goodsName);
                        intent.putExtra("flag" , "success");
                        startActivity(intent);
                        finish();
                    }
                    break;
                case 13: //购买失败
                    Intent intent = new Intent(PayOrderAct.this, PayFinishAct.class);
                    intent.putExtra("buyCount", buyCount);
                    intent.putExtra("totlaPrice", totlaPrice);
                    intent.putExtra("goodsName", goodsName);
                    intent.putExtra("flag" , "fail");
                    startActivity(intent);
                    finish();
                    break;
                case 21: //支付宝下单单号
//                    OrderNumBean orderNumBean = (OrderNumBean) msg.obj;
//                    order_number1 = orderNumBean.getOrder_number();
//                    AlipayUtil2 alipay = new AlipayUtil2();
//                    alipay.pay(PayOrderAct.this, "全价购买" + totlaPrice + "元", order_number1, "全价购买商品", totlaPrice, handler);
//                    //alipay.pay(PayOrderAct.this, "全价购买" + totlaPrice + "元", order_number1, "全价购买商品", "0.01", handler);
                    break;
                case 22://微信下单单号
                    OrderNumBean orderNumBean1 = (OrderNumBean) msg.obj;
                    order_number1 = orderNumBean1.getOrder_number();
                    String newOrderNum1 = order_number1.substring(0, 5) + "," + order_number1.substring(5, 10) + "," + order_number1.substring(10, 15);
                    WXPayUtil2 wxpay = new WXPayUtil2(PayOrderAct.this);
                    wxpay.pay(newOrderNum1); // 微信支付处理
                    break;
                case 31://支付宝支付成功

                    if (flag.equals("full")) {
                        //getGoldFullBuyMsg("Alipay");
                        getCheckPayState(actId, order_number1);
                    }
                    break;
                case 32: // 微信支付成功
                    if (flag.equals("full")) {
                        getCheckPayState(actId, order_number1);
                        //getCheckPayState(actId, "161026469900016");
                    } else if (flag.equals("pop")) {
                        //getCrowdBuyBuyMsg("Alipay");
                        //getCheckPayState(actId , newOrderNum1);
                    }
                    break;
                case 41: //支付宝支付返回

                    Map<String, String> result = (Map<String, String>) msg.obj;
                    payReturn(result);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pay_order);

        msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Constants.APP_ID);

        intent = getIntent();
        if (intent != null) {
            flag = intent.getStringExtra("flag");
            if (flag.equals("full")) {
                getAddressEntity = (GetAddressEntity) intent.getSerializableExtra("address");
                goodsName = intent.getStringExtra("goodsName");
                totlaPrice = String.valueOf(intent.getStringExtra("allPrice"));
                actId = intent.getStringExtra("actId");
                buyCount = intent.getStringExtra("buyCount");
                issue = intent.getStringExtra("issue");
                Log.i("www", "传过来的信息goodsName" + goodsName + "totlaPrice" + totlaPrice);
            } else if (flag.equals("pop")) {
                totlaPrice = intent.getStringExtra("count");
                buyCount = intent.getStringExtra("count");
                actId = intent.getStringExtra("actId");
            }
        }
        initView();
        //getUserMsg();
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_user_message = (TextView) findViewById(R.id.tv_user_message);
        ll_back.setOnClickListener(clickListener);

        rel_rel1 = (RelativeLayout) findViewById(R.id.rel_rel1);
        tv_surplus_time = (TextView) findViewById(R.id.tv_surplus_time);

        if (getAddressEntity != null) {
            userMsg = getAddressEntity.getProvince() + getAddressEntity.getCity()
                    + getAddressEntity.getArea() + getAddressEntity.getAddress() + ", " + getAddressEntity.getRealname() + " , 手机 ："
                    + getAddressEntity.getMobile();
            tv_user_message.setText(userMsg);
            tv_surplus_time.setVisibility(View.GONE);
        } else {
            rel_rel1.setVisibility(View.GONE);
        }

        if (goodsName != null) {
            tv_goods_name.setText(goodsName);
        }

        tv_all_price = (TextView) findViewById(R.id.tv_all_price);
        tv_all_price.setText("¥" + totlaPrice);


        tv_go_pay = (TextView) findViewById(R.id.tv_go_pay);
        tv_go_pay.setOnClickListener(clickListener);

        cb_gold = (CheckBox) findViewById(R.id.cb_gold);
        cb_wechat = (CheckBox) findViewById(R.id.cb_wechat);
        cb_alipay = (CheckBox) findViewById(R.id.cb_alipay);

        cb_gold.setOnCheckedChangeListener(checkedChangeListener);
        cb_wechat.setOnCheckedChangeListener(checkedChangeListener);
        cb_alipay.setOnCheckedChangeListener(checkedChangeListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isClick) {
            if (payFlag != null && payFlag.equals("weChat")) {
                codeResult = WXPayEntryActivity.codeResult;
                Log.i("www", "onResume   code" + codeResult);
                if (codeResult == 1) {
                    // 微信支付成功,检测支付成功接口
                    handler.obtainMessage(12).sendToTarget();
                }else{
                    handler.obtainMessage(13).sendToTarget();
                }
            } else if (payFlag != null && payFlag.equals("alipay")) {

            }
        }
    }

    //获取用户个人信息
    public void getUserMsg() {
        if (CoresunApp.USER_ID == null) {
            return;
        }
        GetProfileBiz.getProfile(this, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                showUserMessage(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void showUserMessage(String result) {
        Log.i("www", "查询个人信息" + result);
        List<Object> enty_list = JsonPaser2.parserAry(result, ProfileDataEntity.class, "data");

        ProfileDataEntity enty = (ProfileDataEntity) enty_list.get(0);
        int gold = Integer.valueOf(enty.getGold());
        handler.obtainMessage(1, gold).sendToTarget();
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:
                    finish();
                    break;
                case R.id.tv_go_pay:
                    if (flag.equals("full")) {
                        if (cb_gold.isChecked()) {
                            isClick = true;
                            payFlag = "gold";
                            getUserMsg();
                        } else if (cb_alipay.isChecked()) {
                            isClick = true;
                            payFlag = "alipay";
                            getAWOrderNum("支付宝支付：" + totlaPrice, "1", totlaPrice);
                        } else if (cb_wechat.isChecked()) {
                            if (CheckApp.isWeixinAvilible(PayOrderAct.this)) {
                                isClick = true;
                                payFlag = "weChat";
                                getAWOrderNum("微信支付：" + totlaPrice, "2", totlaPrice);
                            } else {
                                ToastUtil.makeToast("您未安装微信");
                            }
                        } else {
                            ToastUtil.makeToast("请选择支付方式");
                        }
                    } else if (flag.equals("pop")) {
                        if (surplusTime > 0) {
                            if (cb_gold.isChecked()) {
                                isClick = true;
                                payFlag = "gold";
                                getUserMsg();
                            } else if (cb_alipay.isChecked()) {
                                isClick = true;
                                payFlag = "alipay";
                                getAWOrderNum("支付宝支付：" + totlaPrice, "1", totlaPrice);
                            } else if (cb_wechat.isChecked()) {
                                if (CheckApp.isWeixinAvilible(PayOrderAct.this)) {
                                    isClick = true;
                                    payFlag = "weChat";
                                    getAWOrderNum("微信支付：" + totlaPrice, "2", totlaPrice);
                                } else {
                                    ToastUtil.makeToast("您未安装微信");
                                }
                            } else {
                                ToastUtil.makeToast("请选择支付方式");
                            }
                        } else {
                            ToastUtil.makeToast("支付时间超出限制");
                        }
                    }
                    break;
            }
        }
    };


    //金币原价购买
    public  void getGoldFullBuyMsg(String type) {
        GetDataBiz.getFullPriceBuy(this, type, getAddressEntity, actId, buyCount, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "金币购买返回值" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("result")) {
                    if (jsonObject.getString("result").equals("200")) {
                        if (jsonObject.getString("type").equals("true")) {
                            handler.obtainMessage(11).sendToTarget();
                        }
                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    //金币众筹购买
    public void getCrowdBuyBuyMsg(String type) {
        GetDataBiz.getCrowdBuy(this, type, actId, buyCount, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "金币购买返回值" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("result")) {
                    if (jsonObject.getString("result").equals("200")) {
                        if (jsonObject.getString("type").equals("true")) {
                            handler.obtainMessage(11).sendToTarget();
                        }
                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    /**
     * 微信支付宝获得统一下单订单号
     *
     */
    public void getAWOrderNum(String cname, final String type, String amount) {
        GetDataBiz.getFullPriceBuyWA(this, cname, type, amount, actId, buyCount, "001", issue, "1" ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "微信支付宝统一下单单号" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("result")) {
                    if (jsonObject.getString("result").equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        OrderNumBean orderNumBean = new OrderNumBean();
                        orderNumBean.setOrder_number(data.getString("order_number"));
                        orderNumBean.setTotal_amount(data.getString("total_amount"));
                        orderNumBean.setTypeId(type);
                        if (type.equals("1")) {
                            handler.obtainMessage(21, orderNumBean).sendToTarget();
                        } else if (type.equals("2")) {
                            handler.obtainMessage(22, orderNumBean).sendToTarget();
                        }
                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                ToastUtil.makeToast("订单未提交成功，请重试");
            }
        });
    }

//    public void getAWOrderNum2(String out_trade_no ) {
//        GetDataBiz.getFullPriceBuyWA2(this , out_trade_no ,new HttpRequestListener() {
//            @Override
//            public void onHttpRequestFinish(String result) throws JSONException {
//                Log.i("www" , "2微信支付宝统一下单单号" + result);
//                JSONObject jsonObject = new JSONObject(result);
//                PayReq req = new PayReq();
//                req.appId = Constants.APP_ID;                //appid
//                req.partnerId = jsonObject.getString("mch_id");        //商户号
//                req.prepayId= jsonObject.getString("prepay_id");       //预付订单号
//                req.packageValue = "Sign=WXPay";                 //扩展字段
//                req.nonceStr= jsonObject.getString("nonce_str");       //随机字符串
//                req.timeStamp= String.valueOf(jsonObject.getString("timestamp")); //10位时间戳
//                req.sign = jsonObject.getString("sign");               //签名
//                msgApi.sendReq(req);
//            }
//        });
//    }

    //检测微信支付宝是否购买成功
    public void getCheckPayState(String aid, String orderNum) {
        GetDataBiz.checkCrowdPayState(aid, orderNum, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "检测支付购买成功状态" + result);
                JSONObject jsonObject = new JSONObject(result);

                Log.i("www", "检测支付购买成功状态" + jsonObject.getString("state"));
                    if (jsonObject.getString("state").equals("1")) {
                        //购买成功 跳转
                        handler.obtainMessage(12).sendToTarget();
                    } else if (jsonObject.getString("state").equals("0")){
                        handler.obtainMessage(13).sendToTarget();
                    }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            Log.i("www", "isChecked" + isChecked);

            switch (buttonView.getId()) {
                case R.id.cb_gold:
                    if (isChecked == true) {
                        cb_gold.setChecked(isChecked);
                        cb_wechat.setChecked(!isChecked);
                        cb_alipay.setChecked(!isChecked);
                    } else {
                        cb_gold.setChecked(isChecked);
//                        cb_wechat.setChecked(isChecked);
//                        cb_alipay.setChecked(isChecked);
                    }
                    break;
                case R.id.cb_wechat:
                    if (isChecked == true) {
                        cb_gold.setChecked(!isChecked);
                        cb_wechat.setChecked(isChecked);
                        cb_alipay.setChecked(!isChecked);
                    } else {
                        cb_wechat.setChecked(isChecked);
                        //cb_gold.setChecked(isChecked);
                        //cb_alipay.setChecked(isChecked);
                    }
                    break;
                case R.id.cb_alipay:
                    if (isChecked == true) {
                        cb_gold.setChecked(!isChecked);
                        cb_wechat.setChecked(!isChecked);
                        cb_alipay.setChecked(isChecked);
                    } else {
//                        cb_gold.setChecked(isChecked);
//                        cb_wechat.setChecked(isChecked);
                        cb_alipay.setChecked(isChecked);
                    }
                    break;
            }
        }
    };


    //处理返回的支付结果
    private void payReturn(Map<String, String> result) {

        PayResult payResult = new PayResult(result);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功

        LogUtil.i("wwww" +  resultStatus);

        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            //handler.obtainMessage(12).sendToTarget();

            handler.obtainMessage(31).sendToTarget();

            Toast.makeText(PayOrderAct.this, "支付成功", Toast.LENGTH_SHORT).show();
        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            handler.obtainMessage(13).sendToTarget();
            Toast.makeText(PayOrderAct.this, "支付失败", Toast.LENGTH_SHORT).show();
        }


//        PayResult payResult = new PayResult(result);
//        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//        String resultInfo = payResult.getResult();
//        LogUtil.i("支付宝支付返回结果PayOrderAct:" + resultInfo);
//        String resultStatus = payResult.getResultStatus();
//
//        LogUtil.i("-------------resultStatus-------------" + resultStatus);
//
//        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//        if (TextUtils.equals(resultStatus, "9000")) {
//            //ToastUtil.makeToast("支付宝支付成功");
//                handler.obtainMessage(31).sendToTarget();
//        } else {
//            // 判断resultStatus 为非“9000”则代表可能支付失败
//            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//            if (TextUtils.equals(resultStatus, "8000")) {
//                //ToastUtil.makeToast("支付结果确认中...");
//                handler.obtainMessage(31).sendToTarget();
//                //handler.obtainMessage(102).sendToTarget();
//            } else {
//                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                //ToastUtil.makeToast("支付宝支付失败");
//                handler.obtainMessage(31).sendToTarget();
//                //handler.obtainMessage(102).sendToTarget();
//            }
//        }

    }

}
