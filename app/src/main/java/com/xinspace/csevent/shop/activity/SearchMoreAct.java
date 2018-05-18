package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.ExGoodsAdapter;
import com.xinspace.csevent.shop.modle.ExGoodsBean;
import com.xinspace.csevent.util.LogUtil;
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
 * 积分兑换更多 搜索
 *
 * Created by Android on 2017/6/19.
 */

public class SearchMoreAct extends BaseActivity{

    private EditText ed_search;
    private ImageView iv_cancel;
    private ListView lv_more;
    private TextView tv_yes;
    private Intent intent;
    private String cateid;
    private String keyword;
    private int page;
    private LinearLayout ll_search_back;
    private SDPreference preference;
    private String openId;
    private List<ExGoodsBean>  allGoodList = new ArrayList<ExGoodsBean>();
    private ExGoodsAdapter adapter;
    private int integral;
    private RelativeLayout rel_search;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null) {
                        allGoodList.addAll((Collection<? extends ExGoodsBean>) msg.obj);
                        adapter.setList(allGoodList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 400:

                    ToastUtil.makeToast("暂无相关商品");

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(SearchMoreAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_serach_more);
        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");

        intent = getIntent();
        initView();

        if (intent != null){
            cateid = intent.getStringExtra("cateid");
            integral = intent.getIntExtra("integral" , 0);

            if (cateid != null){
                page = 1;
                getSearchMoreData("" , cateid , page);
            }
        }
    }

    private void initView() {

        ll_search_back = (LinearLayout) findViewById(R.id.ll_search_back);
        ll_search_back.setOnClickListener(onClickListener);

        rel_search = (RelativeLayout) findViewById(R.id.rel_search);
        ed_search = (EditText) findViewById(R.id.ed_search);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        lv_more = (ListView) findViewById(R.id.lv_more);
        tv_yes = (TextView) findViewById(R.id.tv_yes);
        tv_yes.setOnClickListener(onClickListener);

        adapter = new ExGoodsAdapter(this);
        adapter.setList(allGoodList);
        lv_more.setAdapter(adapter);
        lv_more.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(SearchMoreAct.this , ExChangeDetailAct.class);
            intent.putExtra("id" , allGoodList.get(position).getId());
            intent.putExtra("bean" , allGoodList.get(position));
            intent.putExtra("integral" , integral);
            startActivity(intent);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_search_back:
                    finish();
                    break;
                case R.id.tv_yes:
                    keyword = ed_search.getText().toString().trim();
                    if (keyword != null && !keyword.equals("")){
                        if (allGoodList.size() != 0){
                            allGoodList.clear();
                        }
                        page = 1;
                        getSearchMoreData(keyword , cateid , page);

                    }else{

                        ToastUtil.makeToast("请输入搜索关键字");

                    }
                    break;
            }
        }
    };

    private void getSearchMoreData(String keyword , String cateid , int page){

        GetDataBiz.getSearchData(keyword, cateid, String.valueOf(page), openId ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null || result.equals("")){
                    return;
                }

                LogUtil.i("搜索和更多的" + result);
                Gson gson = new Gson();
                JSONObject obj = new JSONObject(result);
                if (obj.getString("code").equals("200")){
                    JSONObject dataObject = obj.getJSONObject("data");

                    List<ExGoodsBean> goodList = new ArrayList<ExGoodsBean>();
                    JSONArray goodsArray = dataObject.getJSONArray("list");
                    for (int i = 0; i < goodsArray.length(); i++) {
                        JSONObject goodsObject = goodsArray.getJSONObject(i);
                        ExGoodsBean exGoodsBean =  gson.fromJson(goodsObject.toString(), ExGoodsBean.class);
                        goodList.add(exGoodsBean);
                    }
                    handler.obtainMessage(200 , goodList).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        onClickListener = null;
        itemClickListener = null;
        allGoodList = null;
        System.gc();
        this.setContentView(R.layout.empty_view);
        super.onDestroy();
    }
}
