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

import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.shop.adapter.OrderAdapter;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
import com.xinspace.csevent.shop.modle.OrderGoodsBean;
import com.xinspace.csevent.shop.modle.PlaceOrderBean;
import com.xinspace.csevent.sweepstake.activity.ChangeAddressAct;
import com.xinspace.csevent.sweepstake.modle.FirstEvent;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/4/14.
 *
 * 商城下单
 */

public class BuyGoodsAct extends BaseActivity{

    private LinearLayout ll_order_back;
    private RelativeLayout rel_no_address;
    private RelativeLayout rel_has_address;

    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView tv_user_address;

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
    private GoodsDetailBean goodsDetail;
    private Intent intent;
    private ImageView iv_go_edit_address;
    private int goodsNum;
    private TextView tv_goods_num;
    private String openid;
    private SDPreference preference;
    private RelativeLayout rel_edit_address;
    private String addressId;

    private List<GoodsDetailBean> goodsList;
    private List<OrderGoodsBean> orderGoodsList;
    private MyListView myListView;
    private OrderAdapter adapter;

    private String flag;
    private String allPrice;

    private LinearLayout linshi1;

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
                    Intent intent = new Intent(BuyGoodsAct.this , PayOrderActivity.class);
                    intent.putExtra("orderId" , orderId);
                    intent.putExtra("type" , 0);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtils.setWindowStatusBarColor(BuyGoodsAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_buy_goods);
        EventBus.getDefault().register(this);
        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");
        initView();
        getUserAddresses();
    }

    @Subscribe
    public void onEventMainThread(FirstEvent event) {
        String msg = event.getEventMsg();
        LogUtil.i("-----------www支付返回的结果----------------" + msg);
        if (msg.equals("3")) {

        } else if (msg.equals("4")) {
            BuyGoodsAct.this.finish();
        }
    }


    private void initView() {

        intent = getIntent();
        if (intent != null){
            goodsList = (List<GoodsDetailBean>) intent.getSerializableExtra("data");
            orderGoodsList = (List<OrderGoodsBean>) intent.getSerializableExtra("dataOrder");
            goodsNum = Integer.valueOf(goodsList.get(0).getGoodsNum());
            goodsDetail = goodsList.get(0);
            allPrice = intent.getStringExtra("allPrice");
            flag = intent.getStringExtra("flag");
        }

        ll_order_back = (LinearLayout) findViewById(R.id.ll_order_back);
        ll_order_back.setOnClickListener(clickListener);

        myListView = (MyListView) findViewById(R.id.lv_order);
        adapter = new OrderAdapter(BuyGoodsAct.this);
        adapter.setGoodsList(goodsList);
        myListView.setAdapter(adapter);

        rel_no_address = (RelativeLayout) findViewById(R.id.rel_no_address);
        rel_no_address.setOnClickListener(clickListener);
        rel_has_address = (RelativeLayout) findViewById(R.id.rel_has_address);
        rel_has_address.setOnClickListener(clickListener);

        rel_edit_address = (RelativeLayout) findViewById(R.id.rel_edit_address);
        rel_edit_address.setOnClickListener(clickListener);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);

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

        tv_goods_num = (TextView) findViewById(R.id.tv_goods_num);

        LogUtil.i("图片地址是" + goodsDetail.getThumb());

        if (!goodsDetail.getThumb().contains(AppConfig.BaseNewUrl)){
            ImagerLoaderUtil.displayImage(AppConfig.BaseNewUrl + goodsDetail.getThumb() , iv_goods_image);
        }else{
            ImagerLoaderUtil.displayImage(goodsDetail.getThumb() , iv_goods_image);
        }
        tv_goods_num.setText("×" + goodsNum);
        prize = Float.valueOf(goodsDetail.getMarketprice());
        tv_goods_name.setText(goodsDetail.getTitle());
        tv_goods_prize.setText("¥" + prize + "");

//        BigDecimal b1 = new BigDecimal(Float.toString(goodsNum));
//        BigDecimal b2 = new BigDecimal(Float.toString(prize));
//        allPrice = b1.multiply(b2).floatValue();

        //tv_all.setText("共1件商品 合计：");

        tv_all_prize.setText("¥" + allPrice + "");
        iv_go_edit_address = (ImageView) findViewById(R.id.iv_go_edit_address);
    }

    public void getUserAddresses() {
        AddressManagerBiz.getAddressList( openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null || result.equals("")){
                    return;
                }
                handleAddAddressResult(result);
                Log.i("www", "查询地址的" + result);
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
                    BuyGoodsAct.this.finish();
                    break;
                case R.id.rel_no_address:
                    Intent intent = new Intent(BuyGoodsAct.this , ChangeAddressAct.class);
                    startActivityForResult(intent , 100);
                    break;

                case R.id.tv_go_order:    // 去下单
                    if (rel_has_address.getVisibility() == View.VISIBLE){
                        if (tv_user_name.getText() != null && !tv_user_name.getText().equals("")){
                            PlaceOrderBean placeOrder = new PlaceOrderBean();

                            JSONArray jsonArray = new JSONArray();
                            JSONObject tmpObj = null;
                            int count = orderGoodsList.size();
                            for(int i = 0; i < count; i++)
                            {
                                tmpObj = new JSONObject();
                                try {
                                    tmpObj.put("cates" , orderGoodsList.get(i).getCates());
                                    tmpObj.put("goodsid", orderGoodsList.get(i).getGoodsid());
                                    tmpObj.put("marketprice", orderGoodsList.get(i).getMarketprice());
                                    tmpObj.put("optionid", orderGoodsList.get(i).getOptionid());
                                    tmpObj.put("total", orderGoodsList.get(i).getTotal());
                                    jsonArray.put(tmpObj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            String jsonArrayStr = jsonArray.toString();
                            //String jsonArrayStr = JSON.toJSONString(orderGoodsList, true).toString();
                            placeOrder.setOpenid(openid);
                            placeOrder.setFromcart(flag);
                            placeOrder.setAddressid(addressId);
                            placeOrder.setGoods(jsonArrayStr);
                            placeOrder(placeOrder);
                        }else{
                            ToastUtil.makeToast("收货地址不能为空");
                        }
                    }else{
                        ToastUtil.makeToast("收货地址不能为空");
                    }
                    break;
                case R.id.rel_edit_address:
                    Intent intent2 = new Intent(BuyGoodsAct.this , ChangeAddressAct.class);
                    startActivityForResult(intent2 , 100);
                    break;
            }
        }
    };

    private void placeOrder(PlaceOrderBean placeOrder){
        GetDataBiz.placeOrder2( placeOrder,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("提交订单的返回" + result);
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonobject = new JSONObject(result);
                if (jsonobject.getInt("code") == 200){
                    ToastUtil.makeToast("下单成功");
                    JSONObject dataJsonObject = jsonobject.getJSONObject("data");
                    handler.obtainMessage(200 , dataJsonObject.getString("orderid")).sendToTarget();
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


    @Override
    protected void onDestroy() {
        intent = null;
        clickListener = null;
        handler.removeCallbacksAndMessages(null);
        list = null;
        addresssList = null;
        goodsList = null;
        orderGoodsList = null;
        EventBus.getDefault().unregister(this);
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }


}
