package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.GroupCateAdapter;
import com.xinspace.csevent.shop.modle.GroupGoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 拼团分类和搜索
 * <p>
 * Created by Android on 2017/6/21.
 */

public class GroupCateAct extends BaseActivity {

    private RecyclerView rv_group_cate;
    private LinearLayout ll_group_back;

    private int page = 1;
    private boolean isMore = false;

    private String cateId;
    private Intent intent;
    private List<GroupGoodsBean> allGoodsList = new ArrayList<GroupGoodsBean>();
    private GroupCateAdapter adapter;
    private EditText ed_search;
    private TextView tv_yes;
    private String keyword;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    rel_data_load.setVisibility(View.GONE);
                    if (msg.obj != null){
                        allGoodsList.addAll((Collection<? extends GroupGoodsBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    rel_data_load.setVisibility(View.GONE);
                    ToastUtil.makeToast("没有更多数据");
                   // adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(GroupCateAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_group_cate);
        intent = getIntent();
        if (intent != null) {
            cateId = intent.getStringExtra("cateId");
        }
        initView();
        initData(page);
    }

    private void initView() {

        ll_group_back = (LinearLayout) findViewById(R.id.ll_group_back);
        ll_group_back.setOnClickListener(clickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);
        ed_search = (EditText) findViewById(R.id.ed_search);

        tv_yes = (TextView) findViewById(R.id.tv_yes);
        tv_yes.setOnClickListener(clickListener);

        rv_group_cate = (RecyclerView) findViewById(R.id.rv_group_cate);
        rv_group_cate.setLayoutManager(new LinearLayoutManager(GroupCateAct.this, LinearLayoutManager.VERTICAL, false));
        adapter = new GroupCateAdapter(GroupCateAct.this , allGoodsList);
        rv_group_cate.setAdapter(adapter);

        rv_group_cate.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void initData(int page) {

        rel_data_load.setVisibility(View.VISIBLE);

        GetDataBiz.getGroupCateData(String.valueOf(page), cateId, keyword, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "拼团分类" + result);
                if (result == null || result.equals("")){
                    return;
                }
                Gson gson = new Gson();
                JSONObject obj = new JSONObject(result);
                JSONObject dataJsonObject = obj.getJSONObject("data");
                List<GroupGoodsBean> goodsList = new ArrayList<GroupGoodsBean>();
                JSONArray goodsJsonArray = dataJsonObject.getJSONArray("list");
                for (int i = 0; i < goodsJsonArray.length(); i++) {
                    JSONObject goodsJSONObject = goodsJsonArray.getJSONObject(i);
                    GroupGoodsBean bean = gson.fromJson(goodsJSONObject.toString(), GroupGoodsBean.class);
                    goodsList.add(bean);
                }

                if (goodsList.size() != 0){
                    isMore = true;
                    handler.obtainMessage(200 , goodsList).sendToTarget();
                }else {
                    isMore = false;
                    handler.obtainMessage(2).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                handler.obtainMessage(2).sendToTarget();
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_group_back:
                    GroupCateAct.this.finish();
                    break;
                case R.id.tv_yes:
                    keyword = ed_search.getText().toString().trim();

                    if (keyword != null && !keyword.equals("")){
                        if (allGoodsList.size() != 0){
                            allGoodsList.clear();
                        }
                        page = 1;
                        initData(page);
                    }else{
                        ToastUtil.makeToast("请输入搜索关键字");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        clickListener = null;
        allGoodsList = null;
        intent = null;
        ImagerLoaderUtil.clearImageMemory();
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }

}
