package com.xinspace.csevent.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.adapter.BrandIconAdapter;
import com.xinspace.csevent.adapter.LatestActivityListAdapter;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.data.biz.GetAdvertisementIconBiz;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.BrandEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.NewcomersTutorialUtil;
import com.xinspace.csevent.util.TimeUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.ActDetailActivity;
import com.xinspace.csevent.ui.activity.AwardPoolActivity;
import com.xinspace.csevent.shop.activity.AwardsTypeActivity;
import com.xinspace.csevent.ui.activity.FullBrandPictureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城
 */
public class ShopFragment extends Fragment{

    private boolean isLoadMore=false;//是否是加载更多
    private LatestActivityListAdapter activityListAdapter;
    private static final int length=20;//默认加载10条活动数据
    private int start =0;//默认从第0条数据起请求
    private List<Object> act_list=new ArrayList<>();//最新活动列表
    private PullToRefreshListView listView;
    private View view;
    private GridView gridView;
    private List<Object> brand_list=new ArrayList<>();//品牌列表
    private NetworkChangeReceiver receiver;
    private BrandIconAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_shop,null);
        setView();
        setListener();
        getAdvertisementIcon();
        getActivityList();

        isShowGuide();
        settingListView();
        registerReceiver();
        return view;
    }
    //判断是否显示新手教程
    private void isShowGuide() {
        NewcomersTutorialUtil.loadToNewcomersTutorial(getActivity(), "shopFragment", R.layout.dialog_tutorial_for_shop);
    }
    //配置上拉加载
    private void settingListView() {
        //上拉加载不要图片,不要更新时间
        ILoadingLayout layout = listView.getLoadingLayoutProxy(false, true);
        layout.setLoadingDrawable(null);
        layout.setReleaseLabel("松开加载");
        layout.setPullLabel("上拉加载");
    }
    //注册广播接收器
    private void registerReceiver() {
        receiver=new NetworkChangeReceiver();
        IntentFilter filter=new IntentFilter(Const.ACTION_CONNECTIVITY_CHANGE);
        getActivity().registerReceiver(receiver,filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消广播接收器的注册
        getActivity().unregisterReceiver(receiver);
    }
    //获取广告商家icon
    private void getAdvertisementIcon() {
        GetAdvertisementIconBiz.getIcon(getActivity(), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                showBrand(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示商家品牌
    private void showBrand(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            String res = obj.getString("result");
            if(res.equals("200")){
                JSONArray ary = obj.getJSONArray("data");
                List<Object> list = JsonPaser.parserAry(ary.toString(), BrandEntity.class);

                brand_list.clear();
                //在第一个位置添加一个拾得活动
                brand_list.add(new BrandEntity());
                //第二个位置添加不二印象
                brand_list.add(new BrandEntity());
                brand_list.addAll(list);

                if(null==adapter){
                    adapter=new BrandIconAdapter(getActivity(),brand_list);
                    gridView.setAdapter(adapter);
                }else{
                    adapter.updateData(brand_list);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //设置监听
    private void setListener() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.i("下拉刷新");
                start=0;
                //调用获取活动列表的方法
                getActivityList();

                //获取品牌信息
                getAdvertisementIcon();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.i("加载更多");
                isLoadMore=true;
                if(act_list.size()!=0){
                    start =act_list.size();
                }
                getActivityList();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=null;
                if(position==0){
                    //拾得活动跳转到奖品类型页面
                    intent=new Intent(getActivity(), AwardsTypeActivity.class);
                }else if(position==1){
                    //不二印象
                    return;
                } else{
                    intent=new Intent(getActivity(), FullBrandPictureActivity.class);
                    BrandEntity enty= (BrandEntity) brand_list.get(position);
                    intent.putExtra("data",enty);
                }
                startActivity(intent);
            }
        });
        //最新活动的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityListEntity enty = (ActivityListEntity) act_list.get(position-2);
                String type=enty.getType();
                LogUtil.i("活动抽奖类型:"+type);
                Intent intent=null;
                if(type.equals("1")){
                    //普通抽奖
                    intent=new Intent(getActivity(),ActDetailActivity.class);

                }else if(type.equals("4")){
                    //游戏抽奖
                    intent=new Intent(getActivity(), AwardPoolActivity.class);
                }
                intent.putExtra("data",enty);
                startActivity(intent);
            }
        });
    }
    //初始化
    private void setView() {
        listView= (PullToRefreshListView) view.findViewById(R.id.lv_shop_listview);
        //添加头部view
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.item_shop_fragment_head_view, null);
        ListView refreshableView = listView.getRefreshableView();
        refreshableView.addHeaderView(headerView);

        gridView= (GridView) headerView.findViewById(R.id.gv_shop_gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));//设置gridview没有点击效果
    }
    //获取活动列表
    private void getActivityList() {
        GetActivityListBiz.getActivityByIndex(String.valueOf(start), String.valueOf(length), CoresunApp.province, CoresunApp.city, CoresunApp.area, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    //刷新完成
                    listView.onRefreshComplete();
                    listView.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());

                    showActivityList(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示活动列表
    private void showActivityList(String result) throws Exception{
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
                if(null==activityListAdapter){
                    activityListAdapter=new LatestActivityListAdapter(getActivity(),actList);
                    listView.setAdapter(activityListAdapter);
                }else{
                    activityListAdapter.updateData(actList);
                }
                act_list.clear();
                act_list.addAll(actList);
            }
        }else if(res.equals("201")){
            ToastUtil.makeToast("没有更多活动");
            isLoadMore=false;
        }
    }
    //网络状态改变的广播接收器
    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Const.ACTION_CONNECTIVITY_CHANGE)){
                getAdvertisementIcon();
                getActivityList();
            }
        }
    }
}
