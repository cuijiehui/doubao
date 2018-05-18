package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 联网获取获奖详细记录
 */
public class AwardRecordDetailBiz {

    public static void getRecordDetail(String id, String type, HttpRequestListener listener){
        String url=AppConfig.PRIZES_DETAIL_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("id", id));
            list.add(new Params("type", type));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("获取中奖记录详情异常:"+e.getMessage());
        }
    }

    public static void getRecordDetail2(String id, String aid , String type, HttpRequestListener listener){
        String url=AppConfig.PRIZES_DETAIL_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("id", aid));
            list.add(new Params("type", type));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("获取中奖记录详情异常:"+e.getMessage());
        }
    }

    /**
     * 确认派送
     * @param id
     * @param type
     * @param listener
     */
    public static void sureDelivery(String id,String type,HttpRequestListener listener){
        String url=AppConfig.SURE_DELIVERY_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("id", id));
            list.add(new Params("type", type));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("奖品确认派送异常:"+e.getMessage());
        }
    }

    public static void sureDelivery2(String id,String type,HttpRequestListener listener){
        String url=AppConfig.SURE_DELIVERY_URL;
        String time = TimeHelper.getDateString(String.valueOf(System.currentTimeMillis()));
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("id", id));
            list.add(new Params("type", type));
            list.add(new Params("receipt_time", time));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("奖品确认派送异常:"+e.getMessage());
        }
    }

}
