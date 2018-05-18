package com.xinspace.csevent.login.activity;
/***
 * 会员中心反馈页面
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.FeedbackBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.PhoneNumUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedbackActivity extends BaseActivity {

    private EditText etTxt,etPhone;
    private Button btCommit;
    private LinearLayout ll_back;
    private String phone;
    private String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.activity_feedback);
        setViews();
        setListeners();
    }

    private void setListeners() {
        //点击返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击提交反馈
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitFeedback();
            }
        });
    }
    //提交反馈信息
    private void commitFeedback() {
        txt=etTxt.getText().toString();
        phone=etPhone.getText().toString();
        if (verify()) {
            FeedbackBiz.commit(phone, txt, new HttpRequestListener() {
                @Override
                public void onHttpRequestFinish(String result) throws JSONException {
                    handleFeedbackReuslt(result);
                }

                @Override
                public void onHttpRequestError(String error) {
                    ToastUtil.makeToast("意见反馈提交成功");
                    finish();
                }
            });
        }
    }

    //初始化
    private void setViews() {
        etTxt=(EditText)findViewById(R.id.et_feedback_txt);
        etPhone=(EditText)findViewById(R.id.et_feedback_phone);
        btCommit=(Button)findViewById(R.id.bt_feedback_commit);
        ll_back= (LinearLayout) findViewById(R.id.ll_feedback_back);
    }
    //处理反馈信息的返回的结果
    private void handleFeedbackReuslt(String result) {
        try {
            JSONObject json = new JSONObject(result);
            switch (json.getString("result")){
                case "200" :
                    ToastUtil.makeToast("反馈信息提交成功");
                    finish();
                    break;
                case "202" :
                    ToastUtil.makeToast("提交出错");
                    break;
                case "201" :
                    ToastUtil.makeToast("添加失败");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**验证号码输入格式是否正确*/
    private boolean verify () {
        if(TextUtils.isEmpty(txt)){
            ToastUtil.makeToast("请输入反馈信息");
            return false;
        }
        if (TextUtils.isEmpty(phone)||(!PhoneNumUtil.isPhoneNO(phone))) {
//            etPhone.setError("请输入正确的联系电话");
            ToastUtil.makeToast("请输入正确的手机号");
            return false;
        }
        return true;
    }
}
