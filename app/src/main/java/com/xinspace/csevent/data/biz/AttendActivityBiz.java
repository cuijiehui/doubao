package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 参加抽奖业务
 */
public class AttendActivityBiz {
    /**
     * @param context 上下文对象
     */
    public static void attendByGeneral(Context context, String aid, String uid, int num,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            //这里的1,2,3,4分别代表抽奖的四个档次
//            if(num.equals("1")){
//                num="1";
//            }else if(num.equals("3")){
//                num="2";
//            }else if(num.equals("6")){
//                num="3";
//            }else if(num.equals("10")){
//                num="4";
//            }
            String time = TimeHelper.getDateString(String.valueOf(System.currentTimeMillis()));
            String url= AppConfig.ATTEND_AWARD_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid" , CoresunApp.USER_ID));
            list.add(new Params("num" , String.valueOf(num)));
            list.add(new Params("aid" , aid));
            list.add(new Params("time" , time));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("抽奖业务异常:"+e.getMessage());
        }
    }

    /**
     * 抽奖池抽奖
     * @param context 上下文对象
     * @param enty 活动实体
     */
    public static void attendByPool(Context context, ActivityListEntity enty, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.ATTEND_AWARD_FOR_POOL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("aid",String.valueOf(enty.getId())));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("抽奖池抽奖业务异常:"+e.getMessage());
        }
    }


    /**
     *   即开抽奖 第一次查询库存
     */
    public static void checkStockGet(Context context, String cid, String pid, HttpRequestListener listener){
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.ACT_CHECK_STOCKET_GET;
            List<Params> list=new ArrayList<>();
            list.add(new Params("cid", cid));
            list.add(new Params("type" , "1"));
            list.add(new Params("fid" , pid));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("抽奖池抽奖业务异常:"+e.getMessage());
        }
    }

    /**
     *   即开抽奖 第二次查询库存
     */
    public static void checkStockSet(Context context, String cid, String pid , String uid,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.ACT_CHECK_STOCKET_SET;
            List<Params> list=new ArrayList<>();
            list.add(new Params("cid", cid));
            list.add(new Params("type" , "1"));
            list.add(new Params("fid" , pid));
            list.add(new Params("user_id" , uid ));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("抽奖池抽奖业务异常:"+e.getMessage());
        }
    }


}
