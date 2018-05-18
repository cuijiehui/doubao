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
import com.xinspace.csevent.publish.adapter.CPartRecordAdapter;
import com.xinspace.csevent.publish.model.PartRecordBean;
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
 * 众筹抽奖某一期的抽奖记录
 *
 * Created by Android on 2016/10/21.
 */
public class CrowdParticipaAct extends BaseActivity{

    private LinearLayout ll_back;
    private PullToRefreshListView lv_crowd_participa;

    private String aid;
    private Intent intent;

    private List<PartRecordBean> dataList;
    private List<PartRecordBean> allDataList;

    private CPartRecordAdapter adapter;

    private boolean isMore;
    private int allSize;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        allDataList.addAll((List<PartRecordBean>)msg.obj);
                        adapter.setDataList(allDataList);
                        allSize = allDataList.size();
                        Log.i("wwww" , "allSize" + allSize);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    lv_crowd_participa.onRefreshComplete();
                    ToastUtil.makeToast("没有更多数据");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_crowd_participa);

        intent = getIntent();
        if (intent != null){
            aid = intent.getStringExtra("aid");
        }
        allDataList = new ArrayList<PartRecordBean>();

        initView();
        initData(aid , 0);
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(onClickListener);
        lv_crowd_participa = (PullToRefreshListView) findViewById(R.id.lv_crowd_participa);
        lv_crowd_participa.setMode(PullToRefreshBase.Mode.BOTH);
        lv_crowd_participa.setOnRefreshListener(onReFreshListener);

        adapter = new CPartRecordAdapter(CrowdParticipaAct.this);
        adapter.setDataList(allDataList);
        lv_crowd_participa.setAdapter(adapter);
    }


    private void initData(String cid , int start) {
        GetDataBiz.crowdPartRecord(CrowdParticipaAct.this , start , "5", cid ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "获取众筹抽奖记录" + result);

                lv_crowd_participa.onRefreshComplete();
                lv_crowd_participa.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());

                JSONObject jsONObject = new JSONObject(result);
                if (jsONObject.has("result")){
                    if (jsONObject.getString("result").equals("200")){
                        JSONArray jsonArray = jsONObject.getJSONArray("data");
                        dataList = new ArrayList<PartRecordBean>();
                        for (int i = 0 ; i < jsonArray.length() ; i++){
                            LogUtil.i("i" + i);
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Gson gson=new Gson();
                            PartRecordBean bean = gson.fromJson(jsonObject1.toString(), PartRecordBean.class);
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
            initData(aid , start);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (isMore){
                initData(aid , allSize);
            }else{
                handler.obtainMessage(2).sendToTarget();
            }
        }
    } ;

}
