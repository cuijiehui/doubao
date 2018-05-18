package com.xinspace.csevent.login.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.login.adapter.CRAllAdapter;
import com.xinspace.csevent.login.model.CrowdRecord;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.TimeUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/10/14.
 *
 * 众筹抽奖全部记录
 */
public class CRAllFragment extends Fragment{

    private View view;
    private PullToRefreshListView lv_crowd_record_all;
    private CRAllAdapter adapter;
    private int start = 0;

    private List<CrowdRecord> recordList;
    private List<CrowdRecord> allRecordList;
    private int allSize;
    private boolean isMore = true;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        allRecordList.addAll((List<CrowdRecord>)msg.obj);
                        adapter.setRecordList(allRecordList);
                        allSize = allRecordList.size();
                        Log.i("wwww" , "allSize" + allSize);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    lv_crowd_record_all.onRefreshComplete();
                    ToastUtil.makeToast("没有更多数据");
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cr_all , null);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        allRecordList.clear();
        start = 0;
        initData(start);
    }


    private void initData(int start) {
        GetDataBiz.crowdRecord(getActivity() , start, "5", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "获取众筹抽奖记录" + result);
                lv_crowd_record_all.onRefreshComplete();
                lv_crowd_record_all.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());
                recordList = new ArrayList<CrowdRecord>();

                if (result != null){
                    int one = result.indexOf("[");
                    int two = result.lastIndexOf("]");
                    String result2 = result.substring(one , two + 1);
                    LogUtil.i("one" + one + "two" + two + "result2" + result2);

                    JSONArray jsonArray = new JSONArray(result2);

                    LogUtil.i("数组长度" + jsonArray.length());

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson=new Gson();
                        CrowdRecord crowdRecord = gson.fromJson(jsonObject.toString(), CrowdRecord.class);
                        recordList.add(crowdRecord);
                    }
                }

                LogUtil.i("解析众筹抽奖记录" + recordList.size());

                if (recordList.size() != 0){
                    handler.obtainMessage(1 , recordList).sendToTarget();
                }else {
                    isMore = false;
                    handler.obtainMessage(2).sendToTarget();
                }

            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i("获取众筹抽奖记录失败");
            }
        });

    }

    private void initView() {

        allRecordList = new ArrayList<CrowdRecord>();
        lv_crowd_record_all = (PullToRefreshListView) view.findViewById(R.id.lv_crowd_record_all);
        lv_crowd_record_all.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new CRAllAdapter(getActivity());
        adapter.setRecordList(allRecordList);
        lv_crowd_record_all.setAdapter(adapter);
        lv_crowd_record_all.setOnRefreshListener(onReFreshListener);

    }


    PullToRefreshBase.OnRefreshListener2<ListView> onReFreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            allRecordList.clear();
            start = 0;
            initData(start);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (isMore){
                initData(allSize);
            }else{
                handler.obtainMessage(2).sendToTarget();
            }
        }
    } ;


}
