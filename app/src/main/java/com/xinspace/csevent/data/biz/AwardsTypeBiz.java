package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取奖品分类页面业务
 */
public class AwardsTypeBiz {
    /**
     * @param province 省
     * @param city 市
     * @param district 区
     */
    public static void getClassList(Context context, String province, String city, String district, HttpRequestListener listener){
        try {
            String url= AppConfig.PRODUCT_CLASS_LIST;
            List<Params> list=new ArrayList<>();
//            list.add(new Params("province",province));
//            list.add(new Params("city",city));
//            list.add(new Params("area",district));

            list.add(new Params("start ","0"));
            list.add(new Params("length ","100"));

            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param context
     * @param start
     * @param id
     * @param listener
     */
    public static void getTypeContent(Context context, String start,String id
                                      ,HttpRequestListener listener){
        try {
            String url= AppConfig.GET_TYPE_CONTENT_URL;
            List<Params> list=new ArrayList<>();
//            list.add(new Params("province",province));
//            list.add(new Params("city",city));
//            list.add(new Params("area",district));
            list.add(new Params("aclass",id));
            list.add(new Params("start ",start));
            list.add(new Params("length ","30"));

            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 通过关键字活动相应的活动
     * @param context 上下文对象
     * @param keyname 关键字
     * @param province 省
     * @param city 市
     * @param district 区
     * @param listener 回调
     */
    public static void getTypeContentByKeyname(Context context, String keyname,String province, String city, String district,HttpRequestListener listener){
        try {
            String url= AppConfig.GET_TYPE_CONTENT_URL;
            List<Params> list=new ArrayList<>();
//            list.add(new Params("province",province));
//            list.add(new Params("city",city));
//            list.add(new Params("area",district));
            list.add(new Params("search",keyname));
            list.add(new Params("start ","0"));
            list.add(new Params("length ","100"));

            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
