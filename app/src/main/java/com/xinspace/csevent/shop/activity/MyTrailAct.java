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
import com.xinspace.csevent.monitor.view.MyGridView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.MoreAdapter;
import com.xinspace.csevent.shop.modle.RecommendBean;
import com.xinspace.csevent.shop.modle.TrialBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的试用界面
 *
 * Created by Android on 2017/6/16.
 */

public class MyTrailAct extends BaseActivity{

    private LinearLayout ll_my_trial_back;
    private ImageView iv_goods_image;
    private TextView tv_apply_num;
    private TextView tv_goods_name;
    private TextView tv_goods_num;
    private TextView tv_goods_price;
    private TextView tv_apply_time;
    private MyGridView gv_more_trial;
    private TextView tv_state;

    private Intent intent;
    private String id;
    private SDPreference preference;
    private String openId;
    private TrialBean allBean;
    private MoreAdapter adapter;
    private List<RecommendBean> beanList = new ArrayList<>();

    private String success;
    private String status;
    private String orderId;
    private String url;

    private TextView tv_go_detail;
    private TextView tv_look_more;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    if (msg.obj != null) {
                        allBean = (TrialBean) msg.obj;

                        ImagerLoaderUtil.displayImageWithLoadingIcon(allBean.getThumb() , iv_goods_image , R.drawable.goods_loading);
                        tv_goods_name.setText(allBean.getTitle());
                        tv_apply_num.setText(allBean.getApply() + "人申请");

                        tv_goods_num.setText(allBean.getTotal());
                        tv_goods_price.setText("¥" + allBean.getPrice());
                        tv_apply_time.setText(allBean.getEndtime() + "结束");

                        success = allBean.getSuccess();
                        status = allBean.getStatus();
                        orderId = allBean.getId();
                        url = allBean.getUrl();

                        LogUtil.i("success" + success + "status" + status);

                        if (success != null && !success.equals("")){
                            if (success.equals("0")){
                                tv_state.setText("您已申请,请耐心等待审核");
                            }else if (success.equals("1")){
                                if (status.equals("1")){
                                    tv_state.setText("恭喜您,申请成功!待发货");
                                }else if (status.equals("2")){
                                    tv_state.setText("恭喜您,申请成功!待收货,查看物流");
                                }else if (status.equals("3")){
                                    tv_state.setText("恭喜您,申请成功!请填写收货地址");
                                }else if (status.equals("4")){
                                    tv_state.setText("恭喜您,申请成功!已完成");
                                }
                            }else if (success.equals("-1")){
                                tv_state.setText("请遗憾，您未获得试用资格,尝试申请其他试用");
                            }
                        }else{
                            //holder.tv_apply_state.setText("免费试用");
                        }
                        beanList = allBean.getList();
                        adapter.setList(beanList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(MyTrailAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_my_trial);

        intent = getIntent();
        if (intent != null){
            id = intent.getStringExtra("id");
        }
        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");

        initView();
        initData();
    }

    private void initView() {

        ll_my_trial_back = (LinearLayout) findViewById(R.id.ll_my_trial_back);
        ll_my_trial_back.setOnClickListener(onClickListener);

        tv_go_detail = (TextView) findViewById(R.id.tv_go_detail);
        tv_go_detail.setOnClickListener(onClickListener);

        tv_look_more = (TextView) findViewById(R.id.tv_look_more);
        tv_look_more.setOnClickListener(onClickListener);

        iv_goods_image = (ImageView) findViewById(R.id.iv_goods_image);
        tv_apply_num = (TextView) findViewById(R.id.tv_apply_num);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_num = (TextView) findViewById(R.id.tv_goods_num);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_state.setOnClickListener(onClickListener);

        gv_more_trial = (MyGridView) findViewById(R.id.gv_more_trial);
        adapter = new MoreAdapter(this);
        adapter.setList(beanList);
        gv_more_trial.setAdapter(adapter);
    }

    private void initData() {
        GetDataBiz.myApplyData(openId, id, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("我的试用" + result);
                if (result == null || result.equals("")){
                    return;
                }
                Gson gson = new Gson();
                JSONObject obj = new JSONObject(result);
                JSONObject dataObject = obj.getJSONObject("data");
                List<RecommendBean> beanList = new ArrayList<RecommendBean>();

                TrialBean trialBean = gson.fromJson(dataObject.toString(), TrialBean.class);
                JSONArray listJsonarry = dataObject.getJSONArray("recommend");
                for (int i = 0; i < listJsonarry.length(); i++) {
                    JSONObject beanObject = listJsonarry.getJSONObject(i);
                    RecommendBean recommendBean = gson.fromJson(beanObject.toString(), RecommendBean.class);
                    beanList.add(recommendBean);
                }
                trialBean.setList(beanList);
                handler.obtainMessage(200 , trialBean).sendToTarget();
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_my_trial_back:  //返回

                    MyTrailAct.this.finish();

                    break;
                case R.id.tv_state:
                    if (success.equals("1")){
                        if (status.equals("1")){
                        }else if (status.equals("2")){
                            Intent intent = new Intent(MyTrailAct.this , ExpressAct.class);
                            intent.putExtra("orderId" , orderId);
                            intent.putExtra("flag" , "2");
                            startActivity(intent);
                        }else if (status.equals("3")){

                        }else if (status.equals("4")){

                        }
                    }
                    break;
                case R.id.tv_go_detail:
                    Intent intent = new Intent(MyTrailAct.this , TrialDetailAct.class);
                    intent.putExtra("id" , id);
                    intent.putExtra("success" , success);
                    intent.putExtra("url" , url);
                    startActivity(intent);
                    break;
                case R.id.tv_look_more:

                    Intent intent2 = new Intent(MyTrailAct.this , FreeTrialAct.class);
                    intent2.putExtra("from" , "MyTrailAct");
                    startActivity(intent2);
                    finish();

                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        onClickListener = null;
        handler.removeCallbacksAndMessages(null);
        ImagerLoaderUtil.clearImageMemory();
        beanList = null;
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
