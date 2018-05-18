package com.xinspace.csevent.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.LatestActivityListAdapter;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.SearchAddressFinishListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.BdMapUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.TimeUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.ActDetailActivity;
import com.xinspace.csevent.ui.activity.AwardPoolActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 普通抽奖fragment
 */
public class GeneralAwardFragment extends Fragment implements SearchAddressFinishListener{
    private View view;
    private PullToRefreshListView listView;
    private int start=0;
    private static final int length=10;//默认每页请求的数据条数
    private String province;
    private String city;
    private String district;
    private boolean isLoadMore=false;
    private List<Object> act_list=new ArrayList<>();//活动列表集合
    private LatestActivityListAdapter activityListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_general_award,null);
        getLocation();
        setView();
        setListener();
        return view;
    }
    //获取位置
    private void getLocation() {
        BdMapUtil map = new BdMapUtil();
        map.setOnAddressFinishListener(this);
        map.getLocation();
    }
    //获取活动列表
    private void getAcvitityList() {
        GetActivityListBiz.getActivityByIndex(String.valueOf(start), String.valueOf(length), province, city, district, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                //关闭下拉刷新
                listView.onRefreshComplete();
                listView.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());
                showActivityList(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //设置监听器
    private void setListener() {
        //刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.i("下拉刷新");
                start=0;
                getAcvitityList();
            }
        });
        //listview的最后一项可见的时候加载更多
        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                LogUtil.i("加载更多");
                isLoadMore=true;
                if(act_list.size()!=0){
                    start =act_list.size();
                }
                getAcvitityList();
            }
        });

        //listview 的item监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityListEntity enty = (ActivityListEntity) act_list.get(position - 1);
                Intent intent=null;
                if(enty.getType().equals("1")){
                    //普通抽奖
                    intent=new Intent(getActivity(),ActDetailActivity.class);
                }else if(enty.getType().equals("4")){
                    //抽奖池抽奖
                    intent=new Intent(getActivity(), AwardPoolActivity.class);
                }
                intent.putExtra("data",enty);
                startActivity(intent);
            }
        });
    }
    //初始化组件
    private void setView() {
        listView= (PullToRefreshListView) view.findViewById(R.id.lv_fragment_general_listview);
    }
    //获取当前时间
    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
    //显示活动列表
    private void showActivityList(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            String res=obj.getString("result");
            if(res.equals("200")){
                JSONArray act_ary = obj.getJSONArray("data");
                List<Object> actList = JsonPaser.parserAry(act_ary.toString(), ActivityListEntity.class);

                //如果是加载更多
                if(isLoadMore){
                    isLoadMore=false;
                    act_list.addAll(actList);
                    activityListAdapter.updateData(act_list);
                }else{
                    act_list.clear();
                    act_list.addAll(actList);
                    //设置适配器
                    activityListAdapter=new LatestActivityListAdapter(getActivity(),act_list);
                    listView.setAdapter(activityListAdapter);
                }
            }else if(res.equals("201")){
                isLoadMore=false;
                ToastUtil.makeToast("没有更多活动");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //获取位置回调
    @Override
    public void onSearchFinish(LatLng latLng, String address, String province, String city, String district) {
        this.province=province;
        this.city=city;
        this.district=district;

        //获取活动列表
        getAcvitityList();
    }
}
