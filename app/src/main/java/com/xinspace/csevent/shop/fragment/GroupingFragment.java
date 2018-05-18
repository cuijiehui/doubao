package com.xinspace.csevent.shop.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.GroupFailAdapter;
import com.xinspace.csevent.shop.modle.GroupFailBean;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 拼团中
 * <p>
 * Created by Android on 2017/6/12.
 */

public class GroupingFragment extends Fragment {

    private View view;
    private RecyclerView recycle_group;
    private SDPreference preference;
    private int page = 1;
    private String openid;
    private String status = "0";
    private List<GroupFailBean> allList = new ArrayList<>();
    private GroupFailAdapter adapter;
    private boolean isMore = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends GroupFailBean>) msg.obj);
                        LogUtil.i("www" + allList.size());
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    ToastUtil.makeToast("暂无拼团数据");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grouping, null);

        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");

        initData(page);
        initView();
        return view;
    }

    private void initData(int page) {

        GetDataBiz.getGroupingData(openid , status , String.valueOf(page) ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("拼团中" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                List<GroupFailBean> beanList = new ArrayList<GroupFailBean>();
                if (jsonObject.getString("code").equals("200")) {
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = dataJsonObject.getJSONArray("list");

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject beanObject = jsonArray.getJSONObject(i);
                        GroupFailBean bean = gson.fromJson(beanObject.toString() , GroupFailBean.class);
                        beanList.add(bean);
                    }

                    if (beanList.size() != 0){
                        isMore = true;
                        handler.obtainMessage(200, beanList).sendToTarget();
                    }else {
                        isMore = false;
                        handler.obtainMessage(2).sendToTarget();
                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void initView() {

        recycle_group = (RecyclerView) view.findViewById(R.id.recycle_group);
        adapter = new GroupFailAdapter(getActivity() , allList , "0");
        recycle_group.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycle_group.setAdapter(adapter);

        recycle_group.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                if (adapter.getItemCount() > 4 && lastCompletelyVisibleItemPosition == adapter.getItemCount() - 1 && isMore) {
                    page++;
                    initData(page);
                    isMore = false;
                }
            }
        });
    }
}
