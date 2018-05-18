package com.xinspace.csevent.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.RegistBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * 重置密码页面
 */
public class ResetPasswordActivity extends Activity {
    private EditText et_password;
    private EditText et_confirm_password;
    private Button bt_confirm;
    private String code;
    private String phone;
    private TextView tv_register;
    private LinearLayout llBack;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    ToastUtil.makeToast("密码修改成功");
                    finish();
                    break;
                case 201:
                    ToastUtil.makeToast("重置密码失败");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        setView();
        setListener();
        getData();
    }
    //获取上个页面传递过来的数据
    private void getData() {
        Intent intent=getIntent();
        code=intent.getStringExtra("code");
        phone=intent.getStringExtra("phone");
    }

    //监听
    private void setListener() {
        //返回
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //跳转到注册页面
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, RegisterActivity.class));
                finish();
            }
        });
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReset();
            }
        });
    }
    //验证输入的密码是否符合格式
    private boolean verifyPassword(String password) {
        //在将数据写入数据库之前,判断用户输入的邮箱格式是否正确
        String filter="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
        Pattern p=Pattern.compile(filter);
        Matcher matcher = p.matcher(password);
        if(!matcher.find()){
            ToastUtil.makeToast("请按格式输入密码");
            return false;
        }
        return true;
    }

    //确认修改密码
    private void confirmReset() {
        String newPass=et_password.getText().toString();
        String confirmPass=et_confirm_password.getText().toString();

        if(TextUtils.isEmpty(newPass)||TextUtils.isEmpty(confirmPass)){
            ToastUtil.makeToast("请填写新密码");
        }else{
            if(newPass.equals(confirmPass)){
                //验证密码是否符合格式
                if(!verifyPassword(confirmPass)){
                    return;
                }
//                FindbackPasswordBiz.resetPassword(phone, code, newPass, new HttpRequestListener() {
//                    @Override
//                    public void onHttpRequestFinish(String result) throws JSONException {
//                        handleResetPasswordResult(result);
//                    }
//
//                    @Override
//                    public void onHttpRequestError(String error) {
//
//                    }
//                });

                RegistBiz.forgetRegistShop(phone, newPass, code, new HttpRequestListener() {
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
            }else{
                ToastUtil.makeToast("密码不一致,请重新输入");
            }
        }
    }

    //处理重置密码的返回结果
    private void handleResetPasswordResult(String result) throws JSONException {
        JSONObject obj=new JSONObject(result);
        String res = obj.getString("result");
        if(res.equals("200")){
            ToastUtil.makeToast("密码修改成功");
            finish();
        }else{
            ToastUtil.makeToast("重置密码失败");
        }
    }

    //初始化
    private void setView() {
        et_password= (EditText) findViewById(R.id.et_reset_new_password);
        et_confirm_password= (EditText) findViewById(R.id.et_reset_confirm_password);
        bt_confirm= (Button) findViewById(R.id.bt_reset_confirm);
        tv_register= (TextView) findViewById(R.id.tv_find_register);
        tv_register.setVisibility(View.GONE);

        llBack= (LinearLayout) findViewById(R.id.ll_reset_back);
    }

}
