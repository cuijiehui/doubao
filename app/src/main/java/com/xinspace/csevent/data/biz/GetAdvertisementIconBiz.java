package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城页面,获取品牌商家icon
 */
public class GetAdvertisementIconBiz {
    /***
     * 获取商品图标
     * @param context 上下文对象
     * @param listener 回调
     */
    public static void getIcon(Context context, HttpRequestListener listener){
        HttpUtil http=new HttpUtil();
        http.setOnHttpRequestFinishListener(listener);

        String url= AppConfig.GET_BRAND_ICON_URL;
        http.sendGet(url);
    }

    /***
     * 获取商家的广告轮播图
     * @param context 上下文对象
     * @param id 商家id
     * @param listener 回调
     */
    public static void getScrollBrandPic(Context context,String id,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.GET_BRAND_PICTURE_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("id",id));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
