package com.xinspace.csevent.shop.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.AllConvertAdapter;
import com.xinspace.csevent.shop.modle.ConvertRecordBean;
import com.xinspace.csevent.shop.weiget.ResultBean;
import com.xinspace.csevent.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 积分兑换已完成
 * <p>
 * <p>
 * Created by Android on 2017/6/19.
 */

public class ConvertFinishFragment extends Fragment {

    private View view;
    private RecyclerView rv_all_convert;
    private boolean isMore = false;
    private SDPreference sdPreference;
    private String openId;
    private int start = 1;
    private List<ConvertRecordBean> allRecordList;
    private AllConvertAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj != null) {
                        allRecordList.addAll((Collection<? extends ConvertRecordBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    //ToastUtil.makeToast("没有更多数据");
                    break;

                case 100:  //刷新列表数据
//                    if(allRecordList.size() != 0){
//                        allRecordList.clear();
//                    }
//                    start = 1;
//                    initData(start);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_convert, null);

        sdPreference = SDPreference.getInstance();
        openId = sdPreference.getContent("openid");
        allRecordList = new ArrayList<>();

        initView();
        //initData(start);
        return view;
    }


    public void initData(int start , String flag) {

        if (flag != null && flag.equals("0")){
            if (allRecordList != null && allRecordList.size() != 0){
                allRecordList.clear();
                start = 1;
            }
        }

        GetDataBiz.getConvertRecord(openId, start, "4", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "积分兑换记录" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                List<ConvertRecordBean> recordList = new ArrayList<ConvertRecordBean>();
                JSONObject jsonObject = new JSONObject(result);
                JSONObject dataJsonObject = jsonObject.getJSONObject("message");
                JSONArray jsonArray = dataJsonObject.getJSONArray("list"); //订单数组

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject beanObject = jsonArray.getJSONObject(i);
                    ConvertRecordBean bean = gson.fromJson(beanObject.toString(), ConvertRecordBean.class);
                    recordList.add(bean);
                }

                LogUtil.i("全部订单的数目解析" + recordList.size());
                if (recordList.size() != 0) {
                    isMore = true;
                    handler.obtainMessage(1, recordList).sendToTarget();
                } else {
                    isMore = false;
                    handler.obtainMessage(2).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i("请求失败了");
            }
        });
    }


    private void initView() {

        rv_all_convert = (RecyclerView) view.findViewById(R.id.rv_all_convert);

        adapter = new AllConvertAdapter(getActivity() , allRecordList , resultBean);
        rv_all_convert.setAdapter(adapter);

        rv_all_convert.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_all_convert.setAdapter(adapter);

        rv_all_convert.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    start++;
                    initData(start , "1");
                    isMore = false;
                }
            }
        });
    }



    ResultBean resultBean = new ResultBean() {

        @Override
        public void resultBean(int position) {

//            LogUtil.i("position" + position);
//
//            allRecordList.remove(position);
//            allOrderAdapter.notifyItemRemoved(position);
//            allOrderAdapter.notifyItemRangeChanged(0, allRecordList.size() - 1);
        }
    };

}
