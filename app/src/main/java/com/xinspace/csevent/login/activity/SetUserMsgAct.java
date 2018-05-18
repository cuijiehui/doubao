package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.SupplyUserInfoBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.PhoneNumUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android on 2016/9/21.
 * <p/>
 * 修改用户昵称和联系方式
 */
public class SetUserMsgAct extends BaseActivity {

    private TextView tv_usermsg_title, tv_message;

    private ImageView iv_recharge_back, img_clear;

    private Intent intent;

    private EditText et_user_nickname;

    private String flag;

    private TextView tv_yes;

    private String txt, phone , realName;

    private String userName, userPhone;

    private SDPreference preference;

    private String openid;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case 100:
                    ToastUtil.makeToast("昵称修改成功");
                    intent.putExtra("result", txt);
                    setResult(1001, intent);
                    SetUserMsgAct.this.finish();
                    break;
                case 101:
                    ToastUtil.makeToast("手机号码修改成功");
                    intent.putExtra("result", phone);
                    setResult(1002, intent);
                    SetUserMsgAct.this.finish();
                    break;
                case 102:
                    ToastUtil.makeToast("真实姓名修改成功");
                    intent.putExtra("result", realName);
                    setResult(1003, intent);
                    SetUserMsgAct.this.finish();
                    break;
                case 200:
                    ToastUtil.makeToast("修改失败，请重试");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_setusermsg);

        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");

        intent = getIntent();
        if (intent != null) {
            flag = intent.getStringExtra("flag");
        }
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
        handler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        tv_usermsg_title = (TextView) findViewById(R.id.tv_usermsg_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        iv_recharge_back = (ImageView) findViewById(R.id.iv_recharge_back);
        img_clear = (ImageView) findViewById(R.id.img_clear);
        et_user_nickname = (EditText) findViewById(R.id.et_user_nickname);

        tv_yes = (TextView) findViewById(R.id.tv_yes);
        tv_yes.setOnClickListener(clickListener);

        iv_recharge_back.setOnClickListener(clickListener);
        img_clear.setOnClickListener(clickListener);

        userName = intent.getStringExtra("name");
        userPhone = intent.getStringExtra("phone");

        if (flag != null && flag.equals("nickName")) {
            tv_usermsg_title.setText("修改昵称");
            et_user_nickname.setHint("请输入昵称");
            tv_message.setText("4-12个字符 ，可由英文，数字 “-” “——” 组成");
        } else if (flag != null && flag.equals("phone")) {
            tv_usermsg_title.setText("修改联系方式");
            tv_message.setText("请输入正确的手机号码");
            int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
            et_user_nickname.setInputType(inputType);
        } else if (flag != null && flag.equals("realName")){
            tv_usermsg_title.setText("修改真实姓名");
            et_user_nickname.setHint("请输入真实姓名");
        }



        et_user_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    tv_yes.setBackgroundResource(R.drawable.bt_pressed_shape);
                }else{
                    tv_yes.setBackgroundResource(R.drawable.tv_spec_bg_shape2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_recharge_back:
                    SetUserMsgAct.this.finish();
                    break;
                case R.id.img_clear:
                    et_user_nickname.setText("");
                    break;
                case R.id.tv_yes:

                    if (flag != null && flag.equals("nickName")) {
                        txt = et_user_nickname.getText().toString();
                        LogUtil.i("txt" + txt);
                        if (!TextUtils.isEmpty(txt)) {
                            supplyInfo(txt, "");
                        }else{
                            ToastUtil.makeToast("请输入昵称");
                        }
                    } else if (flag != null && flag.equals("phone")) {
                        phone = et_user_nickname.getText().toString();
                        if (verify()) {
                            supplyInfo(userName, phone);
                        }
                    }else if (flag != null && flag.equals("realName")){
                        realName = et_user_nickname.getText().toString();
                        LogUtil.i("txt" + txt);
                        if (!TextUtils.isEmpty(realName)) {
                            supplyInfo("", realName);
                        }else{
                            ToastUtil.makeToast("请输入真实姓名");
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 向服务器提交信息
     *
     */
    private void supplyInfo(String name, String name2) {
        SupplyUserInfoBiz.supplyInfo3( openid , name , name2, "",new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i(flag + "修改返回" + result);
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        if (flag.equals("nickName")) {
                            handler.obtainMessage(100).sendToTarget();
                        }else if (flag.equals("realName")){
                            handler.obtainMessage(102).sendToTarget();
                        } else if (flag.equals("phone")) {
                            handler.obtainMessage(101).sendToTarget();
                        }
                    }else{
                        handler.obtainMessage(200).sendToTarget();
                    }
            }

            @Override
            public void onHttpRequestError(String error) {
                LogUtil.i(flag + "修改返回失败");
            }
        });
    }





    private boolean verify() {
        if (TextUtils.isEmpty(phone) || (!PhoneNumUtil.isPhoneNO(phone))) {
//            etPhone.setError("请输入正确的联系电话");
            ToastUtil.makeToast("请输入正确的手机号");
            return false;
        }
        return true;
    }


}
