package com.xinspace.csevent.data.biz;

import android.util.Log;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取oss授权码
 */
public class GetOssKeyBiz {
    public static void getKey(HttpRequestListener listener){
        try {
            Log.i("www" , "授权oss的uid" + CoresunApp.USER_ID);
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.OSS_KEY_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid" , CoresunApp.USER_ID));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
