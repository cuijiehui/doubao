package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取首页广告的业务
 */
public class GetAdvertisementBiz {
    public static void getAdvs(Context context, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.ADVERTISEMENT_URL;
            http.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAdvs2(Context context, HttpRequestListener listener){
        try {
//            HttpUtil http= new HttpUtil();
//            http.setOnHttpRequestFinishListener(listener);
//            String url= "http://shop.coresun.net/User/Index/Advertisement_Img";
//            http.sendGet(url);

            List<Params> list = new ArrayList<Params>();
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= "http://community.coresun.net/index.php/api/Ad/getList";
            list.add(new Params("uid" , "222"));
            list.add(new Params("token" , "2222"));
            list.add(new Params("terminal" , "2"));
            list.add(new Params("display" , "1"));
            http.sendPost2(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAdvs3(Context context, HttpRequestListener listener){
        try {
            List<Params> list = new ArrayList<Params>();
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_INCALL_VD_DATA;
            list.add(new Params("uid" , "222"));
            list.add(new Params("token" , "2222"));
            list.add(new Params("terminal" , "2"));
            list.add(new Params("display" , "2"));
            http.sendPost2(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getShopFristData(HttpRequestListener listener){
        HttpUtil http= new HttpUtil();
        http.setOnHttpRequestFinishListener(listener);
        //String url= "http://shop.coresun.net/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=diypage.api&id=20";
        String url= "http://shop.coresun.net/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=diypage.api&id=22";
        http.sendGet(url);
    }


}
