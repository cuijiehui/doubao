package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.QAndAActivity;

/**
 * 会员中心的Q&A页面接口
 */
public class QAndABiz {
    public static void getQA(Context context){
        try{
            String url=AppConfig.GET_Q_AND_A_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener((QAndAActivity)context);
            http.sendGet(url);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("Q&A页面接口数据异常:"+e.getMessage());
        }
    }
}
