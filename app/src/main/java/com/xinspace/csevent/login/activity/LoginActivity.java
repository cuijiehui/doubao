package com.xinspace.csevent.login.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.monitor.activity.SubmitDataAct;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.biz.GetProfileBiz;
import com.xinspace.csevent.data.biz.LoginBiz;
import com.xinspace.csevent.data.entity.LoginEntity;
import com.xinspace.csevent.data.entity.ProfileDataEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.login.service.LoginIntentService;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.ui.activity.FindMyPasswordActivity;
import com.xinspace.csevent.ui.activity.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.TokenResult;
import sdk_sample.sdk.utils.ToolsUtil;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

public class LoginActivity extends BaseActivity implements PlatformActionListener{
    EditText etPhoneNum,etPassword;
    Button btLogin;
    ImageButton ibQQ,ibWeiBo,ibWeiXin;
    private LoginActivity instance;
    private LoginBoardcardReceiver  receiver;
    private RelativeLayout ll_back;
    private TextView tv_register;

    //登录动作(本地登录&第三方登录)
    public static final int LOGIN_ACTION_LOCALAPP = 0;
    public static final int LOGIN_ACTION_ThirdAPP = 1;
    private String userId;
    private TextView findPassword;

    private List<Object> enty_list;
    private ProfileDataEntity enty;

    private String flag;
    private Intent intent;
    private TextView tv_visitor_login; // 游客登录

    private ImageView iv_login_top;
    private int screenWidth;
    private SDPreference preference;
    private String mobile;

    private String token;
    private long dealTime;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:
                    doGetNeiborId("1");
                    break;
                case 1001:
                    String status = (String) msg.obj;
                    if (status.equals("0")){
                        ToastUtil.makeToast("请完善社区资料");
                        Intent intent = new Intent(LoginActivity.this , SubmitDataAct.class);
                        startActivity(intent);
                    }else if (status.equals("1")){
                        //ToastUtil.makeToast("审核中");
                    }else if (status.equals("2")){
//                        Intent intent = new Intent(LoginActivity.this , OpenDoorActivity.class);
//                        startActivity(intent);
                    }
                    finish();
                    break;
//                case 1002:
//                    finish();
//                    break;
            }
        }
    };
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);

        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        //初始化shareSDK
        ShareSDK.initSDK(this);
        preference = SDPreference.getInstance();

        setContentView(R.layout.activity_login);

        intent = getIntent();
        if (intent != null){
            flag = intent.getStringExtra("flag");
        }

        instance=this;
        setViews();
        setListeners();

        //注册广播
        receiver=new LoginBoardcardReceiver();
        registerReceiver(receiver,new IntentFilter(Const.ACTION_LOGIN));
    }
    /*设置监听*/
    private void setListeners() {
        //找回密码
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,FindMyPasswordActivity.class));
            }
        });
        //跳转到注册页面
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        //返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //登录
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        //微信登录
        ibWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                //authorize(wechat);
                wechat.SSOSetting(false);  //设置false表示使用SSO授权方式
                wechat.setPlatformActionListener(LoginActivity.this); // 设置分享事件回调
                wechat.authorize();//单独授权
                wechat.showUser(null);//授权并获取用户信息
            }
        });
        //QQ登录
        ibQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Platform qq = ShareSDK.getPlatform(QQ.NAME);
//                authorize(qq);

                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.SSOSetting(false);  //设置false表示使用SSO授权方式
                qq.setPlatformActionListener(LoginActivity.this); // 设置分享事件回调
                qq.authorize();//单独授权
                qq.showUser(null);//授权并获取用户信息
            }
        });

        //微博登录
        ibWeiBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                authorize(sinaWeibo);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
                weibo.setPlatformActionListener(LoginActivity.this); // 设置分享事件回调
                weibo.authorize();//单独授权
                weibo.showUser(null);//授权并获取用户信息
            }
        });

        //游客登录
        tv_visitor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.makeToast("游客登录");
            }
        });
    }

    //登录
    private void Login() {
        try {
            String phoneNum = etPhoneNum.getText().toString();
            String password = etPassword.getText().toString();

            if(TextUtils.isEmpty(phoneNum)){
                ToastUtil.makeToast("用户名不能为空");
                return;
            }
            //验证手机号码
            if(!verifyPhone(phoneNum)){
                ToastUtil.makeToast("请输入正确的手机号");
                return;
            }
            if(TextUtils.isEmpty(password)){
                ToastUtil.makeToast("请输入密码");
                return;
            }
            LogUtil.i("点击了登录按钮");

            Intent intent = new Intent(LoginActivity.this, LoginIntentService.class);
            intent.putExtra("phoneNum", phoneNum);
            intent.putExtra("password", password);
            intent.putExtra("action",LOGIN_ACTION_LOCALAPP);
            startService(intent);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("登录出现异常");
        }
    }

    //验证手机号码
    private boolean verifyPhone(String phone) {
        //在将数据写入数据库之前,判断用户输入的邮箱格式是否正确
        String filter="^0?1[3|4|5|8|7][0-9]\\d{8}$";
        Pattern p=Pattern.compile(filter);
        Matcher matcher = p.matcher(phone);
        if(!matcher.find()){
            return false;
        }
        return true;
    }

    //初始化组件
    private void setViews() {
        iv_login_top = (ImageView) findViewById(R.id.iv_login_top);
        screenWidth = ScreenUtils.getScreenWidth(LoginActivity.this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_login_top.getLayoutParams();
        params.width = (int) (screenWidth * 1.0f);
        params.height = (int) (screenWidth * 0.78f);
        iv_login_top.setLayoutParams(params);
        iv_login_top.setScaleType(ImageView.ScaleType.FIT_XY);
        iv_login_top.setImageResource(R.drawable.icon_login_top);

        findPassword= (TextView) findViewById(R.id.tv_login_forget_password);
        findPassword.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        findPassword.getPaint().setAntiAlias(true);//抗锯齿

        etPhoneNum = (EditText) findViewById(R.id.et_login_phone);
        etPassword = (EditText) findViewById(R.id.et_login_password);

        btLogin = (Button) findViewById(R.id.bt_login_login);
        ibQQ = (ImageButton) findViewById(R.id.ib_login_qq);
        ibWeiBo = (ImageButton) findViewById(R.id.ib_login_weibo);
        ibWeiXin = (ImageButton) findViewById(R.id.ib_login_wellChat);
        tv_register= (TextView) findViewById(R.id.tv_login_register);

        ll_back = (RelativeLayout) findViewById(R.id.ll_login_back);

        tv_visitor_login = (TextView) findViewById(R.id.tv_visitor_login);
    }


    /** 登录广播接收类 **/
    class LoginBoardcardReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            if ("400".equals(result)){
                //String msg=intent.getStringExtra("msg");
                ToastUtil.makeToast("密码错误或用户不存在");
            }else{
                //String msg = intent.getStringExtra("msg");

                LoginEntity enty = (LoginEntity) intent.getSerializableExtra("loginEnty");

                String user_id = enty.getUser_id();
                String tel = enty.getUsername();
                String nickName = enty.getUsername();
                String openId = enty.getOpenid();
                mobile = enty.getMobile();

                String cUid = enty.getcUid();
                String cToken = enty.getcToken();

                LogUtil.i("登录成功uid="+user_id);
                LogUtil.i("登录成功" + "tel" + tel + "nickName" + nickName);

                preference.putContent("userId" , user_id);
                preference.putContent("tel" , tel);
                preference.putContent("nickName" , nickName);
                preference.putContent("openid" , openId);
                preference.putContent("mobile" , mobile);

                preference.putContent("cUid" , cUid);
                preference.putContent("cToken" , cToken);
                preference.putContent("com_id" , enty.getCom_id());

                //回调我的页面中OnActivityResult方法
                instance.setResult(RESULT_OK);
                //把第一次登陆获取存储在本地的小奖品记录发给服务器
               //sendSmallPrizeRecord();
                 //getProfile();
                //doGetToken(mobile);
                CoresunApp.isLogin = true;
                queryAuditStatus();

            }
        }

        public void getProfile() {
            if (CoresunApp.USER_ID == null) {
                return;
            }
            GetProfileBiz.getProfile(LoginActivity.this, new HttpRequestListener() {
                @Override
                public void onHttpRequestFinish(String result) throws JSONException {
                    showProfile(result);
                }

                @Override
                public void onHttpRequestError(String error) {

                }
            });
        }

        private void showProfile(String result) {
            Log.i("www", "登录查询信息" + result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                Log.i("www", jsonObject.getString("message"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            enty_list = JsonPaser2.parserAry(result, ProfileDataEntity.class, "data");
//            enty = (ProfileDataEntity) enty_list.get(0);
//            String tel = enty.getTel();
//            String nickName = enty.getNickname();
//            CoresunApp.USER_TEL = tel;
//            CoresunApp.username = nickName;
//
//            if (flag != null && flag.equals("video")){
//                Intent intent = new Intent(LoginActivity.this , ViedoAct.class);
//                startActivity(intent);
//            }
//            finish();
        }

        /**把第一次登陆获取存储在本地的小奖品记录发给服务器*/
        private void sendSmallPrizeRecord() {
            boolean isSend = SharedPreferencesUtil1.getBoolean(LoginActivity.this, "isSendSmallPrize", false);
            if (!isSend) {//判断小奖品记录是否发送过给服务器
                String prizeId = SharedPreferencesUtil1.getString(LoginActivity.this, "smallPrizeId", "");

                LoginBiz.sendPrizeRecord(prizeId, new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        LogUtil.i("登记小奖品结果:"+result);
                    }

                    @Override
                    public void onHttpRequestError(String error) {

                    }
                });
                //发过一次记录后不再发给服务器
                SharedPreferencesUtil1.saveBoolean(LoginActivity.this,"isSendSmallPrize",true);
            }
        }
        /**将用户信息在本地存储起来*/
        private void saveUserInfoInLocal(String user_id) {

        }
    }
    /**第三方授权*/
//    private void authorize(Platform plat) {
//        if (plat == null) {
//            return;
//        }
//        //判断指定平台是否已经完成授权
//        if(plat.isAuthValid()) {
//            plat.removeAccount();
//        }
//        plat.setPlatformActionListener(this);
//        // true不使用SSO授权，false使用SSO授权
//        plat.SSOSetting(false);
//        //获取用户资料,可能进入授权页
//        plat.showUser(null);
//    }


    /**授权页面回调接口*/
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        //授权完成后登录
        userId = platform.getDb().getUserId();
        LogUtil.i("第三方id"+userId);
        String PlatformNname = platform.getDb().getPlatformNname();
        String userIcon = platform.getDb().getUserIcon();
        String username = platform.getDb().getUserName();

        //第一次授权时,将个人数据保存到全局变量
        CoresunApp.userIcon = userIcon;
        CoresunApp.username = username;
        LogUtil.i("用户头像地址:"+userIcon);

        //将第三方用户的信息保存到本地
        SharedPreferencesUtil1.saveString(this,"username",username);
        SharedPreferencesUtil1.saveString(this,"userIcon",userIcon);

        LogUtil.i("平台db的用户信息" + userId+PlatformNname);
        thirdLogin(userId,PlatformNname);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtil.makeToast("授权出错,请重试");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        LogUtil.i("取消授权");
    }


    /**第三方登录的方法*/
    private void thirdLogin(String userId,String PlatformNname){
        Intent intent=new Intent(LoginActivity.this, LoginIntentService.class);
        intent.putExtra("userId", userId);
        intent.putExtra("PlatformNname", PlatformNname);
        intent.putExtra("action",LOGIN_ACTION_ThirdAPP);
        startService(intent);
    }

    private void doGetToken(String mobile) {
        String key = "2";
        String userName = mobile;
        String app_id = "2";
        if(TextUtils.isEmpty(key)){
            ToolsUtil.toast(this, "key不能为空！");
            return;
        }
        if(TextUtils.isEmpty(userName)){
            ToolsUtil.toast(this, "userName不能为空！");
            return;
        }
        if(TextUtils.isEmpty(app_id)){
            ToolsUtil.toast(this, "appId不能为空！");
            return;
        }

        ToolsUtil.getToken(this,key,userName,app_id,null,new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                TokenResult tokeResult = (TokenResult)result;
                //ToolsUtil.toast(getApplicationContext(), "成功: "+tokeResult.getToken());
                token = tokeResult.getToken();
                dealTime = tokeResult.getDead_time();
                handler.obtainMessage(1000).sendToTarget();

                Log.i("www" , "dealtime" + dealTime);
                Log.i("www" , "token" + token);
            }

            @Override
            public void onFail(int code, String msg) {
                ToolsUtil.toast(getApplicationContext(), "您无使用权限");
            }
        });
    }

    /**
     * 获取的社区
     */
    private void doGetNeiborId(String flag){

        Log.i("www" , "------------社区列表请求成功---------------");

        final String userName = mobile;
        if(TextUtils.isEmpty(token)){
            ToolsUtil.toast(this, "token不能为空！");
            return;
        }
        if(TextUtils.isEmpty(userName)){
            ToolsUtil.toast(this, "userName不能为空！");
            return;
        }

        String neibor_flag = "100032";
        if(TextUtils.isEmpty(neibor_flag)){
            ToolsUtil.toast(this, "neibor_flag不能为空");
            return;
        }

        final Map<String, String> headpParams = new HashMap<String, String>();
        headpParams.put("token",token);
        headpParams.put("username", userName);
        headpParams.put("dealtime", dealTime + "");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("neibor_flag", neibor_flag);

        Log.i("www" , "username" + userName);
        Log.i("www" , "neibor_flag" + neibor_flag);

        ToolsUtil.goToOpenDoor2(LoginActivity.this , headpParams , params, flag ,new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                //请求成功回调
                Log.i("社区登录成功"  , "code   " + code + "result   " + result.toString());
                handler.obtainMessage(1002).sendToTarget();
                //preference.putContent("GetNeiborId" , "success");
            }

            @Override
            public void onFail(int code, String msg) {

                //请求失败回调
                Log.i("www"  , "失败code   " + code + "msg   " + msg.toString());

                preference.putContent("GetNeiborId" , "fail");
                handler.obtainMessage(1001).sendToTarget();

                ToolsUtil.toast(getApplicationContext(), "请完善社区资料");


                //ToolsUtil.toast(getApplicationContext(), msg);
            }
        });
    }



    private void queryAuditStatus(){

        String cuid = preference.getContent("cUid");
        String cToken = preference.getContent("cToken");

        GetDataBiz.getqueryAuditStatus(area, cuid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null || result.equals("")){
                    return;
                }

                LogUtil.i("获取审核状态列表" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String status = dataJsonObject.getString("status");
                    handler.obtainMessage(1001 , status).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        unregisterReceiver(receiver);
        intent = null;
        handler.removeCallbacksAndMessages(null);
        System.gc();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
