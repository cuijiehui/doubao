package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

/**
 * 启动应用时全屏广告的接口
 */
public class FullPageAdsBiz {
    public static void getAds(Context context, HttpRequestListener listener){
        try{
            String url=AppConfig.GET_FULLPAGE_ADS_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            http.sendGet(url);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("全屏广告接口数据异常:"+e.getMessage());
        }
    }
}
