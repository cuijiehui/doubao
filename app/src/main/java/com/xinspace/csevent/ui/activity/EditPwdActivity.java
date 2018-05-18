package com.xinspace.csevent.ui.activity;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.FindbackPasswordBiz;
import com.xinspace.csevent.data.biz.RegistBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.PhoneNumUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 *  重置登录密码
 *
 */
public class EditPwdActivity extends Activity {
    private LinearLayout back;
    private EditText phoneNum;
    private EditText codeNum;
    private Button bt_send;
    private Button bt_commit;
    private String tel;
    private int timeCount=60;//60秒后重新发送信息
    private Timer timer;
    private TimerTask timerTask;

    private EditText et_register_pwd;
    private EditText et_register_sure_pwd;
    private ImageView img_clear_code;
    private ImageView iv_register_eye;

    boolean seePwd=false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200 :
                    ToastUtil.makeToast("请重新登录");
                    EditPwdActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_password2);
        setView();
        setListener();
    }
    //设置监听
    private void setListener() {
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发送验证码
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        //下一步
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               nextStep();
                //register();

            }
        });

        img_clear_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeNum.setText("");
            }
        });


        iv_register_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeThePwd();
            }
        });
    }


    //下一步
    private void nextStep() {
        String code = codeNum.getText().toString();
        if(TextUtils.isEmpty(code)){
            ToastUtil.makeToast("请输入验证码");
        }else{
            verifyCodeNum(code);
        }
    }



    //验证验证码的正确性
    private void verifyCodeNum(final String code) {
        RegistBiz.verifyCode2(tel, code, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject obj=new JSONObject(result);
                String res = obj.getString("code");
                if(res.equals("200")){
                    //验证码正确
                    Intent intent=new Intent(EditPwdActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("code" , code);
                    intent.putExtra("phone" , tel);
                    startActivity(intent);
                    finish();
                }else{
                    ToastUtil.makeToast("验证码错误");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }



    //倒计时
    private void countDown(){
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(timeCount<0){
                            timer.cancel();
                            timer=null;
                            timerTask.cancel();
                            timerTask=null;
                            bt_send.setClickable(true);
                            bt_send.setBackgroundResource(R.drawable.selector_bt_state_change);
                            bt_send.setText("获取验证码");
                            timeCount=60;
                            return;
                        }
                        bt_send.setText("重新获取"+timeCount);
                        timeCount--;
                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);
    }
    //发送验证码
    private void sendCode() {
        tel=phoneNum.getText().toString();
        //验证手机号码
        if(PhoneNumUtil.isPhoneNO(tel)){
            FindbackPasswordBiz.sendCode3(tel, new HttpRequestListener() {
                @Override
                public void onHttpRequestFinish(String result) throws JSONException {
                    LogUtil.i("找回密码的的返回值验证码" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")){
                        ToastUtil.makeToast("验证码发送成功");
                    }else{

                    }
                }

                @Override
                public void onHttpRequestError(String error) {

                }
            });
            bt_send.setBackgroundResource(R.drawable.shape_resend_code);
            bt_send.setClickable(false);
            //倒计时
            countDown();
        }else{
            ToastUtil.makeToast("请输入正确的手机号码");
        }
    }
    //初始化
    private void setView() {
        back= (LinearLayout) findViewById(R.id.ll_find_back);
        codeNum= (EditText) findViewById(R.id.et_register_code);
        phoneNum= (EditText) findViewById(R.id.et_find_phone_num);
        bt_send= (Button) findViewById(R.id.bt_find_send_code);
        bt_commit= (Button) findViewById(R.id.bt_find_next);

        et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
        et_register_sure_pwd = (EditText) findViewById(R.id.et_register_sure_pwd);

        img_clear_code = (ImageView) findViewById(R.id.img_clear_code);
        iv_register_eye = (ImageView) findViewById(R.id.iv_register_eye);
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


    private void register(){

        String phone = phoneNum.getText().toString();
        String code = codeNum.getText().toString();
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

        RegistBiz.forgetRegistShop(phone, pwd, code, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 200){
                    ToastUtil.makeToast("密码重置成功");
                    handler.obtainMessage(200).sendToTarget();
                }else{
                    ToastUtil.makeToast("密码重置失败");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
}
