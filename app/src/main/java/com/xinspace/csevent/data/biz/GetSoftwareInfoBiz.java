package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取推荐软件的详细信息
 */
public class GetSoftwareInfoBiz {
    /***
     * @param id 相应的软件的id
     */
    public static void getInfo(String id, HttpRequestListener listener){
        try {
            String url= AppConfig.SOFTWARE_DETAIL_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("id",id));

            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
