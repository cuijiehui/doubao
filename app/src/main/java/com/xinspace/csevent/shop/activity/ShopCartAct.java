package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.ShopCartAdapter;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
import com.xinspace.csevent.shop.modle.OrderGoodsBean;
import com.xinspace.csevent.shop.modle.ShopCartBean;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 购物车
 *
 * Created by Android on 2017/5/3.
 */

public class ShopCartAct extends BaseActivity implements CompoundButton.OnCheckedChangeListener{

    private LinearLayout ll_shopcart_back;
    private SDPreference preference;
    private String openid;
    private List<ShopCartBean> allList = new ArrayList<ShopCartBean>();
    private ListView lv_shop_cart;
    private ShopCartAdapter shopCartAdapter;
    private HashMap<ShopCartBean, Boolean> isSelected;
    private CheckBox checkbox_all;
    private TextView tv_total;
    private TextView tv_gotobuy;

    private List<GoodsDetailBean> goodsList;
    private List<OrderGoodsBean> orderGoodsList;   // 选中下单的list
    private String specId = "";
    private String marketprice;
    private List<ShopCartBean> chShopCartList;
    float totalPrice = 0f;

    private RelativeLayout lin_has_content;
    private RelativeLayout rel_no_content;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    checkbox_all.setChecked(isSelected.size() == allList.size());
                    break;
                case 10:
                    int count = 0;
                    totalPrice = 0 ;
                    Iterator<Map.Entry<ShopCartBean, Boolean>> iterator = isSelected.entrySet().iterator();
                    if (chShopCartList.size() != 0){
                        chShopCartList.clear();
                    }
                    LogUtil.i("选择的isSelected" + isSelected.size());
                    for (int i = 0 ; i < isSelected.size() ; i++){
                        count++;
                        Map.Entry<ShopCartBean, Boolean> entry = iterator.next();
                        ShopCartBean shopCart = entry.getKey();
                        chShopCartList.add(shopCart);
                        totalPrice += Float.valueOf(shopCart.getTotal()) * Float.valueOf((shopCart.getMarketprice()));
                    }

                    LogUtil.i("isSelected" + isSelected.size() + " totalPrice" + totalPrice + "    count" + count);

                    tv_total.setText(getString(R.string.total, String.format("%.2f", totalPrice)));
                    tv_gotobuy.setText("去结算（" + count + "）");

                    break;
                case 200:
                    if (msg.obj != null){
                        LogUtil.i("进来加载数据了");
                        allList.addAll((Collection<? extends ShopCartBean>) msg.obj);
                        if (allList.size() != 0){

                            rel_no_content.setVisibility(View.GONE);
                            lin_has_content.setVisibility(View.VISIBLE);

                            shopCartAdapter.setList(allList);
                            shopCartAdapter.notifyDataSetChanged();
                        }else{
                            LogUtil.i("--------------------------------------------");
                            rel_no_content.setVisibility(View.VISIBLE);
                            lin_has_content.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 400:
                    ToastUtil.makeToast("数据出现错误");
                    break;
                case 500:
//                    allList.clear();
//                    initData();
//                    tv_total.setText("");
//                    tv_gotobuy.setText("去结算（" + 0 + "）");
                    shopCartAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(ShopCartAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_shopcart);
        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");
        LogUtil.i("openid" + openid);

        chShopCartList = new ArrayList<>();
        goodsList = new ArrayList<GoodsDetailBean>();
        orderGoodsList = new ArrayList<OrderGoodsBean>();

        initView();
        initData();
    }

    private void initData() {
        GetActivityListBiz.getShopCartData(openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取购物车列表数据" + result);
                Gson gson = new Gson();
                List<ShopCartBean> list = new ArrayList<ShopCartBean>();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray listArray = dataObject.getJSONArray("list");
                    for (int i = 0 ; i < listArray.length() ; i++){
                        JSONObject cartObject = listArray.getJSONObject(i);
                        ShopCartBean bean = gson.fromJson(cartObject.toString(),ShopCartBean.class);
                        list.add(bean);
                    }
                    LogUtil.i("购物车的长度" + list.size());
                    mHandler.obtainMessage(200 , list).sendToTarget();
                }else{
                    mHandler.obtainMessage(400 , list).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                //mHandler.obtainMessage(400).sendToTarget();
            }
        });
    }

    private void initView() {
        ll_shopcart_back = (LinearLayout) findViewById(R.id.ll_shopcart_back);
        ll_shopcart_back.setOnClickListener(onClickListener);

        tv_total = (TextView) findViewById(R.id.tv_total);

        tv_gotobuy = (TextView) findViewById(R.id.tv_gotobuy);
        tv_gotobuy.setOnClickListener(onClickListener);

        isSelected = new HashMap<ShopCartBean, Boolean>();
        checkbox_all = (CheckBox) findViewById(R.id.checkbox_all);
        checkbox_all.setOnCheckedChangeListener(this);

        lv_shop_cart = (ListView) findViewById(R.id.lv_shop_cart);
        shopCartAdapter = new ShopCartAdapter(ShopCartAct.this , isSelected , mHandler , openid);
        shopCartAdapter.setList(allList);
        lv_shop_cart.setAdapter(shopCartAdapter);

        lin_has_content = (RelativeLayout) findViewById(R.id.lin_has_content);
        rel_no_content = (RelativeLayout) findViewById(R.id.rel_no_content);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_shopcart_back:
                    finish();
                    break;
                case R.id.tv_gotobuy: // 点击购买

                    if (goodsList.size()!= 0){
                        goodsList.clear();
                    }

                    if (orderGoodsList.size() != 0){
                        orderGoodsList.clear();
                    }

                    for (int i = 0 ; i < chShopCartList.size() ; i++){
                        GoodsDetailBean goodsDetailBean = new GoodsDetailBean();
                        goodsDetailBean.setGoodsNum(chShopCartList.get(i).getTotal());
                        goodsDetailBean.setMarketprice(chShopCartList.get(i).getMarketprice());
                        goodsDetailBean.setThumb(chShopCartList.get(i).getThumb());
                        goodsDetailBean.setTitle(chShopCartList.get(i).getTitle());

                        OrderGoodsBean orderGoodsBean = new OrderGoodsBean();
                        orderGoodsBean.setTotal(chShopCartList.get(i).getTotal());
                        orderGoodsBean.setOptionid(chShopCartList.get(i).getOptionid());
                        orderGoodsBean.setMarketprice(chShopCartList.get(i).getMarketprice());
                        //orderGoodsBean.setCates("");
                        orderGoodsBean.setGoodsid(chShopCartList.get(i).getGoodsid());
                        goodsList.add(goodsDetailBean);
                        orderGoodsList.add(orderGoodsBean);
                    }

                    Intent intent = new Intent(ShopCartAct.this , BuyGoodsAct.class);
                    intent.putExtra("data" , (Serializable) goodsList);
                    intent.putExtra("dataOrder" , (Serializable) orderGoodsList);
                    intent.putExtra("allPrice",String.valueOf(totalPrice));
                    intent.putExtra("flag" , "0");
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        LogUtil.i("全选isChecked" + isChecked);
        if (!isChecked && isSelected.size() != allList.size()) {
            return;
        }
        isSelected.clear();
        if (isChecked) {
            for (int i = 0; i < allList.size(); i++) {
                isSelected.put(allList.get(i), true);
            }
        }
        shopCartAdapter.notifyDataSetChanged();
        mHandler.sendEmptyMessage(1);
    }

    @Override
    protected void onDestroy() {
        onClickListener = null;
        this.setContentView(R.layout.empty_view);
        goodsList = null;
        orderGoodsList = null;
        mHandler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
