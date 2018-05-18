package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取用户个人数据
 */
public class GetProfileBiz {
    public static void getProfile(Context context, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.PROFILE_URL2;
            List<Params> list=new ArrayList<>();
            list.add(new Params("userid", CoresunApp.USER_ID));
            http.sendPost4(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void getProfile2(String openid, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.USER_INTEGRAL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("openid", openid));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getProfile3(String openid, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.USER_MESSAGE;
            List<Params> list=new ArrayList<>();
            list.add(new Params("openid", openid));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
