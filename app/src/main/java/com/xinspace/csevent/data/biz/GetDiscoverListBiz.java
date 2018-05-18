package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

/**
 * 获取推荐的软件列表
 */
public class GetDiscoverListBiz {
    public static void getdiscover(HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.FOUND_DISCOVER_URL;
            http.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
