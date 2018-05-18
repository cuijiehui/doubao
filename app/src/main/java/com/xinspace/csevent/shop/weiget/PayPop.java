package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.StoreOrderAct;
import com.xinspace.csevent.shop.modle.GetOrderBean;
import com.xinspace.csevent.util.CheckApp;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.WXPayUtil2;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android on 2017/5/18.
 */

public class PayPop extends PopupWindow {

    private Context context;
    private View view;

    private String orderId;
    private SDPreference preference;
    private String openId;

    private String ordersn;
    private String price;
    private GetOrderBean getOrderBean;

    private RelativeLayout rel_chat;
    private TextView tv_pay_bg;
    private RelativeLayout rel_alipay;
    private RelativeLayout rel_yinlian;
    //WXPayUtil2 wxpay ;
    private int codeResult;

    private int PAYSUCCESS = 1;
    private int PAYERROR = 0;
    private boolean isClickWChact = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    getOrderBean = (GetOrderBean) msg.obj;
                    ordersn = getOrderBean.getOrdersn();
                    price = getOrderBean.getPrice();
                    break;
                case 500:
                    ToastUtil.makeToast("网络连接失败");
                    break;
                case 0:
                    ToastUtil.makeToast("支付失败");
                    PayPop.this.dismiss();
                    //PayGoodsAct.this.finish();
                    break;
                case 1:
                    ToastUtil.makeToast("支付成功");
                    Intent intent = new Intent(context, StoreOrderAct.class);
                    intent.putExtra("flag" , "0");
                    context.startActivity(intent);
                    PayPop.this.dismiss();
                    break;
            }
        }
    };


    public PayPop(Context context , String orderId , String openId) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.act_pay_goods, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.orderId = orderId;
        this.orderId = openId;
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context));
        //this.getCouponListener = getCouponListener;
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
        initData();
    }

    private void initData() {

        final Gson gson = new Gson();
        GetDataBiz.getOrderData(orderId, openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取订单返回的结果" + result);
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")) {
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    GetOrderBean getOrderBean = gson.fromJson(dataJsonObject.toString(), GetOrderBean.class);
                    handler.obtainMessage(200, getOrderBean).sendToTarget();
                } else {
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                handler.obtainMessage(500).sendToTarget();
            }
        });
    }

    private void initView() {
        rel_chat = (RelativeLayout) view.findViewById(R.id.rel_chat);
        rel_chat.setOnClickListener(onClickListener);

        rel_alipay = (RelativeLayout) view.findViewById(R.id.rel_alipay);
        rel_alipay.setOnClickListener(onClickListener);

        tv_pay_bg = (TextView) view.findViewById(R.id.tv_pay_bg);
        tv_pay_bg.setOnClickListener(onClickListener);

        rel_yinlian = (RelativeLayout) view.findViewById(R.id.rel_yinlian);
        rel_yinlian.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_chat:
                    if (CheckApp.isWeixinAvilible(context)) {
                        isClickWChact = true;
                        WXPayUtil2 wxpay = new WXPayUtil2(context);
                        wxpay.pay(ordersn, price);
                    } else {
                        ToastUtil.makeToast("您未安装微信");
                    }
                    break;
                case R.id.rel_alipay:
                    ToastUtil.makeToast("此支付方式暂时无法使用");
                    break;
                case R.id.rel_yinlian:
                    ToastUtil.makeToast("此支付方式暂时无法使用");
                    break;
                case R.id.tv_pay_bg:
                    PayPop.this.dismiss();
                    break;
            }
        }
    };

}
