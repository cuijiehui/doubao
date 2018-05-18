package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取夺宝记录列表
 */
public class GetDuobaoRecordBiz {
    /**
     * 获取夺宝记录中的全部记录
     */
    public static void getRecordByAll(HttpRequestListener listener){
        try{
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            String url= AppConfig.DUO_BAO_RECORD_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
