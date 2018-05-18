package com.xinspace.csevent.monitor.fragment;

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
import com.xinspace.csevent.monitor.adapter.RepairsAdapter;
import com.xinspace.csevent.monitor.bean.RepairsBean;
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
 * Created by Android on 2017/3/17.
 */

public class RepairsRecordFragment extends Fragment{

    private View view;
    private RecyclerView recycle_repairs;
    private String cUid;
    private String token;
    private SDPreference preference;
    private int page;
    private List<RepairsBean> allList;
    private RepairsAdapter adapter;
    private boolean isMore;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        LogUtil.i("----------------------------------");
                        allList.addAll((Collection<? extends RepairsBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 400:
                    ToastUtil.makeToast("数据请求失败");
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
        view = inflater.inflate(R.layout.fragment_repairs_record , null);
        preference = SDPreference.getInstance();

        initView();
        initData(page);
        
        return view;
    }

    private void initView() {
        allList = new ArrayList<>();
        cUid = preference.getContent("cUid");
        token = preference.getContent("cToken");
        page = 1;
        recycle_repairs = (RecyclerView) view.findViewById(R.id.recycle_repairs);
        adapter = new RepairsAdapter(getActivity() , allList);
        recycle_repairs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycle_repairs.setAdapter(adapter);

        recycle_repairs.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    LogUtil.i("加载下一页-----------------");
                    page++;
                    initData(page);
                    isMore = false;
                }
            }
        });
    }

    private void initData(int page) {

        GetDataBiz.repairsListData(area, cUid, token, String.valueOf(page), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("报修记录的" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                List<RepairsBean> repairsList = new ArrayList<RepairsBean>();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject repairsObject = jsonArray.getJSONObject(i);
                        RepairsBean bean = gson.fromJson(repairsObject.toString() , RepairsBean.class);
                        repairsList.add(bean);
                    }

                    if (repairsList.size() != 0){
                        isMore = true;
                    }else{
                        isMore = false;
                    }
                    handler.obtainMessage(200 , repairsList).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                handler.obtainMessage(400).sendToTarget();
            }
        });
    }

}
