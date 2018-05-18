package com.xinspace.csevent.baskorder.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.baskorder.adapter.BaskOrderAdapter;
import com.xinspace.csevent.baskorder.model.BaskOrder;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.TimeUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/10/31.
 *
 * 我的晒单
 *
 */
public class MyBaskOrderAct extends BaseActivity{

    private PullToRefreshListView lv_my_bask_order;
    private LinearLayout ll_back;

    private String length = "5";

    private List<BaskOrder> dataList;
    private List<BaskOrder> allDataList;
    private BaskOrderAdapter adapter ;
    private boolean isMore = true;
    private int allSize;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if ((List<BaskOrder>)msg.obj != null){
                        allDataList.addAll((List<BaskOrder>) msg.obj);
                        adapter.setDataList(allDataList);
                        allSize = allDataList.size();
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    lv_my_bask_order.onRefreshComplete();
                    ToastUtil.makeToast("没有更多数据");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_bask_order);
        initView();
        initData("0");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
        handler.removeCallbacksAndMessages(null);
    }


    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);
        lv_my_bask_order = (PullToRefreshListView) findViewById(R.id.lv_my_bask_order);

        allDataList = new ArrayList<BaskOrder>();
        lv_my_bask_order.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new BaskOrderAdapter(MyBaskOrderAct.this);
        adapter.setDataList(allDataList);
        lv_my_bask_order.setAdapter(adapter);
        lv_my_bask_order.setOnRefreshListener(onReFreshListener);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:

                    MyBaskOrderAct.this.finish();

                    break;
            }
        }
    };


    PullToRefreshBase.OnRefreshListener2<ListView> onReFreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {


        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            allDataList.clear();
            initData("0");
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (isMore){
                initData(String.valueOf(allSize));
            }else{
                handler.obtainMessage(2).sendToTarget();
            }
        }
    } ;


    private void initData(String start) {
        GetDataBiz.getMyBaskOrderList(MyBaskOrderAct.this , start, length, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {

                lv_my_bask_order.onRefreshComplete();
                lv_my_bask_order.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());
                Log.i("www", "获取我的晒单列表数据" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("result").equals("200")){
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    dataList = new ArrayList<BaskOrder>();
                    JSONArray jsonArray1 = dataObject.getJSONArray("Immediate");
                    for (int i = 0 ; i < jsonArray1.length() ; i++){
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        BaskOrder baskOrder = new BaskOrder();
                        baskOrder.setName(jsonObject1.getString("name"));
                        baskOrder.setStartdate(jsonObject1.getString("startdate"));
                        baskOrder.setMatch(jsonObject1.getString("match"));
                        baskOrder.setNoactivity(jsonObject1.getString("noactivity"));
                        baskOrder.setId(jsonObject1.getString("id"));
                        baskOrder.setSid(jsonObject1.getString("sid"));
                        baskOrder.setComment(jsonObject1.getString("comment"));
                        baskOrder.setTitle(jsonObject1.getString("title"));
                        baskOrder.setDatetime(jsonObject1.getString("datetime"));
                        baskOrder.setAll_match(jsonObject1.getString("all_match"));
                        baskOrder.setUsername(jsonObject1.getString("username"));
                        baskOrder.setImg(jsonObject1.getString("img"));

                        List<String> smallImgList = new ArrayList<String>();
                        JSONArray smallArray = jsonObject1.getJSONArray("smallImg");
                        for (int m = 0 ; m < smallArray.length() ; m++){
                            smallImgList.add(smallArray.getString(m));
                        }
                        baskOrder.setSmallList(smallImgList);

                        List<String> bigImgList = new ArrayList<String>();
                        JSONArray bigArray = jsonObject1.getJSONArray("BigImg");
                        for (int n = 0 ; n < bigArray.length() ; n++){
                            bigImgList.add(bigArray.getString(n));
                        }
                        baskOrder.setBigList(bigImgList);
                        dataList.add(baskOrder);
                    }

                    JSONArray jsonArray2 = dataObject.getJSONArray("indiana");
                }

                if (dataList.size() != 0){
                    handler.obtainMessage(1 , dataList).sendToTarget();
                }else{
                    handler.obtainMessage(2).sendToTarget();
                    isMore = false;
                }

            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i("获取晒单列表数据");
            }
        });
    }

}
