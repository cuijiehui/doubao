package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.OrderDetailAdapter;
import com.xinspace.csevent.shop.modle.ExpressBean;
import com.xinspace.csevent.shop.modle.OrderDetail;
import com.xinspace.csevent.shop.modle.OrderDetailBean;
import com.xinspace.csevent.shop.modle.OrderMiddleBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情
 *
 * Created by Android on 2017/5/17.
 */

public class OrderDetailAct extends BaseActivity{


    private LinearLayout ll_store_order_back;
    private TextView tv_order_status;
    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView tv_user_address;

    private MyListView lv_order;
    private TextView tv_status_but;
    private TextView tv_order_price;

    private TextView tv_order_freight;
    private TextView tv_order_real_price;

    private TextView tv_order_num;
    private TextView tv_pay_way;
    private TextView tv_order_time;
    private TextView tv_pay_time;

    private Intent intent;
    private String orderId;

    private SDPreference preference;
    private String openId;
    private OrderDetailBean orderDetailBean;

    private OrderDetailAdapter orderDetailAdapter;

    private RelativeLayout rel_has_express;
    private TextView tv_express_content;
    private TextView tv_express_time;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        orderDetailBean = (OrderDetailBean) msg.obj;
                        showData();
                    }
                    break;
                case 400:
                    ToastUtil.makeToast("申请成功");
                    tv_status_but.setText("售后/退款");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(OrderDetailAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_order_detail);

        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");

        intent = getIntent();
        if (intent != null){
            orderId = intent.getStringExtra("orderId");
            LogUtil.i("单个订单的ID" + orderId);
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {

        ll_store_order_back = (LinearLayout) findViewById(R.id.ll_store_order_back);
        ll_store_order_back.setOnClickListener(clickListener);

        tv_order_status = (TextView) findViewById(R.id.tv_order_status);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);

        lv_order = (MyListView) findViewById(R.id.lv_order);
        tv_status_but = (TextView) findViewById(R.id.tv_status_but);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        tv_order_freight = (TextView) findViewById(R.id.tv_order_freight);
        tv_order_real_price = (TextView) findViewById(R.id.tv_order_real_price);

        tv_status_but.setOnClickListener(clickListener);

        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        tv_pay_way = (TextView) findViewById(R.id.tv_pay_way);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);

        tv_order_status.setOnClickListener(clickListener);

        rel_has_express = (RelativeLayout) findViewById(R.id.rel_has_express);
        rel_has_express.setOnClickListener(clickListener);
        rel_has_express.setVisibility(View.GONE);

        tv_express_content = (TextView) findViewById(R.id.tv_express_content);
        tv_express_time = (TextView) findViewById(R.id.tv_express_time);

    }


    private void showData() {

        OrderDetail orderDetail = orderDetailBean.getOrderDetail();
        GetAddressEntity getAddressEntity = orderDetailBean.getGetAddressEntity();
        List<OrderMiddleBean> goodsList = orderDetailBean.getGoodsList();
        ExpressBean expressBean = orderDetailBean.getExpressBean();

        String status = orderDetail.getStatus();
        int refundstate = Integer.valueOf(orderDetail.getRefundstate()) ;

        if (refundstate > 0){

            if (status.equals("0")){
                tv_order_status.setText("申请售后中");
            }else if(status.equals("1")){
                tv_order_status.setText("退款中");
            }else if (status.equals("2")){
                rel_has_express.setVisibility(View.VISIBLE);
                tv_order_status.setText("申请售后中");
            }else if (status.equals("3")){
                rel_has_express.setVisibility(View.VISIBLE);
                tv_order_status.setText("申请售后中");
            }

            if (status.equals("0")){
                tv_status_but.setText("申请售后中");
            }else if(status.equals("1")){
                tv_status_but.setText("退款中");
                tv_status_but.setOnClickListener(clickListener);
            }else if (status.equals("2")){
                tv_status_but.setText("申请售后中");
                tv_status_but.setOnClickListener(clickListener);
            }else if (status.equals("3")){
                tv_status_but.setText("申请售后中");
            }

        }else{
            if (status.equals("0")){
                tv_order_status.setText("待付款");
            }else if(status.equals("1")){
                tv_order_status.setText("待发货");
            }else if (status.equals("2")){
                rel_has_express.setVisibility(View.VISIBLE);
                tv_order_status.setText("待收货");
            }else if (status.equals("3")){
                rel_has_express.setVisibility(View.VISIBLE);
                tv_order_status.setText("已完成");
            }

            if (status.equals("0")){
                tv_status_but.setText("付款");
            }else if(status.equals("1")){
                tv_status_but.setText("售后/退款");
                tv_status_but.setOnClickListener(clickListener);
            }else if (status.equals("2")){
                tv_status_but.setText("售后/退款");
                tv_status_but.setOnClickListener(clickListener);
            }else if (status.equals("3")){
                tv_status_but.setText("交易完成");
            }
        }


        tv_express_time.setText(expressBean.getTime());
        tv_express_content.setText(expressBean.getStep());

        String addressUser = getAddressEntity.getRealname();
        String addressTel = getAddressEntity.getMobile();
        String address = getAddressEntity.getProvince() + getAddressEntity.getCity() + getAddressEntity.getArea() + getAddressEntity.getAddress();

        tv_user_name.setText(addressUser);
        tv_user_phone.setText(addressTel);
        tv_user_address.setText(address);

        orderDetailAdapter = new OrderDetailAdapter(OrderDetailAct.this);
        orderDetailAdapter.setGoodsList(goodsList);
        lv_order.setAdapter(orderDetailAdapter);

        tv_order_price.setText("¥" + orderDetail.getGoodsprice());
        tv_order_freight.setText("¥" + orderDetail.getOlddispatchprice());
        tv_order_real_price.setText("¥" + orderDetail.getOldprice());

        String orderNum = orderDetail.getOrdersn();
        String createtime = orderDetail.getCreatetime();
        String paytime = orderDetail.getPaytime();

        tv_order_num.setText("订单编号：" + orderNum);
        String payType = orderDetail.getPaytype();
        if (payType.equals("0")){
            tv_pay_way.setText("支付方式：尚未支付");
        }
        else if (payType.equals("21")){
            tv_pay_way.setText("支付方式：微信支付");
        }
        else if (payType.equals("22")){
            tv_pay_way.setText("支付方式：支付宝支付");
        }
        else{
            tv_pay_way.setText("支付方式：其他支付");
        }


        tv_order_time.setText("下单时间：" + createtime);

        if (!paytime.equals("")){
            tv_pay_time.setText( "付款时间：" + paytime);
        }else{
            tv_pay_time.setText("付款时间：" + "未付款");
        }

    }


    private void initData() {
        GetDataBiz.getOrderDetailData(orderId, openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("单个订单详情的result" + result);
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jSONObject = new JSONObject(result);
                if (jSONObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jSONObject.getJSONObject("data");

                    OrderDetailBean orderDetailBean = new OrderDetailBean();
                    Gson gson = new Gson();
                    OrderDetail orderDetail = gson.fromJson(dataJsonObject.toString() , OrderDetail.class);

                    JSONObject addressJsonObject = dataJsonObject.getJSONObject("address");
                    GetAddressEntity getAddressEntity = gson.fromJson(addressJsonObject.toString() , GetAddressEntity.class);

                    List<OrderMiddleBean> goodsList = new ArrayList<OrderMiddleBean>();
                    JSONArray goodsArray = dataJsonObject.getJSONArray("goods");
                    for(int i = 0 ; i < goodsArray.length() ; i++){
                        JSONObject goodsJsonobject = goodsArray.getJSONObject(i);
                        OrderMiddleBean orderMiddleBean = gson.fromJson(goodsJsonobject.toString() , OrderMiddleBean.class);
                        goodsList.add(orderMiddleBean);
                    }

                    LogUtil.i("订单详情解析" + goodsList.size());

                    JSONObject expressObject = dataJsonObject.getJSONObject("express");
                    ExpressBean expressBean = gson.fromJson(expressObject.toString() , ExpressBean.class);

                    orderDetailBean.setOrderDetail(orderDetail);
                    orderDetailBean.setGetAddressEntity(getAddressEntity);
                    orderDetailBean.setGoodsList(goodsList);
                    orderDetailBean.setExpressBean(expressBean);

                    handler.obtainMessage(200 , orderDetailBean).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_store_order_back:
                    finish();
                    break;
                case R.id.tv_status_but:

                    LogUtil.i("tv_status_but.getText().toString()" + tv_status_but.getText().toString());

                    if(tv_status_but.getText().toString().trim().equals("售后/退款")){
                        Intent intent = new Intent(OrderDetailAct.this , AfterSaleAct.class);
                        intent.putExtra("order" , orderDetailBean);
                        intent.putExtra("orderid" , orderId);
                        startActivityForResult(intent , 1000);
                    }else if (tv_status_but.getText().toString().trim().equals("退款中")
                            |tv_status_but.getText().toString().trim().equals("申请售后中")
                            ){
                        cacleData();
                    }else if (tv_status_but.getText().toString().trim().equals("付款")){
                        Intent intent = new Intent(OrderDetailAct.this , PayOrderActivity.class);
                        intent.putExtra("orderId" , orderId);
                        startActivity(intent);
                    }
                    break;
                case R.id.rel_has_express:
                    Intent intent = new Intent(OrderDetailAct.this , ExpressAct.class);
                    intent.putExtra("orderId" , orderId);
                    intent.putExtra("flag" , "1");
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 1001) {

            tv_status_but.setText("退款成功");

        }
    }


    private void cacleData(){
        GetDataBiz.cancleAfterSaleData(orderId, openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    handler.obtainMessage(400).sendToTarget();
                }else{
                    ToastUtil.makeToast("取消失败");
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                ToastUtil.makeToast("取消失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        ImagerLoaderUtil.clearImageMemory();
        clickListener = null;
        intent = null;
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
