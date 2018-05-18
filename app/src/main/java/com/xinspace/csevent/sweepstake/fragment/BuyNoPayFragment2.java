package com.xinspace.csevent.sweepstake.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.AllOrderAdapter2;
import com.xinspace.csevent.shop.adapter.ItemOrderBottom;
import com.xinspace.csevent.shop.adapter.ItemOrderIn;
import com.xinspace.csevent.shop.adapter.ItemOrderTop;
import com.xinspace.csevent.shop.modle.AllOrderBean;
import com.xinspace.csevent.shop.modle.OrderMiddleBean;
import com.xinspace.csevent.shop.weiget.OrderContent;
import com.xinspace.csevent.shop.weiget.ResultBean;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Android on 2016/10/12.
 *
 * 待付款界面
 *
 */
public class BuyNoPayFragment2 extends Fragment{

    private View view;
    private RecyclerView rcNoPay;
    private int start = 1;
    private AllOrderAdapter2 allOrderAdapter;


    private List<AllOrderBean> recordList;
    private List<AllOrderBean> allRecordList;
    private int allSize;
    private boolean isMore = false;
    private SDPreference sdPreference;
    private String openId;

    private List<OrderContent> orderContents;
    private List<OrderContent> allOrderContents;
    private TwinklingRefreshLayout refreshLayout;
    private boolean ERROR = false;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        allRecordList.addAll((Collection<? extends AllOrderBean>) msg.obj);
                        allOrderAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefreshing();
                        refreshLayout.finishLoadmore();
                    }
                    break;
                case 2:
                    refreshLayout.finishRefreshing();
                    refreshLayout.finishLoadmore();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy_no_pay , null);

        sdPreference = SDPreference.getInstance();
        openId = sdPreference.getContent("openid");
        start = 1;
        initView();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        allRecordList.clear();
        start = 1;
        refreshLayout.startRefresh();
        LogUtil.i("onResume-----BuyNoPayFragment2");
    }


    @Override
    public void onStart() {
        super.onStart();
        LogUtil.i("onStart---BuyNoPayFragment2");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.i("onAttach---BuyNoPayFragment2");
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i("onPause-----BuyNoPayFragment2");
    }

    private void initData(int start) {
        GetDataBiz.getUserBuyRecord2( openId , start , "0", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "获取购买记录" + result);
                if (result == null && result.equals("")){
                    return;
                }

                orderContents = new ArrayList<OrderContent>();

                Gson gson=new Gson();
                recordList = new ArrayList<AllOrderBean>();
                JSONObject jsonObject = new JSONObject(result);
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                JSONArray jsonArray = dataJsonObject.getJSONArray("list"); //订单数组
                for (int i = 0 ; i < jsonArray.length() ; i++){

                    JSONObject orderJsonObject = jsonArray.getJSONObject(i);
                    AllOrderBean allOrderBean = gson.fromJson(orderJsonObject.toString(),AllOrderBean.class);

                    String orderId = orderJsonObject.getString("id");

                    ItemOrderTop itemOrderTop = new ItemOrderTop(allOrderBean);
                    orderContents.add(itemOrderTop);

                    JSONArray goodsArray1 = orderJsonObject.getJSONArray("goods"); //店铺数组
                    for (int j = 0 ; j < goodsArray1.length() ; j++){
                        JSONObject storeJsonObject = goodsArray1.getJSONObject(j);
                        JSONArray goodsArray2 = storeJsonObject.getJSONArray("goods"); //店铺的商品数组
                        List<OrderMiddleBean> middleBeanList = new ArrayList<OrderMiddleBean>();
                        for (int m = 0 ; m < goodsArray2.length() ; m++){
                            JSONObject goodsJsonObject = goodsArray2.getJSONObject(m);
                            OrderMiddleBean orderMiddleBean = gson.fromJson(goodsJsonObject.toString(), OrderMiddleBean.class);
                            orderMiddleBean.setOrderId(orderId);
                            ItemOrderIn orderIMiddle = new ItemOrderIn(orderMiddleBean);
                            orderContents.add(orderIMiddle);
                            middleBeanList.add(orderMiddleBean);
                        }
                        allOrderBean.setMiddleBeanList(middleBeanList);
                    }
                    //TODO 设置底部数据-需要的数据直接传
                    ItemOrderBottom orderBottom = new ItemOrderBottom(getActivity() , allOrderBean , "0" , resultBean);
                    orderContents.add(orderBottom);

                    recordList.add(allOrderBean);
                }

                LogUtil.i("全部订单的数目解析" + recordList.size());

                if (orderContents.size() != 0){
                    isMore = true;
                    handler.obtainMessage(1 , recordList).sendToTarget();
                }else {
                    isMore = false;
                    handler.obtainMessage(2).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                ERROR = true;
                LogUtil.i("请求失败了");
            }
        });
    }

    private void initView() {

        allRecordList = new ArrayList<>();
        allOrderContents = new ArrayList<>();

        rcNoPay = (RecyclerView) view.findViewById(R.id.rc_buy_no_pay);
        allOrderAdapter = new AllOrderAdapter2(getActivity() , allRecordList , resultBean);

        rcNoPay.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rcNoPay.setAdapter(allOrderAdapter);
        initFreshLayout();
    }

    /**
     * 下拉刷新加载器
     */
    private void initFreshLayout() {
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.no_pay_record_refreshlayout);
        ProgressLayout header = new ProgressLayout(getActivity());
        header.setColorSchemeResources(R.color.Blue, R.color.Orange, R.color.Yellow, R.color.Green);
        refreshLayout.setHeaderView(header);
        refreshLayout.setFloatRefresh(true);
        refreshLayout.setOverScrollRefreshShow(false);
        refreshLayout.setAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(allRecordList.size() != 0){
                            allRecordList.clear();
                        }
                        start = 1;
                        initData(start);

                    }
                },0);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                LogUtil.e("onLoadmore");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("加载下一页-----------------");
                        start++;
                        initData(start);
                        isMore = false;
                    }
                },1000);
            }


            @Override
            public void onLoadmoreCanceled() {
                LogUtil.e("onLoadmoreCanceled");
                super.onLoadmoreCanceled();
            }

            @Override
            public void onFinishLoadMore() {
                if(ERROR){
                    ERROR=false;
                }else {
                    start++;
                }
                LogUtil.e("onFinishLoadMore");
                super.onFinishLoadMore();
            }
        });
    }

    ResultBean resultBean = new ResultBean() {

        @Override
        public void resultBean(int position) {
            allRecordList.remove(position);
            allOrderAdapter.notifyItemRemoved(position);
            allOrderAdapter.notifyItemRangeChanged(0, allRecordList.size() - 1);
        }
    };
}
