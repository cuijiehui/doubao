package com.xinspace.csevent.data.biz;


import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/***
 * 获取活动的详细信息
 */
public class GetActDetailInfoBiz {



    /**
     *   即开抽奖活动
     */
    public static void getDetail(ActivityListEntity enty, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.ACTIVITY_INFORMATION;
            List<Params> list=new ArrayList<>();
            if(CoresunApp.USER_ID==null){
                list.add(new Params("uid",""));
            }else{
                list.add(new Params("uid", CoresunApp.USER_ID));
            }
            list.add(new Params("activity_id",String.valueOf(enty.getId())));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     *   众筹抽奖活动
     */
    public static void getCrowdDetail(String actId, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.CROWD_ACT_INFORMATION;
            List<Params> list=new ArrayList<>();
            if(CoresunApp.USER_ID==null){
                list.add(new Params("uid",""));
            }else{
                list.add(new Params("uid", CoresunApp.USER_ID));
            }
            list.add(new Params("activity_id",actId));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
