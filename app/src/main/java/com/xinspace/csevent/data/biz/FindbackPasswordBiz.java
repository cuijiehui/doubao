package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 找回密码业务
 */
public class FindbackPasswordBiz {

    /**
     * 重新设置新的密码
     * @param tel 手机号
     * @param code 验证码
     * @param newPass 新密码
     * @param listener 回调
     */
    public static void resetPassword(String tel,String code,String newPass,HttpRequestListener listener){
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.RESET_CODE_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("tel", tel));
            list.add(new Params("code", code));
            list.add(new Params("password", newPass));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求发送验证码
     * @param tel 手机号码
     * @param listener 回调
     */
    public static void sendCode(String tel, HttpRequestListener listener){
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);

            String codeUrl= AppConfig.REGISTER_CODE_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("tel", tel));
            list.add(new Params("bool", "1"));
            httpUtil.sendPost(codeUrl,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 找回密码
     *
     * @param tel
     * @param listener
     */
    public static void sendCode2(String tel, HttpRequestListener listener){
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);

            String codeUrl= AppConfig.REGISTER_CODE_URL3;
            List<Params> list=new ArrayList<>();
            list.add(new Params("mobile", tel));
            list.add(new Params("temp", "sms_forget"));
            list.add(new Params("imgcode", "0"));
            httpUtil.sendPost(codeUrl,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendCode3(String tel, HttpRequestListener listener){
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);

            String codeUrl= AppConfig.REGISTER_CODE_URL4;
            List<Params> list=new ArrayList<>();
            list.add(new Params("mobile", tel));
            list.add(new Params("temp", "sms_reset"));
            list.add(new Params("imgcode", "0"));
            httpUtil.sendPost(codeUrl,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
