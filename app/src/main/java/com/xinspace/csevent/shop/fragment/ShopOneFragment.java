package com.xinspace.csevent.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.data.entity.GoodsModle;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.GoodsDetailAct;
import com.xinspace.csevent.sweepstake.adapter.GoodsListAdapter2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Android on 2017/3/19.
 */

public class ShopOneFragment extends Fragment{

    private View view;
    private ListView lv_shop_goods;
    private GoodsListAdapter2 goodsAdapter;
    private List<GoodsModle> goods_List = new ArrayList<GoodsModle>();
    private boolean isMore;
    private int page = 1;
    private String cateId;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (msg.obj != null){
                        goods_List.addAll((Collection<? extends GoodsModle>) msg.obj);
                        Log.i("www" , "传过来数组长度" + goods_List.size());
                        goodsAdapter.setList(goods_List);
                        goodsAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web , null);
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        cateId = bundle.getString("cate");
        LogUtil.i("分类的Id" + cateId);

        rel_data_load = (RelativeLayout) view.findViewById(R.id.rel_data_load);

        lv_shop_goods = (ListView) view.findViewById(R.id.lv_shop_goods);
        goodsAdapter = new GoodsListAdapter2(getActivity());
        goodsAdapter.setList(goods_List);
        lv_shop_goods.setAdapter(goodsAdapter);

        getGoodsList(page , cateId);
        lv_shop_goods.setOnScrollListener(onScrollListener);

        lv_shop_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity() , GoodsDetailAct.class);
                intent.putExtra("goodId" , goods_List.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void getGoodsList(int pageNum , String cateId) {
        GetActivityListBiz.getGoodsList2(String.valueOf(pageNum) , cateId , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    Log.i("www", "微信商城" + result.toString());
                    if (result == null || result.equals("")){
                        return;
                    }
                    rel_data_load.setVisibility(View.GONE);
                    showGoodsList(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

                Log.i("www", "微信商城error   " + error.toString());
                rel_data_load.setVisibility(View.GONE);
//                if (null == activityListAdapter) {
//                    activityListAdapter = new GridViewActivityAdapter(getActivity(), act_list);
//                    listView.setAdapter(activityListAdapter);
//                }
//                listView.onRefreshComplete();
            }
        });
    }


    private void showGoodsList(String result) throws Exception {

        JSONObject obj = new JSONObject(result);
        String type = obj.getString("code");
        if (type.equals("200")) {
            JSONObject jsonObject = obj.getJSONObject("data");
            JSONArray act_ary = jsonObject.getJSONArray("list");
            List<GoodsModle> goodsEntities = new ArrayList<>();
            for (int i = 0; i < act_ary.length(); i++) {
                JSONObject j = act_ary.getJSONObject(i);
                Gson gson=new Gson();
                GoodsModle goodsModle = gson.fromJson(j.toString(), GoodsModle.class);
                goodsEntities.add(goodsModle);
            }
            LogUtil.i("商品分类列表" + goodsEntities.size());
            if (goodsEntities.size() != 0){
                handler.obtainMessage(100 , goodsEntities).sendToTarget();
                isMore = false;
            }else{
                ToastUtil.makeToast("没有更多商品");
                //isMore = false;
            }
        } else {
            ToastUtil.makeToast("数据出错");
        }
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (isMore && scrollState == SCROLL_STATE_IDLE){
                page ++;
                getGoodsList(page , cateId);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            LogUtil.i("firstVisibleItem" + firstVisibleItem);
            LogUtil.i("visibleItemCount" + visibleItemCount);
            LogUtil.i("totalItemCount" + totalItemCount);

            isMore = (firstVisibleItem + visibleItemCount == totalItemCount);
        }
    };

}
