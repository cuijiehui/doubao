package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

/***
 * 获取随机小奖品的业务
 */
public class SmallPrizesBiz {
    public static void getSmallPrizes (HttpRequestListener listener){
        String url=AppConfig.SMALL_PRIZES_URL;
        HttpUtil httpUtil=new HttpUtil();
        httpUtil.setOnHttpRequestFinishListener(listener);

        httpUtil.sendGet(url);
    }
}
