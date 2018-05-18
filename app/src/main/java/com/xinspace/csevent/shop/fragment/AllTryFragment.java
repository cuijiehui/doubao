package com.xinspace.csevent.shop.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.TrialListAdapter;
import com.xinspace.csevent.shop.adapter.TrialTypeAdapter;
import com.xinspace.csevent.shop.modle.TrialBean;
import com.xinspace.csevent.shop.modle.TrialTypeBean;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sdk_sample.sdk.views.HorizontalListView;

/**
 * 所有试用
 * <p>
 * Created by Android on 2017/6/14.
 */

public class AllTryFragment extends Fragment {

    private View view;
    private HorizontalListView hv_all_try_type;
    private TrialTypeAdapter trialTypeAdapter;

    private ListView lv_all_try;
    private TrialListAdapter trialAdapter;
    private List<TrialTypeBean> allTypeList = new ArrayList<>();
    private int page = 1;
    private String cate;

    private List<TrialBean> allBeanList = new ArrayList<TrialBean>();
    private boolean isLast;
    private boolean isMore;

    private SDPreference preference;
    private String openId;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    if (msg.obj != null) {
                        allTypeList.addAll((Collection<? extends TrialTypeBean>) msg.obj);
                        trialTypeAdapter.setList(allTypeList);
                        trialTypeAdapter.notifyDataSetChanged();

                        cate = allTypeList.get(0).getId();
                        getTrailData(page, cate);
                    }
                    break;
                case 2000:
                    if (msg.obj != null) {
                        allBeanList.addAll((Collection<? extends TrialBean>) msg.obj);
                        trialAdapter.setList(allBeanList);
                        trialAdapter.notifyDataSetChanged();
                    }
                    break;
                case 400:
                    ToastUtil.makeToast("没有更多商品");
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_try, null);

        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");

        initView();
        initData();

        return view;
    }

    private void initView() {
        hv_all_try_type = (HorizontalListView) view.findViewById(R.id.hv_all_try_type);
        hv_all_try_type.setFocusable(false);
        trialTypeAdapter = new TrialTypeAdapter(getActivity());
        trialTypeAdapter.setList(allTypeList);
        hv_all_try_type.setAdapter(trialTypeAdapter);
        hv_all_try_type.setOnItemClickListener(onItemClickListener);

        lv_all_try = (ListView) view.findViewById(R.id.lv_all_try);
        lv_all_try.setOnScrollListener(onScrollListener);
        trialAdapter = new TrialListAdapter(getActivity());
        trialAdapter.setList(allBeanList);
        lv_all_try.setAdapter(trialAdapter);
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (isLast && scrollState == SCROLL_STATE_IDLE){
                page++;
                getTrailData(page, cate);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(totalItemCount == visibleItemCount + firstVisibleItem && isMore){
                isLast = true;
            }else{
                isLast = false;
            }
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            trialTypeAdapter.setSelectItem(position);
            trialTypeAdapter.notifyDataSetChanged();
            cate = allTypeList.get(position).getId();

            page = 1;
            allBeanList.clear();
            getTrailData(page, cate);
        }
    };

    private void getTrailData(int pageNum, String cate) {
        GetDataBiz.getTrialList(pageNum, cate, openId ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("免费试用分类列表" + result);
                if (result == null || result.equals("")){
                    return;
                }
                List<TrialBean> beanList = new ArrayList<TrialBean>();
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                JSONArray listJsonarry = dataJsonObject.getJSONArray("list");
                for (int i = 0; i < listJsonarry.length(); i++) {
                    JSONObject beanObject = listJsonarry.getJSONObject(i);
                    TrialBean trialBean = gson.fromJson(beanObject.toString(), TrialBean.class);
                    beanList.add(trialBean);
                }

                if (beanList.size() == 0){
                    isMore = false;
                    handler.obtainMessage(400, beanList).sendToTarget();
                }else{
                    isMore = true;
                    handler.obtainMessage(2000, beanList).sendToTarget();
                }

            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    /**
     * 请求数据
     */
    private void initData() {

        GetDataBiz.getTrialType(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("免费试用分类" + result);
                if (result == null || result.equals("")){
                    return;
                }

                List<TrialTypeBean> typeList = new ArrayList<TrialTypeBean>();
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataJsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    JSONObject jsonObject1 = dataJsonArray.getJSONObject(i);
                    TrialTypeBean typeBean = gson.fromJson(jsonObject1.toString(), TrialTypeBean.class);
                    typeList.add(typeBean);
                }
                handler.obtainMessage(200, typeList).sendToTarget();
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

}
