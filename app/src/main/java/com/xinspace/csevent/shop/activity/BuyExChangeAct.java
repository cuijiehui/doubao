package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.ExGoodsBean;
import com.xinspace.csevent.shop.modle.GroupOrderBean;
import com.xinspace.csevent.sweepstake.activity.ChangeAddressAct;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/4/14.
 *
 * 积分商城商品下单
 */

public class BuyExChangeAct extends BaseActivity{

    private LinearLayout ll_order_back;
    private RelativeLayout rel_no_address;
    private RelativeLayout rel_has_address;

    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView tv_user_address;
    private TextView tv_goods_num;

    private ImageView iv_goods_image;
    private TextView tv_goods_name;
    private TextView tv_goods_prize;

    private TextView tv_detail_reduce;
    private TextView tv_detail_add;
    private EditText et_detail_count;

    private TextView tv_all;
    private TextView tv_all_prize;
    private TextView tv_go_order;

    private List<Object> list;
    private List<GetAddressEntity> addresssList;
    private GetAddressEntity getAddressEntity;
    private GetAddressEntity getAddressEntity1;

    private int count = 1;
    private Float prize;

    private String imgUrl;
    private ExGoodsBean goodsDetail;
    private Intent intent;
    private ImageView iv_go_edit_address;
    private int goodsNum;
    private String openid;
    private SDPreference preference;
    private RelativeLayout rel_edit_address;
    private String addressId;

    private String allPrice;
    private String type;
    private String teamid;
    private RelativeLayout rel_bottom;

    private int codeResult;
    private boolean isClickWChact;
    private TextView tv_goods_msg;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String detailAddaress;
            switch (msg.what){
                case 1: //直接有地址
                    getAddressEntity1 = (GetAddressEntity) msg.obj;
                    addressId = getAddressEntity1.getId();
                    tv_user_name.setText(getAddressEntity1.getRealname());
                    tv_user_phone.setText(getAddressEntity1.getMobile());
                    detailAddaress =  getAddressEntity1.getProvince() + getAddressEntity1.getCity()
                            + getAddressEntity1.getArea() + getAddressEntity1.getAddress();
                    tv_user_address.setText(detailAddaress);
                    break;
                case 2:  // 添加地址
                    getAddressEntity1 = (GetAddressEntity) msg.obj;
                    addressId = getAddressEntity1.getId();
                    tv_user_name.setText(getAddressEntity1.getRealname());
                    tv_user_phone.setText(getAddressEntity1.getMobile());
                    detailAddaress =  getAddressEntity1.getProvince() + getAddressEntity1.getCity()
                            + getAddressEntity1.getArea() + getAddressEntity1.getAddress();
                    tv_user_address.setText(detailAddaress);
                    break;
                case 200 : // 下单成功生成订单号 去支付
                    String orderId = (String) msg.obj;
                    goToPay(orderId);

                    break;
                case 300:   // 只需积分
                    BuyExChangeAct.this.finish();
                    Intent intent = new Intent(BuyExChangeAct.this , ConvertRecordAct.class);
                    startActivity(intent);
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

        StatusBarUtils.setWindowStatusBarColor(BuyExChangeAct.this , R.color.app_bottom_color);

        setContentView(R.layout.act_buy_exchange);
        //EventBus.getDefault().register(this);
        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");
        initView();
        getUserAddresses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("-------------------------------");
        codeResult = WXPayEntryActivity.codeResult;
        Log.i("www", "onResume   code" + codeResult);
        if (isClickWChact){
            if (codeResult != 0){

                if (codeResult == 1) {
                    // 微信支付成功,检测支付成功接口

                } else {
                    // 微信支付失败
                    //handler.obtainMessage(PAYERROR).sendToTarget();
                }

                finish();
                Intent intent = new Intent(BuyExChangeAct.this , ConvertRecordAct.class);
                startActivity(intent);
            }
        }

    }



    public void getUserAddresses() {
        AddressManagerBiz.getAddressList( openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("查询地址的" + result);
                if(result != null && !result.equals("")){
                    handleAddAddressResult(result);
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //处理用户地址返回的结果
    private void handleAddAddressResult(String result) {

        list = JsonPaser2.parserAry(result, GetAddressEntity.class, "data");
        //显示地址
        if (list.size() != 0 && list.get(0) instanceof GetAddressEntity) {
            rel_no_address.setVisibility(View.GONE);
            rel_has_address.setVisibility(View.VISIBLE);
            addresssList = new ArrayList<>();
            for (Object obj : list) {
                addresssList.add((GetAddressEntity) obj);
            }

            if (list.size() == addresssList.size()) {
                for (int i = 0; i < addresssList.size(); i++) {
                    if (addresssList.get(i).getIsdefault().equals("1")) {
                        getAddressEntity = addresssList.get(i);
                        handler.obtainMessage(1, getAddressEntity).sendToTarget();
                        return;
                    } else {
                        getAddressEntity = addresssList.get(0);
                        handler.obtainMessage(1, getAddressEntity).sendToTarget();
                    }
                }
            }
        }else{
            rel_no_address.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_order_back:
                    BuyExChangeAct.this.finish();
                    break;
                case R.id.rel_no_address:
                    Intent intent = new Intent(BuyExChangeAct.this , ChangeAddressAct.class);
                    startActivityForResult(intent , 100);
                    break;
                case R.id.tv_go_order:
                    int tab = goodsDetail.getTab();
                    if (tab == 1){
                        isClickWChact = true;
                        // 去下单
                        if (rel_has_address.getVisibility() == View.VISIBLE){
                            if (tv_user_name.getText() != null && !tv_user_name.getText().equals("")){
                                GroupOrderBean bean = new GroupOrderBean();
                                bean.setOpenid(openid);
                                bean.setId(goodsDetail.getId());
                                bean.setAid(getAddressEntity1.getId());
                                bean.setOptionid(goodsDetail.getSpecId());
                                groupsOrder(bean);
                            }else{
                                ToastUtil.makeToast("收货地址不能为空");
                            }
                        }else{
                            ToastUtil.makeToast("收货地址不能为空");
                        }
                    }else{
                        ToastUtil.makeToast("您的积分不足");
                    }
                    break;
                case R.id.rel_edit_address:
                    Intent intent2 = new Intent(BuyExChangeAct.this , ChangeAddressAct.class);
                    startActivityForResult(intent2 , 100);
                    break;
            }
        }
    };

    private void groupsOrder(GroupOrderBean bean){
        GetDataBiz.exChangeOrder(bean , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("积分兑换提交订单的返回" + result);
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonobject = new JSONObject(result);
                if (jsonobject.getInt("code") == 200){
                    ToastUtil.makeToast("下单成功");
                    JSONObject dataJsonObject = jsonobject.getJSONObject("message");

                    if (dataJsonObject.getInt("pay") == 0){
                        //只需积分
                        handler.obtainMessage(300).sendToTarget();
                    }else{
                        //积分 + 钱
                        handler.obtainMessage(200 , dataJsonObject.getString("logid")).sendToTarget();
                    }
                }else{
                    handler.obtainMessage(400 , jsonobject.getString("message")).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i("提交订单的返回失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            switch (resultCode){
                case 1000:
                    rel_no_address.setVisibility(View.GONE);
                    rel_has_address.setVisibility(View.VISIBLE);
                    GetAddressEntity getAddressEntity = (GetAddressEntity) data.getSerializableExtra("getAddressEntity");
                    if (getAddressEntity != null){
                        handler.obtainMessage(2, getAddressEntity).sendToTarget();
                    }
                    break;
            }
        }
    }


    /**
     * 购买下单商品支付弹窗
     * @param orderId
     */
    private void goToPay(String orderId){
        Intent intent = new Intent(this, PayOrderActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        clickListener = null;
        EventBus.getDefault().unregister(this);
        list = null;
        addresssList = null;

        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }

    private void initView() {

        intent = getIntent();
        if (intent != null){
            goodsDetail = (ExGoodsBean) intent.getSerializableExtra("bean");
        }

        rel_bottom = (RelativeLayout) findViewById(R.id.rel_bottom);

        ll_order_back = (LinearLayout) findViewById(R.id.ll_order_back);
        ll_order_back.setOnClickListener(clickListener);

        rel_no_address = (RelativeLayout) findViewById(R.id.rel_no_address);
        rel_no_address.setOnClickListener(clickListener);
        rel_has_address = (RelativeLayout) findViewById(R.id.rel_has_address);
        rel_has_address.setOnClickListener(clickListener);

        rel_edit_address = (RelativeLayout) findViewById(R.id.rel_edit_address);
        rel_edit_address.setOnClickListener(clickListener);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);
        tv_goods_num = (TextView) findViewById(R.id.tv_goods_num);

        iv_goods_image = (ImageView) findViewById(R.id.iv_goods_image);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_prize = (TextView) findViewById(R.id.tv_goods_prize);

        tv_detail_reduce = (TextView) findViewById(R.id.tv_detail_reduce);
        tv_detail_reduce.setOnClickListener(clickListener);
        tv_detail_add = (TextView) findViewById(R.id.tv_detail_add);
        tv_detail_add.setOnClickListener(clickListener);
        et_detail_count = (EditText) findViewById(R.id.et_detail_count);

        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_all_prize = (TextView) findViewById(R.id.tv_all_prize);
        tv_go_order = (TextView) findViewById(R.id.tv_go_order);
        tv_go_order.setOnClickListener(clickListener);

        iv_go_edit_address = (ImageView) findViewById(R.id.iv_go_edit_address);

        ImagerLoaderUtil.displayImageWithLoadingIcon(goodsDetail.getThumb() , iv_goods_image , R.drawable.icon_detail_load);
        tv_goods_name.setText(goodsDetail.getTitle());
        tv_goods_num.setText("×1");
        tv_goods_prize.setText(goodsDetail.getCredit() + "积分");


        tv_all_prize.setText(goodsDetail.getCredit() + "积分+"+ goodsDetail.getMoney() + "元");
        tv_goods_msg = (TextView) findViewById(R.id.tv_goods_msg);
        tv_goods_msg.setText(goodsDetail.getCredit() + "积分+" + goodsDetail.getMoney() + "元");
    }

}
