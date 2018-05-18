package com.xinspace.csevent.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.MyExpandableListAdapter;
import com.xinspace.csevent.shop.modle.ShopBean;
import com.xinspace.csevent.shop.modle.ShopCartBean;
import com.xinspace.csevent.shop.weiget.OnShoppingCartChangeListener;
import com.xinspace.csevent.shop.weiget.ShoppingCartBiz;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 购物车列表
 *
 * Created by Android on 2017/6/21.
 */

public class ShopCartAct2 extends BaseActivity{

    private ExpandableListView expandableListView;
    private ImageView ivSelectAll;
    private TextView tvCountMoney;
    private TextView btnSettle;
    private SDPreference preference;
    private String openid;
    private List<ShopBean> allShopList = new ArrayList<>();
    private MyExpandableListAdapter adapter;

    private String countMoney ;
    private String countGoods ;
    private LinearLayout ll_shopcart_back;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allShopList.addAll((Collection<? extends ShopBean>) msg.obj);
                        updateListView();
                    }
                    break;
                case 400:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(ShopCartAct2.this , R.color.app_bottom_color);
        setContentView(R.layout.act_shopcart2);

        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");
        LogUtil.i("openid" + openid);

        initView();
        initData();
    }


    private void initView() {

        ll_shopcart_back = (LinearLayout) findViewById(R.id.ll_shopcart_back);
        ll_shopcart_back.setOnClickListener(onClickListener);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        ivSelectAll = (ImageView) findViewById(R.id.ivSelectAll);
        tvCountMoney = (TextView) findViewById(R.id.tvCountMoney);
        btnSettle = (TextView) findViewById(R.id.btnSettle);

        adapter = new MyExpandableListAdapter(ShopCartAct2.this , openid);
        adapter.setList(allShopList);
        expandableListView.setAdapter(adapter);

        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        adapter.setOnShoppingCartChangeListener(new OnShoppingCartChangeListener() {
            @Override
            public void onDataChange(String selectCount, String selectMoney) {

//                int goodsCount = ShoppingCartBiz.getGoodsCount();
//                if (!isNetworkOk) {//网络状态判断暂时不显示
//                }
//                if (goodsCount == 0) {
//                    showEmpty(true);
//                } else {
//                    showEmpty(false);//其实不需要做这个判断，因为没有商品的时候，必须退出去添加商品；
//                }
                countMoney = String.format(getResources().getString(R.string.count_money), selectMoney);
                countGoods = String.format(getResources().getString(R.string.count_goods), selectCount);
                //String title = String.format(getResources().getString(R.string.shop_title), goodsCount + "");
                tvCountMoney.setText(countMoney);
                btnSettle.setText(countGoods);
                //tvTitle.setText(title);
            }

            @Override
            public void onSelectItem(boolean isSelectedAll) {
                ShoppingCartBiz.checkItem(isSelectedAll, ivSelectAll);
            }
        });


        //通过监听器关联Activity和Adapter的关系，解耦；
        View.OnClickListener listener = adapter.getAdapterListener();
        if (listener != null) {
            //即使换了一个新的Adapter，也要将“全选事件”传递给adapter处理；
            ivSelectAll.setOnClickListener(adapter.getAdapterListener());
            //结算时，一般是需要将数据传给订单界面的
            btnSettle.setOnClickListener(adapter.getAdapterListener());
        }
    }

    private void updateListView() {
        adapter.setList(allShopList);
        adapter.notifyDataSetChanged();
        expandAllGroup();
    }

    /**
     * 展开所有组
     */
    private void expandAllGroup() {
        for (int i = 0; i < allShopList.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }


    private void initData() {
        GetActivityListBiz.getShopCartData(openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取购物车列表数据" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                List<ShopBean> shopList = new ArrayList<ShopBean>();

                List<ShopCartBean> goodsList = new ArrayList<ShopCartBean>();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray listArray = dataObject.getJSONArray("list");
                    for (int i = 0 ; i < listArray.length() ; i++){
                        JSONObject cartObject = listArray.getJSONObject(i);
                        ShopCartBean bean = gson.fromJson(cartObject.toString(),ShopCartBean.class);
                        goodsList.add(bean);
                    }
                    LogUtil.i("购物车的长度" + goodsList.size());

                    ShopBean shopBean = new ShopBean();
                    shopBean.setShopName("拾得商城");
                    shopBean.setShopId("1");
                    shopBean.setEditing(false);
                    shopBean.setList(goodsList);
                    shopList.add(shopBean);

                    mHandler.obtainMessage(200 , shopList).sendToTarget();
                }else{
                    mHandler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_shopcart_back:
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClickListener = null;
        mHandler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
    }
}
