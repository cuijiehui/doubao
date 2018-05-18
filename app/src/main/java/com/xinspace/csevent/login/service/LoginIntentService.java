package com.xinspace.csevent.login.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.xinspace.csevent.data.biz.LoginBiz;
import com.xinspace.csevent.data.entity.LoginEntity;
import com.xinspace.csevent.data.entity.ThirdLoginEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginIntentService extends IntentService implements HttpRequestListener{

    //必须是无参构造
    public LoginIntentService() {
        super("LoginIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //本地登录
        if (intent.getIntExtra("action",-1) == LoginActivity.LOGIN_ACTION_LOCALAPP) {

            String phoneNum = intent.getStringExtra("phoneNum");
            String password = intent.getStringExtra("password");
            Log.i("www" , "phoneNum" + phoneNum + "password" + password);
            //执行本地账号系统登录业务
            LoginBiz.localLogin(LoginIntentService.this,phoneNum,password);
        }

        //第三方登录
        if (intent.getIntExtra("action",-1) == LoginActivity.LOGIN_ACTION_ThirdAPP){
            String openid = intent.getStringExtra("userId");
            String PlatformNname = intent.getStringExtra("PlatformNname");
            String state = "";

            if ("Wechat".equals(PlatformNname)){
                state="2";
            }
            if ("QQ".equals(PlatformNname)){
                state="1";
            }
            if ("SinaWeibo".equals(PlatformNname)){
                state="3";
            }
            //执行第三方账号登录业务
            LoginBiz.thirdLogin(this, openid, state);
        }
    }

    //联网的回调方法,取得json字符串
    @Override
    public void onHttpRequestFinish(String result) throws JSONException {
        Log.i("www", "登录请求回调数据"+result);
        LoginEntity enty = new LoginEntity();
        JSONObject jsonObject = new JSONObject(result);
        enty.setResult(jsonObject.getString("code"));

        if (jsonObject.getInt("code") == 200){
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            enty.setUser_id(jsonObject1.getString("id"));
            enty.setTel(jsonObject1.getString("nickname"));
            enty.setUsername(jsonObject1.getString("nickname"));
            enty.setOpenid(jsonObject1.getString("openid"));
            enty.setMobile(jsonObject1.getString("mobile"));
            enty.setcToken(jsonObject1.getJSONObject("community").getString("token"));
            enty.setcUid(jsonObject1.getJSONObject("community").getString("uid"));
            enty.setCom_id(jsonObject1.getJSONObject("community").getString("com_id"));
            enty.setCommunity_id(jsonObject1.getJSONObject("community").getString("community_id"));
            sendBroadcastForLocalLogin(enty);
        }else{
            ToastUtil.makeToast("密码错误或账号不存在");
        }

//        if (result.contains("integral")) {
//            //第三方登录
//            ThirdLoginEntity enty = (ThirdLoginEntity) JsonPaser2.parserObj(result, ThirdLoginEntity.class);
//            if ("1".equals(enty.getFrist())) {//第一次登录时需要补充资料
//                Intent intent = new Intent(LoginIntentService.this, SupplyUserInfoActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("enty", enty);
//                startActivity(intent);
//            }
//            sendBroadcastForThirdLogin(enty);
//        }else{
//            //本地登录
//            //LoginEntity enty= (LoginEntity) JsonPaser2.parserObj(result, LoginEntity.class);
//            LoginEntity enty = new LoginEntity();
//            JSONObject jsonObject = new JSONObject(result);
//            enty.setResult(jsonObject.getString("code"));
//            if (jsonObject.getInt("code") == 200){
//                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                enty.setUser_id(jsonObject1.getString("id"));
//                enty.setTel(jsonObject1.getString("tel"));
//                enty.setUsername(jsonObject1.getString("username"));
//                enty.setIntegral(jsonObject1.getString("integral"));
//            }
//            sendBroadcastForLocalLogin(enty);
//        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }

    //发广播给登录的Activity(本地登录)
    private void sendBroadcastForLocalLogin(LoginEntity enty) {
        LogUtil.i("本地登录消息:" + enty.getUser_id());
        LogUtil.i("消息:" + enty.getUser_id());
        Intent intent2Log = new Intent(Const.ACTION_LOGIN);
        //intent2Log.putExtra("msg",enty.getMsg());
//        intent2Log.putExtra("result",enty.getResult());
//        intent2Log.putExtra("user_id",enty.getUser_id());
//        intent2Log.putExtra("username", enty.getUsername());
//        intent2Log.putExtra("tel" , enty.getTel());
        intent2Log.putExtra("loginEnty" , enty);
        sendBroadcast(intent2Log);
    }


    //发广播给登录的Activity(第三方登录)
    private void sendBroadcastForThirdLogin(ThirdLoginEntity enty) {
        LogUtil.i("消息:" + enty.getResult());
        LogUtil.i("消息:" + enty.getMsg());
        LogUtil.i("消息:" + enty.getUser_id());
        Intent intent2Log=new Intent(Const.ACTION_LOGIN);
        intent2Log.putExtra("msg",enty.getMsg());
        intent2Log.putExtra("result",enty.getResult());
        intent2Log.putExtra("user_id",enty.getUser_id());
        sendBroadcast(intent2Log);
    }
}
