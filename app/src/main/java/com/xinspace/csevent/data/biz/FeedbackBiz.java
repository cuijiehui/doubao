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
 * 签到业务
 */
public class FeedbackBiz {
    public static void commit(String tel, String feedback, HttpRequestListener listener){
        try{
            String url=AppConfig.FEEDBACK_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            List<Params> list=new ArrayList<>();
            list.add(new Params("tel", tel));
            list.add(new Params("feedback", feedback));
            list.add(new Params("uid", CoresunApp.USER_ID));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("意见反馈接口错误信息:"+e.getMessage());
        }
    }
}
