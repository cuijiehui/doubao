package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 签到业务
 */
public class QianDaoBiz {
    /**
     * 签到
     * @param listener 回调
     */
    public static void qiandao(HttpRequestListener listener){
        try{
            String url=AppConfig.QIAN_DAO_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 判断用户是否已经签过到
     * @param listener 回调
     */
    public static void isQianDao(HttpRequestListener listener){
        try {
            String url=AppConfig.IS_QIAN_DAO_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
