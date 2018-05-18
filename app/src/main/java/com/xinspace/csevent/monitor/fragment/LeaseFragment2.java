package com.xinspace.csevent.monitor.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.LeaseApplyAdapter;
import com.xinspace.csevent.monitor.bean.LeaseApplyBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;


/**
 * Created by Android on 2017/7/21.
 */

public class LeaseFragment2 extends Fragment{

    private View view;
    private RecyclerView rv_all_lease;
    private SDPreference preference;
    private String uid;
    private String token;
    private String type = "1";
    private int page = 1;
    private boolean isMore;
    private List<LeaseApplyBean> allList = new ArrayList<LeaseApplyBean>();
    private LeaseApplyAdapter leaseAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends LeaseApplyBean>) msg.obj);
                        leaseAdapter.notifyDataSetChanged();
                    }
                    break;
                case 400:

                    ToastUtil.makeToast("没有更多数据");

                    break;
            }
        }
    };
    private String area;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        area = SharedPreferencesUtil1.getString(getActivity(), COMMUNITY_AREA, "");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fra_all_lease , null);
        preference = SDPreference.getInstance();
        uid  = preference.getContent("cUid");
        token = preference.getContent("cToken");
        initView();
        //initData(page);

        return view;
    }

    private void initView() {

        rv_all_lease = (RecyclerView) view.findViewById(R.id.rv_all_lease);
        leaseAdapter = new LeaseApplyAdapter(allList , getActivity());

        rv_all_lease.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_all_lease.setAdapter(leaseAdapter);

        rv_all_lease.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                if (leaseAdapter.getItemCount() > 3 && lastCompletelyVisibleItemPosition == leaseAdapter.getItemCount() - 1 && isMore) {
                    LogUtil.i("加载下一页-----------------");
                    page++;
                    initData(page , "1" , uid , token);
                    isMore = false;
                }
            }
        });


    }

    public void initData(int pageNum , String flag , String cuid , String ctoken) {


        if (flag != null && flag.equals("0")){
            if (allList != null && allList.size() != 0){
                allList.clear();
                pageNum = 1;
            }
        }

        GetDataBiz.GET_APPLY_DATA(area, cuid, ctoken, type, String.valueOf(pageNum), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {

                if (TextUtils.isEmpty(result)){
                    return;
                }

                Gson gson = new Gson();
                //LeaseData data = gson.fromJson(result , LeaseData.class);

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 200){
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    List<LeaseApplyBean> list = new ArrayList<LeaseApplyBean>();
                    for (int i = 0 ; i < dataArray.length() ; i++){
                        JSONObject jsonObject1 = dataArray.getJSONObject(i);
                        LeaseApplyBean bean = gson.fromJson(jsonObject1.toString() , LeaseApplyBean.class);
                        list.add(bean);
                    }

                    if (list.size() != 0){
                        handler.obtainMessage(200 , list).sendToTarget();
                        isMore = true;
                    }else{
                        isMore = false;
                        handler.obtainMessage(400).sendToTarget();
                    }
                }else{

                }


            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });


    }
}
