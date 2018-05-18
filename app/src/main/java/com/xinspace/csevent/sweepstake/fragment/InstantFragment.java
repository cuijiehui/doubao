package com.xinspace.csevent.sweepstake.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.model.LatLng;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.SearchAddressFinishListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.BdMapUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Android on 2016/9/23.
 */
public class InstantFragment extends Fragment implements SearchAddressFinishListener {

    private RecyclerView recycler_instant;
    private View view;
    private int start =0;//默认从第0条数据起请求
    private static final int length=20;//默认加载10条活动数据
    private String province;//省
    private String city;
    private String district;
    private BdMapUtil map;
    private boolean isLoadMore;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_instant , null);
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView() {
        getLocation();
        recycler_instant = (RecyclerView) view.findViewById(R.id.recycler_instant);
        //getActivityList();

    }

    //获取活动列表
    private void getActivityList() {
        GetActivityListBiz.getActivityByIndex(String.valueOf(start), String.valueOf(length), province, city, district, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    Log.i("www" , "获取活动列表fragment" + result);
                    if (result == null && result.equals("")){
                        return;
                    }
                    //showActivityList(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    private void showActivityList(String result) throws Exception{
        JSONObject obj = new JSONObject(result);
        String res = obj.getString("result");
        if(res.equals("200")){
            JSONArray act_ary = obj.getJSONArray("data");
            List<Object> actList = JsonPaser.parserAry(act_ary.toString(), ActivityListEntity.class);
            Log.i("www" , "解析list长度" + actList.size());
            //如果是加载更多
//            if(isLoadMore){
//                Log.i("www" , "进来刷新适配");
//                isLoadMore=false;
//                act_list.addAll(actList);
//                activityListAdapter.updateData(act_list);
//            }else{
//                act_list.clear();
//                act_list.addAll(actList);
//                if(null==activityListAdapter){
//                    activityListAdapter=new GridViewActivityAdapter(getActivity(),act_list);
//                    //listView.setAdapter(activityListAdapter);
//                }else{
//                    activityListAdapter.updateData(act_list);
//                }
//            }
        }else if(res.equals("201")){
            ToastUtil.makeToast("没有更多活动");
            isLoadMore=false;
        }
    }

    //获取当前位置
    private void getLocation() {
        map = new BdMapUtil();
        map.setOnAddressFinishListener(this);
        map.getLocation();
    }

    @Override
    public void onSearchFinish(LatLng latLng, String address, String province, String city, String district) {
        if(null==province){
            map.getLocation();
            return;
        }
        this.province=province;
        this.city=city;
        this.district=district;
        //将位置信息保存到全局
        CoresunApp.province=province;
        CoresunApp.city=city;
        CoresunApp.area=district;

        //调用获取活动列表的方法
        getActivityList();
    }
}
