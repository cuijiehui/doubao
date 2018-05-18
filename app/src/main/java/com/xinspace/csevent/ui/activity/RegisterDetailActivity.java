package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.RegistBiz;
import com.xinspace.csevent.data.entity.RegEntity;
import com.xinspace.csevent.login.weiget.PasswordMethod;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   设置密码 确定密码界面
 * */
public class RegisterDetailActivity extends BaseActivity{
    Button bt_register;
    private String phone;
    private String gender="1";
    private String phoneCode;
    private ImageButton ibBack;
    private EditText etName ;
    private EditText etPwd;
    private ImageButton ibMan;
    private ImageButton ibWoman;
    private TextView tvAccount;
    private ImageView ivEye;
    boolean seePwd=false;
    private EditText etSurePwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);

        getData();

        setViews();
        setListeners();
    }
    //获取上个页面的数据
    private void getData() {
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        phoneCode=intent.getStringExtra("phoneCode");
    }

    //设置监听器
    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击注册
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount(phone, phoneCode,gender);
            }
        });

        //点击性别女
        ibWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibWoman.setBackgroundResource(R.drawable.bt_register_gender_woman_shape_pressed);
                ibMan.setBackgroundResource(R.drawable.bt_register_gender_man_shape_unpressed);
                gender = "0";
            }
        });
        //点击性别男
        ibMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibWoman.setBackgroundResource(R.drawable.bt_register_gender_woman_shape_unpressed);
                ibMan.setBackgroundResource(R.drawable.bt_register_gender_man_shape_pressed);
                gender = "1";
            }
        });
        //点击眼睛查看密码
        ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeThePwd();
            }
        });
    }

    //点击眼睛查看密码
    private void seeThePwd() {
        if(seePwd==false) {
            //可见
            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivEye.setImageDrawable(getResources().getDrawable(R.drawable.eye_green));
            seePwd=true;
        }else{
            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivEye.setImageDrawable(getResources().getDrawable(R.drawable.eye_grey));
            seePwd=false;
        }
        //切换后将EditText光标置于末尾
        CharSequence charSequence = etPwd.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    //初始化
    private void setViews() {
        tvAccount = (TextView) findViewById(R.id.tv_register_account1);
        etName = (EditText) findViewById(R.id.et_register_name);
        etPwd = (EditText) findViewById(R.id.et_register_pwd);
        tvAccount.setText(phone);

        ibMan = (ImageButton) findViewById(R.id.ib_register_man);
        ibWoman = (ImageButton) findViewById(R.id.ib_register_woman);

        bt_register = (Button) findViewById(R.id.bt_register_register);
        ibBack = (ImageButton) findViewById(R.id.iv_register_back);
        ivEye=(ImageView)findViewById(R.id.iv_register_eye);

        etSurePwd = (EditText) findViewById(R.id.et_register_sure_pwd);
        etSurePwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etSurePwd.setTransformationMethod(new PasswordMethod());
        etPwd.setTransformationMethod(new PasswordMethod());

    }

    //注册账号
    private void registerAccount(String phone,String phoneCode,String gender){

        String password = etPwd.getText().toString().trim();
        String surePassword = etSurePwd.getText().toString().trim();

        LogUtil.i("www   "  + "password" + password + "surePassword"  + surePassword);


        if(TextUtils.isEmpty(password)){
            ToastUtil.makeToast("请填写密码");
            return;
        }

        if(TextUtils.isEmpty(surePassword)){
            ToastUtil.makeToast("请填写确认密码");
            return;
        }

        //对输入的密码做一个正则判断(6-20位数字或者字母)
        boolean is_right = verifyPassword(password);
        if(!is_right){
            return;
        }

        boolean isSureRight = verifyPassword(surePassword);
        if(!isSureRight){
            return;
        }

        RegistBiz.registAccount(phone, phoneCode, password, phone, gender, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www" , "注册返回参数" + result);
                handleRegister(result);
            }

            @Override
            public void onHttpRequestError(String error) {

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

    //处理注册返回结果
    private void handleRegister(String result) {
        RegEntity enty = (RegEntity) JsonPaser2.parserObj(result, RegEntity.class);
        LogUtil.i("注册信息:" + enty.toString());
        String message =enty.getMsg();
        if ("202".equals(message)){
            ToastUtil.makeToast("用户已注册");
        }if ("203".equals(message)){
            ToastUtil.makeToast("验证码无效");
        }else {
           // ToastUtil.makeToast("注册成功!您的id为:" + enty.getUser_id() + ",积分为:" + enty.getIntegral());
            Intent intent = new Intent(RegisterDetailActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

            //注册完成后进入已登录状态
            CoresunApp.USER_ID = enty.getUser_id();
        }
    }
}
