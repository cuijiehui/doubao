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
import com.xinspace.csevent.login.adapter.CWRllAdapter;
import com.xinspace.csevent.login.model.CrowdWinRecord;
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
 * Created by Android on 2016/10/18.
 * 
 * 全部中奖纪录
 */
public class CWNoRecevingFragment extends Fragment{
    
    private View view;
    private PullToRefreshListView lv_crowd_win_record_all;

    private CWRllAdapter adapter;
    private int start = 0;
    private List<CrowdWinRecord> recordList;
    private List<CrowdWinRecord> allRecordList;
    private int allSize;
    private boolean isMore = true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        List<CrowdWinRecord> list = (List<CrowdWinRecord>)msg.obj;
                        List<CrowdWinRecord> noSendList = new ArrayList<CrowdWinRecord>();
                        for (int i = 0 ; i < list.size() ; i++){
                            if (list.get(i).getStart() != null && list.get(i).getStart().equals("1") && list.get(i).getConfirm().equals("0")){
                                noSendList.add(list.get(i));
                            }
                        }
                        handler.obtainMessage(3 , noSendList).sendToTarget();
//                        allRecordList.addAll((List<CrowdWinRecord>)msg.obj);
//                        adapter.setWinRecordList(allRecordList);
//                        allSize = allRecordList.size();
//                        Log.i("wwww" , "allSize" + allSize);
//                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    lv_crowd_win_record_all.onRefreshComplete();
                    ToastUtil.makeToast("没有更多数据");
                    break;
                case 3:
                    if (msg.obj != null){
                        allRecordList.addAll((List<CrowdWinRecord>)msg.obj);
                        adapter.setWinRecordList(allRecordList);
                        allSize = allRecordList.size();
                        Log.i("wwww" , "allSize" + allSize);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        allRecordList.clear();
        start = 0;
        initData(start);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cw_all , null);
        initView();
        return view;
    }

    private void initView() {
        allRecordList = new ArrayList<CrowdWinRecord>();
        lv_crowd_win_record_all = (PullToRefreshListView) view.findViewById(R.id.lv_crowd_win_record_all);
        lv_crowd_win_record_all.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new CWRllAdapter(getActivity());
        adapter.setWinRecordList(allRecordList);
        lv_crowd_win_record_all.setAdapter(adapter);
        lv_crowd_win_record_all.setOnRefreshListener(onReFreshListener);

    }


    private void initData(int start) {
        GetDataBiz.crowdWinRecord(getActivity() , start, "5", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "获取众筹中奖记录" + result);
                lv_crowd_win_record_all.onRefreshComplete();
                lv_crowd_win_record_all.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());
                recordList = new ArrayList<CrowdWinRecord>();

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("allprice")){
                    JSONArray jsonArray = jsonObject.getJSONArray("allprice");
                    LogUtil.i("数组长度" + jsonArray.length());

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Gson gson=new Gson();
                        CrowdWinRecord crowdWinRecord = gson.fromJson(jsonObject1.toString(), CrowdWinRecord.class);
                        recordList.add(crowdWinRecord);
                    }
                }

                LogUtil.i("解析众筹中奖记录" + recordList.size());
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
