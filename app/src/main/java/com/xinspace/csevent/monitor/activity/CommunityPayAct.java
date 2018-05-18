package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.xinspace.csevent.alipay.PayResult;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.monitor.bean.PayTypeBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.StoreOrderAct;
import com.xinspace.csevent.sweepstake.modle.AlipayInfo;
import com.xinspace.csevent.sweepstake.modle.FirstEvent;
import com.xinspace.csevent.util.CheckApp;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.ToastUtils;
import com.xinspace.csevent.util.WXPayUtil2;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.Map;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;
import static com.xinspace.csevent.wxapi.Constants.ALIPAY_SUCCESSED;

/**
 * 社区缴费 支付界面
 *
 *
 * Created by Android on 2017/5/24.
 */

public class CommunityPayAct extends BaseActivity{

    private static final int ALIPAY_STATUS = 3;
    private String payId;
    private Intent intent ;
    private PayTypeBean bean;
    private String typeName;
    private ImageView iv_type_icon;
    private TextView tv_type_name;
    private TextView tv_pay_money;
    private TextView tv_remark;
    private TextView tv_community_pay_title;

    private CheckBox cb_wechat, cb_alipay;
    private TextView tv_go_pay;

    private boolean isClickWChact;

    private SDPreference preference;
    private String cUid;
    private String token;
    private LinearLayout ll_community_pay_back;
    private int codeResult;
    private int PAYSUCCESS = 1;
    private int PAYERROR = 0;
    private Gson gson = new Gson();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ToastUtil.makeToast("支付失败");
                    CommunityPayAct.this.finish();
                    break;
                case 1:
                    ToastUtil.makeToast("支付成功");
                    Intent intent = new Intent(CommunityPayAct.this, PayMentRecordAct.class);
                    startActivity(intent);
                    CommunityPayAct.this.finish();
                    break;

                case ALIPAY_STATUS:
                    PayResult result = new PayResult((Map<String, String>) msg.obj);
                    String resultStatus = result.getResultStatus();
                    String resultinfo = result.getResult();
                    if (TextUtils.equals(resultStatus, ALIPAY_SUCCESSED)){
                        EventBus.getDefault().post(new FirstEvent("4"));
                        Intent intent2 = new Intent(CommunityPayAct.this, StoreOrderAct.class);
                        intent2.putExtra("flag" , "0");
                        startActivity(intent2);
                        LogUtil.e("支付成功");
                        ToastUtil.makeToast("支付成功");
                        CommunityPayAct.this.finish();
                    }else{
                        LogUtil.e("resultCode:" + resultStatus + "\nresultInfo:" + resultinfo);
                        ToastUtil.makeToast("支付失败");
                    }
                    break;
            }
        }
    };
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_community_pay);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        preference = SDPreference.getInstance();
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        cUid = preference.getContent("cUid");
        token = preference.getContent("cToken");

        intent = getIntent();
        if (intent != null){
            bean = (PayTypeBean) intent.getSerializableExtra("bean");
            typeName = intent.getStringExtra("typeName");
            payId = bean.getId();
        }
        initView();
    }

    private void initView() {

        ll_community_pay_back = (LinearLayout) findViewById(R.id.ll_community_pay_back);
        ll_community_pay_back.setOnClickListener(clickListener);

        tv_community_pay_title = (TextView) findViewById(R.id.tv_community_pay_title);

        iv_type_icon = (ImageView) findViewById(R.id.iv_type_icon);
        tv_type_name = (TextView) findViewById(R.id.tv_type_name);
        tv_pay_money = (TextView) findViewById(R.id.tv_pay_money);
        tv_remark = (TextView) findViewById(R.id.tv_remark);

        tv_go_pay = (TextView) findViewById(R.id.tv_go_pay);
        tv_go_pay.setOnClickListener(clickListener);

        cb_wechat = (CheckBox) findViewById(R.id.cb_wechat);
        cb_wechat.setChecked(true);

        cb_alipay = (CheckBox) findViewById(R.id.cb_alipay);

        cb_wechat.setOnCheckedChangeListener(checkedChangeListener);
        cb_alipay.setOnCheckedChangeListener(checkedChangeListener);

        tv_community_pay_title.setText(typeName);
        tv_type_name.setText(typeName);
        tv_pay_money.setText(bean.getSum() + "元");
        tv_remark.setText(" : "+bean.getInstruct());

        if (typeName.equals("水费")) {
            iv_type_icon.setImageResource(R.drawable.icon_pay_water_big);
        }else if (typeName.equals("电费")) {
            iv_type_icon.setImageResource(R.drawable.icon_energy_charge_big);
        }else if (typeName.equals("煤气费")) {
            iv_type_icon.setImageResource(R.drawable.icon_fuel_gas_big);
        }else if (typeName.equals("物业费")) {
            iv_type_icon.setImageResource(R.drawable.icon_property_fee_big);
        }else if (typeName.equals("停车费")) {
            iv_type_icon.setImageResource(R.drawable.icon_parking_charge_big);
        }else if (typeName.equals("管理费")) {
            iv_type_icon.setImageResource(R.drawable.icon_pay_other_big);
        }else{
            iv_type_icon.setImageResource(R.drawable.icon_pay_other_big);
        }
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
                    handler.obtainMessage(PAYSUCCESS).sendToTarget();
                } else {
                    // 微信支付失败
                    handler.obtainMessage(PAYERROR).sendToTarget();
                }
            }
        }
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            Log.i("www", "isChecked" + isChecked);

            switch (buttonView.getId()) {
                case R.id.cb_wechat:
                    if (isChecked == true) {
                        cb_wechat.setChecked(isChecked);
                        cb_alipay.setChecked(!isChecked);
                    } else {
                        cb_wechat.setChecked(isChecked);
                    }
                    break;
                case R.id.cb_alipay:
                    if (isChecked == true) {
                        cb_wechat.setChecked(!isChecked);
                        cb_alipay.setChecked(isChecked);
                    } else {
                        cb_alipay.setChecked(isChecked);
                    }
                    break;
            }
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_community_pay_back:
                    finish();
                    break;
                case R.id.tv_go_pay:
                    if (cb_wechat.isChecked()) {  //微信支付
                        if (CheckApp.isWeixinAvilible(CommunityPayAct.this)) {
                            isClickWChact = true;
                            WXPayUtil2 wxpay = new WXPayUtil2(CommunityPayAct.this);
                            wxpay.pay(cUid, token, payId);
                        } else {
                            ToastUtil.makeToast("您未安装微信");
                        }
                    }
                    else if (cb_alipay.isChecked()) {   //支付宝支付
                        isClickWChact = true;
                        GetDataBiz.getCommunityAlipayInfo(area, cUid, token, payId, new HttpRequestListener() {
                            @Override
                            public void onHttpRequestFinish(String result) throws JSONException {
                                createAlipay(result);
                            }

                            @Override
                            public void onHttpRequestError(String error) {
                                ToastUtil.makeToast("获取支付宝订单失败");
                            }
                        });

                    }else{
                        ToastUtil.makeToast("请选择支付方式");
                    }
                    break;
            }
        }
    };

    private void createAlipay(String result) {
        AlipayInfo alipayInfo = gson.fromJson(result, AlipayInfo.class);
        if (alipayInfo.getCode().equals("200")){
            final String info = alipayInfo.getData().getOrderinfo();
            if (TextUtils.equals(info, "400")) return;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(CommunityPayAct.this);
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
        handler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        clickListener = null;
        System.gc();
        super.onDestroy();
    }

}
