package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页"马上开始抽奖"的业务
 */
public class AttendAwardNowBiz {

    /**
     * @param pro 省份
     * @param city 城市
     * @param area 区
     */
    public static void attend(Context context, String userId, String pro, String city, String area, HttpRequestListener listener){

        try{
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.START_AWARD_NOW;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", userId));
            list.add(new Params("province",pro));
            list.add(new Params("city",city));
            list.add(new Params("area",area));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
