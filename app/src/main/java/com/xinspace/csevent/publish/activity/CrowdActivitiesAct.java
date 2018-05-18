package com.xinspace.csevent.publish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.publish.adapter.CActivitiesAdapter;
import com.xinspace.csevent.publish.model.PublishActivitiesBean;
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
 * 众筹往期揭晓揭记录
 *
 * Created by Android on 2016/10/21.
 */
public class CrowdActivitiesAct extends BaseActivity{

    private LinearLayout ll_back;
    private PullToRefreshListView lv_crowd_a_publish;

    private String cid;
    private Intent intent;

    private List<PublishActivitiesBean> dataList;
    private List<PublishActivitiesBean> allDataList;

    private CActivitiesAdapter adapter;

    private boolean isMore;
    private int allSize;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        allDataList.addAll((List<PublishActivitiesBean>)msg.obj);
                        adapter.setDataList(allDataList);
                        allSize = allDataList.size();
                        Log.i("wwww" , "allSize" + allSize);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    lv_crowd_a_publish.onRefreshComplete();
                    ToastUtil.makeToast("没有更多数据");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_crowd_activities);

        intent = getIntent();
        if (intent != null){
            cid = intent.getStringExtra("cid");
        }
        allDataList = new ArrayList<PublishActivitiesBean>();

        initView();
        initData(cid , 0);
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(onClickListener);
        lv_crowd_a_publish = (PullToRefreshListView) findViewById(R.id.lv_crowd_a_publish);
        lv_crowd_a_publish.setMode(PullToRefreshBase.Mode.BOTH);
        lv_crowd_a_publish.setOnRefreshListener(onReFreshListener);

        adapter = new CActivitiesAdapter(CrowdActivitiesAct.this);
        adapter.setDataList(allDataList);
        lv_crowd_a_publish.setAdapter(adapter);
    }


    private void initData(String cid , int start) {
        GetDataBiz.crowdPublishActivities(CrowdActivitiesAct.this , start , "5", cid ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "获取众筹往期揭晓" + result);

                lv_crowd_a_publish.onRefreshComplete();
                lv_crowd_a_publish.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());

                JSONObject jsONObject = new JSONObject(result);
                if (jsONObject.has("result")){
                    if (jsONObject.getString("result").equals("200")){
                        JSONArray jsonArray = jsONObject.getJSONArray("data");
                        dataList = new ArrayList<PublishActivitiesBean>();
                        for (int i = 0 ; i < jsonArray.length() ; i++){
                            LogUtil.i("i" + i);
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Gson gson=new Gson();
                            PublishActivitiesBean bean = gson.fromJson(jsonObject1.toString(), PublishActivitiesBean.class);
                            dataList.add(bean);
                        }

                        LogUtil.i("size" + dataList.size());

                        if (dataList.size() != 0){
                            isMore = true;
                            handler.obtainMessage(1 , dataList).sendToTarget();
                        }else {
                            isMore = false;
                            handler.obtainMessage(2).sendToTarget();
                        }

                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i("获取众筹抽奖记录失败");
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    finish();
                    break;
            }
        }
    };


    PullToRefreshBase.OnRefreshListener2<ListView> onReFreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            allDataList.clear();
            int start = 0;
            initData(cid , start);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (isMore){
                initData(cid , allSize);
            }else{
                handler.obtainMessage(2).sendToTarget();
            }
        }
    } ;

}
