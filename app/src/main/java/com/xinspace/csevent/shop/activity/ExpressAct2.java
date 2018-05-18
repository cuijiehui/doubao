package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.ExpressAdapter;
import com.xinspace.csevent.shop.modle.ExpressBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 积分商城物流
 *
 * Created by Android on 2017/5/20.
 */

public class ExpressAct2 extends BaseActivity{

    private LinearLayout ll_express_back;
    private SDPreference preference;
    private String openid;
    private Intent intent;
    private String orderId;

    private ImageView image;
    private TextView tv_express_state;
    private TextView tv_express_name;
    private TextView tv_express_num;

    private MyListView lv_express;

    private String thumb;
    private String num;
    private String expresscom;
    private String expresssn;
    private String msg;

    private List<ExpressBean> allList;
    private ExpressAdapter adapter;
    private String flag;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends ExpressBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                        showData();
                    }
                    break;
                case 400:

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtils.setWindowStatusBarColor(ExpressAct2.this , R.color.app_bottom_color);

        setContentView(R.layout.act_express);

        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");
        intent = getIntent();
        if (intent != null){
            orderId = intent.getStringExtra("orderId");
            flag = intent.getStringExtra("flag");
        }
        allList = new ArrayList<>();
        initView();
        if (flag != null && flag.equals("1")){
            initData();
        }else if (flag != null && flag.equals("2")){
            initData2();
        }else if (flag != null && flag.equals("3")){  //积分商城的物流
            initData3();
        }
    }

    private void initView() {
        ll_express_back = (LinearLayout) findViewById(R.id.ll_express_back);
        ll_express_back.setOnClickListener(clickListener);

        image = (ImageView) findViewById(R.id.image);
        image.setFocusable(true);

        tv_express_state = (TextView) findViewById(R.id.tv_express_state);
        tv_express_name = (TextView) findViewById(R.id.tv_express_name);
        tv_express_num = (TextView) findViewById(R.id.tv_express_num);

        lv_express = (MyListView) findViewById(R.id.lv_express);
        adapter = new ExpressAdapter(ExpressAct2.this , allList);
        lv_express.setAdapter(adapter);
    }


    private void showData() {
        ImagerLoaderUtil.displayImageWithLoadingIcon(thumb , image , R.drawable.icon_detail_load);
        tv_express_state.setText("物流状态：" + msg);
        tv_express_name.setText("物流公司：" + expresscom);
        tv_express_num.setText("运单编号：" + expresssn);
    }

    private void initData() {
        GetDataBiz.lookExpressData(orderId, openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("物流的result" + result);
                if (result == null || result.equals("")){
                    return;
                }
                List<ExpressBean> expressList = new ArrayList<ExpressBean>();
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                    thumb = dataJsonObject.getString("thumb");
                    num = dataJsonObject.getString("num");
                    expresscom = dataJsonObject.getString("expresscom");
                    expresssn = dataJsonObject.getString("expresssn");
                    msg = dataJsonObject.getString("msg");

                    JSONArray expressArray = dataJsonObject.getJSONArray("express");
                    for (int i = 0 ; i < expressArray.length() ; i++){
                        JSONObject expressObject = expressArray.getJSONObject(i);
                        ExpressBean expressBean = gson.fromJson(expressObject.toString() , ExpressBean.class);
                        expressList.add(expressBean);
                    }
                    handler.obtainMessage(200 , expressList).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    private void initData2() {
        GetDataBiz.lookExpressData2(orderId, openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("物流的result" + result);
                if (result == null || result.equals("")){
                    return;
                }
                List<ExpressBean> expressList = new ArrayList<ExpressBean>();
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                    thumb = dataJsonObject.getString("thumb");
                    expresscom = dataJsonObject.getString("expresscom");
                    expresssn = dataJsonObject.getString("expresssn");
                    msg = dataJsonObject.getString("msg");

                    JSONArray expressArray = dataJsonObject.getJSONArray("express");
                    for (int i = 0 ; i < expressArray.length() ; i++){
                        JSONObject expressObject = expressArray.getJSONObject(i);
                        ExpressBean expressBean = gson.fromJson(expressObject.toString() , ExpressBean.class);
                        expressList.add(expressBean);
                    }
                    handler.obtainMessage(200 , expressList).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    private void initData3() {
        GetDataBiz.lookExpressData3(orderId, openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("物流的result" + result);
                if (result == null || result.equals("")){
                    return;
                }
                List<ExpressBean> expressList = new ArrayList<ExpressBean>();
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                    thumb = dataJsonObject.getString("thumb");
                    expresscom = dataJsonObject.getString("expresscom");
                    expresssn = dataJsonObject.getString("expresssn");
                    msg = dataJsonObject.getString("msg");

                    JSONArray expressArray = dataJsonObject.getJSONArray("express");
                    for (int i = 0 ; i < expressArray.length() ; i++){
                        JSONObject expressObject = expressArray.getJSONObject(i);
                        ExpressBean expressBean = gson.fromJson(expressObject.toString() , ExpressBean.class);
                        expressList.add(expressBean);
                    }
                    handler.obtainMessage(200 , expressList).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_express_back:
                    ExpressAct2.this.finish();
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        clickListener = null;
        handler.removeCallbacksAndMessages(null);
        allList.clear();
        allList = null;
        System.gc();
        this.setContentView(R.layout.empty_view);
        super.onDestroy();
    }
}
