package com.xinspace.csevent.app.utils;

import android.content.Context;

import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;


/**
 * Created by Android on 2016/9/21.
 */
public class DataUtils {


    /**
     * 常见问题
     *
     */
    public static void getFaqList(Context context, HttpRequestListener listener){
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.GET_TYPE_FAQ;
            http.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 抽奖规则
     *
     */
    public static void getRuleList(Context context, HttpRequestListener listener){
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.GET_TYPE_rule;
            http.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 抽奖规则
     *
     */
    public static void getUserAgree(Context context, HttpRequestListener listener){
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.GET_TYPE_user_agree;
            http.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 常见问题
     *
     */
    public static void getHotQueList(Context context, HttpRequestListener listener){
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.hotQuesList;
            http.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
