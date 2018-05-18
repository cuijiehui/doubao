package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.util.CheckApp;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.WXPayUtil3;


/**
 * 开团商品购买选择支付方式
 *
 * Created by Android on 2017/5/18.
 */

public class PayGroupPop extends PopupWindow {

    private Context context;
    private View view;
    private String orderId;
    private SDPreference preference;
    private String openId;

    private String ordersn;

    private RelativeLayout rel_chat;
    private TextView tv_pay_bg;
    private RelativeLayout rel_alipay;
    private RelativeLayout rel_yinlian;
    //WXPayUtil2 wxpay ;

    private int codeResult;
    private int PAYSUCCESS = 1;
    private int PAYERROR = 0;
    private boolean isClickWChact = false;
    private String teamid;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 500:
                    ToastUtil.makeToast("网络连接失败");
                    break;
//                case 0:
//                    ToastUtil.makeToast("支付失败");
//                    PayGroupPop.this.dismiss();
//                    //PayGoodsAct.this.finish();
//                    break;
//                case 1:
//                    ToastUtil.makeToast("支付成功");
//                    Intent intent = new Intent(context, StoreOrderAct.class);
//                    intent.putExtra("flag" , "0");
//                    context.startActivity(intent);
//                    PayGroupPop.this.dismiss();
//                    break;
            }
        }
    };


    public PayGroupPop(Context context , String orderId , String teamid) {
        this.context = context;
        this.orderId = orderId;
        this.teamid = teamid;
        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
        view = LayoutInflater.from(context).inflate(R.layout.act_pay_goods, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context));
        //this.getCouponListener = getCouponListener;
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
        initData();
    }

    private void initData() {

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
                        WXPayUtil3 wxpay = new WXPayUtil3(context);
                        wxpay.pay(openId , orderId , teamid);
                    } else {
                        ToastUtil.makeToast("您未安装微信,请先安装微信");
                    }
                    break;
                case R.id.rel_alipay:
                    ToastUtil.makeToast("此支付方式暂时无法使用");
                    break;
                case R.id.rel_yinlian:
                    ToastUtil.makeToast("此支付方式暂时无法使用");
                    break;
                case R.id.tv_pay_bg:
                    PayGroupPop.this.dismiss();
                    break;
            }
        }
    };

}
