package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.RegistBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.PhoneNumUtil;
import com.xinspace.csevent.util.TimeCountUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.xinspace.csevent.R.id.bt_register_register;
import static com.xinspace.csevent.R.id.bt_register_requestCode;
import static com.xinspace.csevent.R.id.et_register_code;
import static com.xinspace.csevent.R.id.et_register_phone;

/**
 * 注册页面 获取验证码
 */
public class RegisterActivity extends BaseActivity implements HttpRequestListener {
    private EditText et_phone, et_code;
    private Button bt_register, bt_requestCode;
    private LinearLayout ll_back;
    private String valuePhoneNum;
    private String valueCodeNum;
    private String res = null;
    private ImageView imgClearCode;
    private CheckBox cbUserDeal;
    private boolean isCheck = true;
    private TextView textview2;

    private EditText et_register_pwd;
    private EditText et_register_sure_pwd;
    private ImageView iv_register_eye;

    boolean seePwd=false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    RegisterActivity.this.finish();
                    break;
            }
        }
    };


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setViews();
        setListeners();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 设置监听
     */
    private void setListeners() {
        /**点击返回*/
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击获取验证码
        bt_requestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyPhone()) {//先验证手机号码格式是否正确
                    //按钮倒计时
                    TimeCountUtil timeCountUtil =
                            new TimeCountUtil(RegisterActivity.this, 30000, 1000, bt_requestCode);
                    timeCountUtil.start();
                    String phoneNum = et_phone.getText().toString();
                    // 获取验证码
                    getVerificationCode(phoneNum);
                }
            }
        });

        if (isCheck){
            //点击跳转到输入密码页码
            bt_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();
                }
            });
        }else{
            ToastUtil.makeToast("请同意拾得用户协议");
        }
    }

    /**
     * 注册
     *
     */
    private void register(){
        String phone = et_phone.getText().toString();
        String code = et_code.getText().toString();
        String pwd = et_register_pwd.getText().toString();
        String surePwd = et_register_sure_pwd.getText().toString();

        if (TextUtils.isEmpty(phone)){
            ToastUtil.makeToast("请填写手机号码");
            return;
        }

        if (TextUtils.isEmpty(code)){
            ToastUtil.makeToast("请填写验证码");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            ToastUtil.makeToast("请填写密码");
            return;
        }

        if (TextUtils.isEmpty(surePwd)){
            ToastUtil.makeToast("请填写确认密码");
            return;
        }

        if (!pwd.equals(surePwd)){
            ToastUtil.makeToast("两次密码填写不一致");
            return;
        }

       RegistBiz.registShop(phone, pwd, code, new HttpRequestListener() {
           @Override
           public void onHttpRequestFinish(String result) throws JSONException {
               JSONObject jsonObject = new JSONObject(result);
               if (jsonObject.getInt("code") == 200){
                   ToastUtil.makeToast("注册成功");
                   handler.obtainMessage(200).sendToTarget();
               }else{
                   ToastUtil.makeToast("注册失败");
               }
           }

           @Override
           public void onHttpRequestError(String error) {

           }
       });
    }

    //点击眼睛查看密码
    private void seeThePwd() {
        if(seePwd == false) {
            //可见
            et_register_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            iv_register_eye.setImageDrawable(getResources().getDrawable(R.drawable.eye_green));
            seePwd = true;
        }else{
            et_register_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            iv_register_eye.setImageDrawable(getResources().getDrawable(R.drawable.eye_grey));
            seePwd = false;
        }
        //切换后将EditText光标置于末尾
        CharSequence charSequence = et_register_pwd.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    //验证验证码
    private void verifyCode() {
        RegistBiz.verifyCode(et_phone.getText().toString(), et_code.getText().toString(), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject obj = new JSONObject(result);
                String res_code = obj.getString("result");
                if (res_code.equals("200")) {
                    //验证码正确，进入下个页面
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, RegisterDetailActivity.class);
                    String phone = et_phone.getText().toString();
                    String phoneCode = et_code.getText().toString();
                    intent.putExtra("phone", phone);
                    intent.putExtra("phoneCode", phoneCode);
                    startActivity(intent);
                    finish();
                } else {
                    //错误
                    ToastUtil.makeToast("验证码错误");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    /**
     * 初始化控件
     */
    private void setViews() {

        et_code = (EditText) findViewById(et_register_code);
        et_phone = (EditText) findViewById(et_register_phone);

        bt_register = (Button) findViewById(bt_register_register);
        bt_requestCode = (Button) findViewById(bt_register_requestCode);

        ll_back = (LinearLayout) findViewById(R.id.ll_register_back);

        imgClearCode = (ImageView) findViewById(R.id.img_clear_code);
        imgClearCode.setOnClickListener(clickListener);

        cbUserDeal = (CheckBox) findViewById(R.id.cb_user_deal);
        cbUserDeal.setChecked(true);
        cbUserDeal.setOnCheckedChangeListener(checkedChangeListener);

        textview2 = (TextView) findViewById(R.id.textview2);
//        textview2.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
//        textview2.getPaint().setAntiAlias(true);//抗锯齿

        et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
        et_register_sure_pwd = (EditText) findViewById(R.id.et_register_sure_pwd);
        iv_register_eye = (ImageView) findViewById(R.id.iv_register_eye);

        //点击眼睛查看密码
        iv_register_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeThePwd();
            }
        });
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isCheck = isChecked;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_clear_code:
                    et_code.setText("");
                    break;
            }
        }
    };

    /**
     * 向手机发送验证码
     *
     * @param phoneNum 需要发送验证码的手机号  15626180667
     */
    private void getVerificationCode(final String phoneNum) {
        RegistBiz.sendVerificationCode2(this, phoneNum);
    }

    /**
     * 联网请求回调接口,返回发送验证码后的josn字符串
     */
    @Override
    public void onHttpRequestFinish(String result) {
//        MesCodeEntity enty = (MesCodeEntity) JsonPaser2.parserObj(result, MesCodeEntity.class);
//        res = enty.getResult();
        try {
            JSONObject jsonObject = new JSONObject(result);
            ToastUtil.makeToast(jsonObject.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }

    /**
     * 验证手机号码格式是否正确
     */
    private boolean verifyPhone() {
        valuePhoneNum = et_phone.getText().toString();

        if (TextUtils.isEmpty(valuePhoneNum) || (!PhoneNumUtil.isMobileNO(valuePhoneNum))) {
            ToastUtil.makeToast("请输入正确的手机号码");
            return false;
        }
        return true;
    }
}
