package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.OrderDetail;
import com.xinspace.csevent.shop.modle.OrderDetailBean;
import com.xinspace.csevent.shop.modle.RefundBean;
import com.xinspace.csevent.shop.weiget.ReimbursePop;
import com.xinspace.csevent.shop.weiget.ReimbursePop2;
import com.xinspace.csevent.shop.weiget.ReimburseReSult;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 售后退款界面
 *
 * Created by Android on 2017/5/18.
 */

public class AfterSaleAct extends BaseActivity{

    private LinearLayout ll_after_sale_back;
    private RelativeLayout rel_1;
    private RelativeLayout rel_2;

    private TextView tv_1;
    private TextView tv_2;

    private EditText ed_reimburse_price;
    private EditText ed_reimburse_content;
    private TextView tv_submit;
    private LinearLayout rel_bg;

    private ReimbursePop reimbursePop;
    private ReimbursePop2 reimbursePop2;

    private Intent intent;
    private OrderDetailBean orderDetailBean;
    private OrderDetail orderDetail;
    private String price;
    private String id;
    private SDPreference preference;
    private String openId;

    private String result1 , result2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    Intent intent = new Intent();
                    setResult(1001);
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtils.setWindowStatusBarColor(AfterSaleAct.this , R.color.app_bottom_color);

        setContentView(R.layout.act_after_sale);
        preference = SDPreference.getInstance();
        intent = getIntent();
        if (intent != null){
            orderDetailBean = (OrderDetailBean) intent.getSerializableExtra("order");
            id = intent.getStringExtra("orderid");
        }

        openId = preference.getContent("openid");

        orderDetail = orderDetailBean.getOrderDetail();
        price = orderDetail.getOldprice();

        initView();
        initData();
    }

    private void initView() {

        ll_after_sale_back = (LinearLayout) findViewById(R.id.ll_after_sale_back);
        ll_after_sale_back.setOnClickListener(onClickListener);

        rel_bg = (LinearLayout) findViewById(R.id.rel_bg);

        rel_1 = (RelativeLayout) findViewById(R.id.rel_1);
        rel_1.setOnClickListener(onClickListener);

        rel_2 = (RelativeLayout) findViewById(R.id.rel_2);
        rel_2.setOnClickListener(onClickListener);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);

        ed_reimburse_content = (EditText) findViewById(R.id.ed_reimburse_price);
        ed_reimburse_price = (EditText) findViewById(R.id.ed_reimburse_price);

        ed_reimburse_price.setHint("最大退款金额" + price);

        tv_submit = (TextView) findViewById(R.id.tv_submit);//提交申请
        tv_submit.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_after_sale_back:

                    finish();

                    break;
                case R.id.rel_1:
                    showPop1();
                    break;
                case R.id.rel_2:
                    showPop2();
                    break;
                case R.id.tv_submit:
                    initData();
                    break;
            }
        }
    };


    private void initData() {
        RefundBean refundBean = new RefundBean();

        if (result1 != null && !result1.equals("")){
            if (rel_1.equals("退款不退货")){
                refundBean.setRtype("0");
            }else{
                refundBean.setRtype("1");
            }
        }else{
            ToastUtil.makeToast("请选择申请服务");
            return;
        }

        if (result2 != null && !result2.equals("")){
            refundBean.setReason(result2);
        }else{
            ToastUtil.makeToast("请选择退款原因");
            return;
        }


        String content = ed_reimburse_content.getText().toString();
        refundBean.setContent(content);

        String price = ed_reimburse_price.getText().toString();
        if (price != null && !price.equals("")){
            refundBean.setPrice(price);
        }else{
            ToastUtil.makeToast("请选择退款金额");
            return;
        }

        refundBean.setOpenId(openId);
        refundBean.setOrderId(id);

        GetDataBiz.afterSaleData(refundBean , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if(result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    handler.obtainMessage(200).sendToTarget();
                    ToastUtil.makeToast("申请退款成功");
                }else{
                    ToastUtil.makeToast("申请退款失败");
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                ToastUtil.makeToast("申请退款失败");
            }
        });
    }


    private void showPop1(){
        if (reimbursePop == null) {
            reimbursePop = new ReimbursePop(AfterSaleAct.this , reimburseReSult);
        }

        if (!reimbursePop.isShowing()) {
            reimbursePop.showAtLocation(rel_bg, Gravity.BOTTOM, 0, 0);
            reimbursePop.isShowing();
        } else {
            reimbursePop.dismiss();
            reimbursePop = null;
        }
    }


    private void showPop2(){
        if (reimbursePop2 == null) {
            reimbursePop2 = new ReimbursePop2(AfterSaleAct.this , reimburseReSult2);
        }
        if (!reimbursePop2.isShowing()) {
            reimbursePop2.showAtLocation(rel_bg, Gravity.BOTTOM, 0, 0);
            reimbursePop2.isShowing();
        } else {
            reimbursePop2.dismiss();
            reimbursePop2 = null;
        }
    }


    ReimburseReSult reimburseReSult = new ReimburseReSult(){

        @Override
        public void setResult(String result) {
            result1 = result;
            tv_1.setText( "申请服务 " + result);
        }
    };


    ReimburseReSult reimburseReSult2 = new ReimburseReSult(){

        @Override
        public void setResult(String result) {
            result2 = result;
            tv_2.setText( "退货原因 " + result);
        }
    };


    @Override
    protected void onDestroy() {
        reimburseReSult = null;
        reimburseReSult2 = null;
        onClickListener = null;
        handler.removeCallbacksAndMessages(null);
        if (reimbursePop != null){
            reimbursePop.onDestory();
        }
        if (reimbursePop2 != null){
            reimbursePop2.onDestory();
        }
        reimbursePop = null;
        reimbursePop2 = null;
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();

    }
}
