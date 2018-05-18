package com.xinspace.csevent.sweepstake.fragment;

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
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.AllOrderAdapter;
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
 * 待发货
 */
public class BuyNoSendFragment extends Fragment{

    private View view;
    private RecyclerView rcNoSend;
    private int start = 1;
    private AllOrderAdapter allOrderAdapter;

    private List<AllOrderBean> recordList;
    private List<AllOrderBean> allRecordList;
    private int allSize;
    private boolean isMore = false;
    private SDPreference sdPreference;
    private String openId;

    private List<OrderContent> orderContents;
    private List<OrderContent> allOrderContents;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        allOrderContents.addAll((Collection<? extends OrderContent>) msg.obj);
                        allOrderAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    ToastUtil.makeToast("没有更多数据");
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy_no_send , null);
        sdPreference = SDPreference.getInstance();
        openId = sdPreference.getContent("openid");
        start = 1;
        initData(start);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initData(int start) {
        GetDataBiz.getUserBuyRecord2( openId , start , "1", new HttpRequestListener() {
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
                    AllOrderBean allOrderBean = gson.fromJson(orderJsonObject.toString(), AllOrderBean.class);
                    recordList.add(allOrderBean);

                    String orderId = orderJsonObject.getString("id");

                    ItemOrderTop itemOrderTop = new ItemOrderTop(allOrderBean);
                    orderContents.add(itemOrderTop);

                    //LogUtil.i("iiiiiiiiiiiiiiiii" + i);

                    JSONArray goodsArray1 = orderJsonObject.getJSONArray("goods"); //店铺数组
                    for (int j = 0 ; j < goodsArray1.length() ; j++){

                        //LogUtil.i("jjjjjjjjjjjjjjjjjjj" + j);

                        JSONObject storeJsonObject = goodsArray1.getJSONObject(j);
                        JSONArray goodsArray2 = storeJsonObject.getJSONArray("goods"); //店铺的商品数组
                        List<OrderMiddleBean> middleBeanList = new ArrayList<OrderMiddleBean>();

                        for (int m = 0 ; m < goodsArray2.length() ; m++){

                            //LogUtil.i("mmmmmmmmmmmmmm" + m);

                            JSONObject goodsJsonObject = goodsArray2.getJSONObject(m);
                            OrderMiddleBean orderMiddleBean = gson.fromJson(goodsJsonObject.toString(), OrderMiddleBean.class);
                            orderMiddleBean.setOrderId(orderId);
                            middleBeanList.add(orderMiddleBean);
                            ItemOrderIn orderIMiddle = new ItemOrderIn(orderMiddleBean);
                            orderContents.add(orderIMiddle);
                        }
                    }
                    //TODO 设置底部数据-需要的数据直接传
                    ItemOrderBottom orderBottom = new ItemOrderBottom(getActivity() , allOrderBean , "2" , resultBean);
                    orderContents.add(orderBottom);
                }

                LogUtil.i("未发货订单的数目解析" + recordList.size());

                if (orderContents.size() != 0){
                    isMore = true;
                    handler.obtainMessage(1 , orderContents).sendToTarget();
                }else {
                    isMore = false;
                    handler.obtainMessage(2).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i("请求失败了");
            }
        });
    }

    private void initView() {

        allRecordList = new ArrayList<AllOrderBean>();
        allOrderContents = new ArrayList<>();

        rcNoSend = (RecyclerView) view.findViewById(R.id.rc_buy_no_send);
        allOrderAdapter = new AllOrderAdapter(getActivity() , allOrderContents);

        rcNoSend.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rcNoSend.setAdapter(allOrderAdapter);

        rcNoSend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                if (allOrderAdapter.getItemCount() > 4 && lastCompletelyVisibleItemPosition == allOrderAdapter.getItemCount() - 1 && isMore) {
                    LogUtil.i("加载下一页-----------------");
                    start++;
                    initData(start);
                    isMore = false;
                }
            }
        });
    }

    ResultBean resultBean = new ResultBean() {
        @Override
        public void resultBean(int position) {

        }
    };

}
