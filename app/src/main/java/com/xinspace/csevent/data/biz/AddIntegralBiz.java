package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户下载发现页面的软件后,为用户添加积分
 */
public class AddIntegralBiz {
    /***
     * @param context 上下文对象
     * @param id 所下载软件的id
     */
    public static void addIntegral(Context context, String id, HttpRequestListener listener){
        try {
            //下载完成之后回调给用户添加积分的接口
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            List<Params> list = new ArrayList<>();
            String url = AppConfig.ADD_INTEGRAL_URL;
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("id", id));
            http.sendPost(url, list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
