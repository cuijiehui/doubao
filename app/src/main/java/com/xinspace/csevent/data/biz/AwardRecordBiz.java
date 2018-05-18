package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 联网获取中奖记录
 */
public class AwardRecordBiz {
    public static void getRecord(HttpRequestListener listener){
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);

            String url=AppConfig.PRIZES_LIST_URL;
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("获取奖品记录列表异常:"+e.getMessage());
        }

    }

}
