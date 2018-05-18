package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.DataUtils;
import com.xinspace.csevent.login.model.HotQuestionBean;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.AddressManagerActivity;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/11/4.
 *
 * 客服中心
 */
public class CallCenterAct extends BaseActivity{

    private List<HotQuestionBean> dataList;

    private LinearLayout ll_back;
    private TextView tv_question_1;
    private TextView tv_question_2;
    private TextView tv_question_3;
    private TextView tv_question_4;
    private TextView tv_question_5;
    private TextView tv_question_6;

    private RelativeLayout rel_question_1;
    private RelativeLayout rel_question_2;
    private RelativeLayout rel_question_3;
    private RelativeLayout rel_question_4;
    private RelativeLayout rel_question_5;
    private RelativeLayout rel_question_6;


    private LinearLayout lin_rule;
    private LinearLayout lin_address;
    private LinearLayout lin_feedback;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        List<HotQuestionBean> list = (List<HotQuestionBean>) msg.obj;
                        tv_question_1.setText("1. " + list.get(0).getQuestions());
                        tv_question_2.setText("2. " + list.get(1).getQuestions());
                        tv_question_3.setText("3. " + list.get(2).getQuestions());
                        tv_question_4.setText("4. " + list.get(3).getQuestions());
                        tv_question_5.setText("5. " + list.get(4).getQuestions());
                        tv_question_6.setText("6. " + "更多问题");
                    }
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_call_center);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
        dataList = null;
    }

    private void initView() {

        dataList = new ArrayList<HotQuestionBean>();

        initData();

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        tv_question_1 = (TextView) findViewById(R.id.tv_question_1);
        tv_question_2 = (TextView) findViewById(R.id.tv_question_2);
        tv_question_3 = (TextView) findViewById(R.id.tv_question_3);
        tv_question_4 = (TextView) findViewById(R.id.tv_question_4);
        tv_question_5 = (TextView) findViewById(R.id.tv_question_5);
        tv_question_6 = (TextView) findViewById(R.id.tv_question_6);

        rel_question_1 = (RelativeLayout) findViewById(R.id.rel_question_1);
        rel_question_2 = (RelativeLayout) findViewById(R.id.rel_question_2);
        rel_question_3 = (RelativeLayout) findViewById(R.id.rel_question_3);
        rel_question_4 = (RelativeLayout) findViewById(R.id.rel_question_4);
        rel_question_5 = (RelativeLayout) findViewById(R.id.rel_question_5);
        rel_question_6 = (RelativeLayout) findViewById(R.id.rel_question_6);

        rel_question_1.setOnClickListener(clickListener);
        rel_question_2.setOnClickListener(clickListener);
        rel_question_3.setOnClickListener(clickListener);
        rel_question_4.setOnClickListener(clickListener);
        rel_question_5.setOnClickListener(clickListener);
        rel_question_6.setOnClickListener(clickListener);

        lin_rule = (LinearLayout) findViewById(R.id.lin_rule);
        lin_address = (LinearLayout) findViewById(R.id.lin_address);
        lin_feedback = (LinearLayout) findViewById(R.id.lin_feedback);

        lin_rule.setOnClickListener(clickListener);
        lin_address.setOnClickListener(clickListener);
        lin_feedback.setOnClickListener(clickListener);


    }

    private void initData() {
        DataUtils.getHotQueList(this, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                LogUtil.i("热点问题" + result);
                if (jsonObject.getString("result").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Gson gson=new Gson();
                        HotQuestionBean  hotQuestionBean = gson.fromJson(jsonObject1.toString(), HotQuestionBean.class);
                        dataList.add(hotQuestionBean);
                    }
                    handler.obtainMessage(1 , dataList).sendToTarget();
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
                case R.id.ll_back:

                    CallCenterAct.this.finish();

                    break;
                case R.id.rel_question_1:
                    Intent intent1 = new Intent(CallCenterAct.this , HotQuesAct.class );
                    intent1.putExtra("data" , dataList.get(0));
                    startActivity(intent1);
                    break;
                case R.id.rel_question_2:
                    Intent intent2 = new Intent(CallCenterAct.this , HotQuesAct.class );
                    intent2.putExtra("data" , dataList.get(1));
                    startActivity(intent2);
                    break;
                case R.id.rel_question_3:
                    Intent intent3 = new Intent(CallCenterAct.this , HotQuesAct.class );
                    intent3.putExtra("data" , dataList.get(2));
                    startActivity(intent3);
                    break;
                case R.id.rel_question_4:
                    Intent intent4 = new Intent(CallCenterAct.this , HotQuesAct.class );
                    intent4.putExtra("data" , dataList.get(3));
                    startActivity(intent4);
                    break;
                case R.id.rel_question_5:
                    Intent intent5 = new Intent(CallCenterAct.this , HotQuesAct.class );
                    intent5.putExtra("data" , dataList.get(4));
                    startActivity(intent5);
                    break;
                case R.id.rel_question_6:
                    Intent intent6 = new Intent(CallCenterAct.this , MoreQuesAct.class );
                    intent6.putExtra("data" , (Serializable) dataList);
                    startActivity(intent6);
                    break;
                case R.id.lin_rule:
                    Intent intentRule = new Intent(CallCenterAct.this , SweepRulesAct.class );
                    startActivity(intentRule);
                    break;
                case R.id.lin_address:
                    if (CoresunApp.USER_ID == null) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(CallCenterAct.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(CallCenterAct.this, AddressManagerActivity.class);
                        intent.putExtra("flag" , "home");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_feedback:
                    Intent intent = new Intent(CallCenterAct.this, FeedbackActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

}
