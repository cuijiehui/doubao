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
 * 用户邮箱获取系统推送接口
 */
public class EmailBiz {
    public static void getSysMsg(HttpRequestListener listener){
        try{
            String url=AppConfig.USER_EMAIL_GET_SYS_MSG_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("用户邮箱获取系统推送接口数据出错:"+e.getMessage());
        }
    }
}
