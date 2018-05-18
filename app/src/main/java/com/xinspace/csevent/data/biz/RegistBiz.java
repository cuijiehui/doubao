package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.ui.activity.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhihong on 2015/12/8.
 * 注册页面的业务
 */
public class RegistBiz {

    //验证验证码
    public static void verifyCode(String phone, String code, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url=AppConfig.VERIFY_CODE_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("tel",phone));
            list.add(new Params("code",code));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void verifyCode2(String mobile, String verifycode, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url=AppConfig.VERIFY_CODE_URL2;
            List<Params> list=new ArrayList<>();
            list.add(new Params("mobile",mobile));
            list.add(new Params("verifycode",verifycode));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //向服务器发送请求取得验证码
    public static void sendVerificationCode(Context context,String phoneNum){

        final String codeUrl= AppConfig.REGISTER_CODE_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((RegisterActivity)context);

            List<Params> list=new ArrayList<>();
            list.add(new Params("tel", phoneNum));
            list.add(new Params("bool", "0"));
            httpUtil.sendPost(codeUrl,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendVerificationCode2(Context context,String phoneNum){

        final String codeUrl= AppConfig.REGISTER_CODE_URL3;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((RegisterActivity)context);

            List<Params> list=new ArrayList<>();
            list.add(new Params("mobile", phoneNum));
            list.add(new Params("temp", "sms_reg"));
            list.add(new Params("imgcode", "0"));
            httpUtil.sendPost(codeUrl,list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //向服务器发送请求注册账号
    public static void registAccount(String phone,String phoneCode,String pwd,String name,String gender,HttpRequestListener listener){

        String registerUrl= AppConfig.REGISTER_URL;
        String imei = SDPreference.getInstance().getContent("szImei");

        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);

            List<Params> list=new ArrayList<>();
            list.add(new Params("tel", phone));
            list.add(new Params("password", pwd));
            list.add(new Params("code", phoneCode));
            list.add(new Params("nickname", phone));
            list.add(new Params("sex", gender));
            list.add(new Params("imei", imei));
            httpUtil.sendPost(registerUrl,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registShop(String phone , String pwd , String phoneCode , HttpRequestListener listener){

        String registerUrl= AppConfig.REGISTER_URL3;
        //String imei = SDPreference.getInstance().getContent("szImei");
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("mobile", phone));
            list.add(new Params("verifycode", phoneCode));
            list.add(new Params("pwd", pwd));
            httpUtil.sendPost(registerUrl , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void forgetRegistShop(String phone , String pwd , String phoneCode , HttpRequestListener listener){

        String registerUrl= AppConfig.FORGET_REGISTER_URL;
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("mobile", phone));
            list.add(new Params("verifycode", phoneCode));
            list.add(new Params("pwd", pwd));
            httpUtil.sendPost(registerUrl , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
