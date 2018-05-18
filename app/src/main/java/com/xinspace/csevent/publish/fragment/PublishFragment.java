package com.xinspace.csevent.publish.fragment;

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
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.publish.adapter.PublishAdapter;
import com.xinspace.csevent.publish.model.PublishBean;
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
 * 众筹揭晓 fragment
 *
 */
public class PublishFragment extends Fragment{

    private View view;
    private PullToRefreshListView lv_crowd_publish;

    private long currentTime;
    private long currentTime1;

    private String startTime;
    private String endTime;

    private List<PublishBean> publishList;
    private List<PublishBean> allPublishList;

    private boolean isMore = true;

    private PublishAdapter adapter ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        allPublishList.addAll((List<PublishBean>)msg.obj);
                        adapter.setPublishList(allPublishList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    lv_crowd_publish.onRefreshComplete();
                    ToastUtil.makeToast("没有更多数据");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_publish , null);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        allPublishList.clear();
        initData();
    }


    private void initView() {
        currentTime = System.currentTimeMillis();
        currentTime1 = currentTime - 48*60*60*1000;
        endTime = TimeHelper.getDateString(String.valueOf(currentTime));
        startTime = TimeHelper.getDateString(String.valueOf(currentTime1));
        allPublishList = new ArrayList<PublishBean>();
        LogUtil.i("startTime" + startTime + "endTime" + endTime);

        lv_crowd_publish = (PullToRefreshListView) view.findViewById(R.id.lv_crowd_publish);
        lv_crowd_publish.setMode(PullToRefreshBase.Mode.BOTH);

        adapter = new PublishAdapter(getActivity());
        adapter.setPublishList(allPublishList);
        lv_crowd_publish.setAdapter(adapter);
        lv_crowd_publish.setOnRefreshListener(onReFreshListener);
    }

    PullToRefreshBase.OnRefreshListener2<ListView> onReFreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            allPublishList.clear();
            initData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (isMore){
                initData();
            }else{
                handler.obtainMessage(2).sendToTarget();
            }
        }
    } ;


    private void initData() {
        GetDataBiz.crowdPublish(getActivity() , startTime, endTime, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {

                lv_crowd_publish.onRefreshComplete();
                lv_crowd_publish.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());

                Log.i("www", "获取众筹揭晓数据" + result);
                publishList = new ArrayList<PublishBean>();
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
                        PublishBean publishBean = gson.fromJson(jsonObject.toString(), PublishBean.class);
                        LogUtil.i("publishBean" + publishBean.getNoactivity() + "aaa" + publishBean.getId());

                        String wintime = publishBean.getWintime();

                        long winTime = TimeHelper.getStringToDate(wintime);
                        long currentTime2 = System.currentTimeMillis();

//                        int oneTime = wintime.indexOf(":");
//                        int twoTime = wintime.lastIndexOf(":");
//                        String minute = wintime.substring(oneTime + 1 , twoTime);
//                        int downTime;
//                        LogUtil.i("www" + minute );
//                        if (minute.substring(1 , 2).equals("0")){
//                            downTime = countDownTime(Integer.valueOf(minute.substring(0 , 1))) * 1000;
//                        }else{
//                            downTime = countDownTime(Integer.valueOf(minute)) * 1000;
//                        }
                        publishBean.setChaTime(currentTime2 - winTime);
                        publishBean.setDownTime(10 * 60 * 1000);
                        LogUtil.i("winTime" + winTime + "currentTime2" +currentTime2 + "cha" + (currentTime2 - winTime));

                        publishList.add(publishBean);
                    }

                    LogUtil.i("解析publishList长度" + publishList.size());

                    if (publishList.size() != 0){
                        handler.obtainMessage(1 , publishList).sendToTarget();
                        isMore = false;
                    }else {
                        isMore = false;
                        handler.obtainMessage(2).sendToTarget();
                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i("获取众筹众筹揭晓数据");
            }
        });
    }

}
