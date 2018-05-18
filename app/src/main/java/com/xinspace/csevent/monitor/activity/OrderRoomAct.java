package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.monitor.bean.LeaseRoomBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * Created by Android on 2017/7/20.
 */

public class OrderRoomAct extends BaseActivity{

    private Intent intent;
    private LinearLayout ll_order_back;
    private LinearLayout lin_book_tel;
    private LeaseRoomBean bean;

    private RelativeLayout rel_order_time;
    private EditText ed_user_nickname;
    private EditText ed_order_tel;
    private String time ;
    private SDPreference preference;
    private String uid;
    private String token;
    private TextView tv_order_time;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:

                    ToastUtil.makeToast("提交申请成功");
                    finish();

                    break;
                case 400:

                    ToastUtil.makeToast("提交申请失败");

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(OrderRoomAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_order_room);
        intent = getIntent();
        preference = SDPreference.getInstance();
        uid  = preference.getContent("cUid");
        token = preference.getContent("cToken");
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");

        if (intent != null){
            bean = (LeaseRoomBean) intent.getSerializableExtra("bean");
        }
        initView();
        initData();
    }

    private void initView() {

        ll_order_back = (LinearLayout) findViewById(R.id.ll_order_back);
        lin_book_tel = (LinearLayout) findViewById(R.id.lin_book_tel);

        ll_order_back.setOnClickListener(onClickListener);
        lin_book_tel.setOnClickListener(onClickListener);

        rel_order_time = (RelativeLayout) findViewById(R.id.rel_order_time);
        rel_order_time.setOnClickListener(onClickListener);

        ed_user_nickname = (EditText) findViewById(R.id.ed_user_nickname);
        ed_order_tel = (EditText) findViewById(R.id.ed_user_nickname);

        tv_order_time = (TextView) findViewById(R.id.tv_order_time);

    }

    private void initData() {

    }

    private String area;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_order_back:

                    finish();

                    break;
                case R.id.lin_book_tel:  // 提交

                    String name = ed_user_nickname.getText().toString().trim();
                    String tel = ed_order_tel.getText().toString().trim();

                    if (TextUtils.isEmpty(name)){
                        ToastUtil.makeToast("请输入姓名");
                    }

                    if (TextUtils.isEmpty(tel)){
                        ToastUtil.makeToast("请输入手机号");
                    }

                    if (TextUtils.isEmpty(time)){
                        ToastUtil.makeToast("请选择时间");
                    }

                    GetDataBiz.SUBMIT_APPLY_DATA(area, uid, token, bean.getId(), tel, name, time, new HttpRequestListener() {
                        @Override
                        public void onHttpRequestFinish(String result) throws JSONException {
                            if (TextUtils.isEmpty(result)){
                                return;
                            }

                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 200){
                                handler.obtainMessage(200).sendToTarget();
                            }else{
                                handler.obtainMessage(400).sendToTarget();
                            }

                        }

                        @Override
                        public void onHttpRequestError(String error) {

                        }
                    });

                    break;
                case R.id.rel_order_time:

                    Intent intent = new Intent(OrderRoomAct.this , OrderDateAct.class);
                    startActivityForResult(intent , 1000);

                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == 1000){
            if (data != null){
                time = data.getStringExtra("clickDay") + " " + data.getStringExtra("time");
                tv_order_time.setText(time);
            }
        }
    }


}
