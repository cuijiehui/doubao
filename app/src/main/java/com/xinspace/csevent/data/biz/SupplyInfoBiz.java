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
 * 添加完善资料接口
 */
public class SupplyInfoBiz {
    //写入资料接口
    public static void supplyInfo(String nickname, String sex, String birthday, String salary, String email, String interest, HttpRequestListener listener){
        try{
            String url=AppConfig.SUPPLY_INFO_WRITE_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("nickname", nickname));
            list.add(new Params("sex", sex));
            list.add(new Params("birthday", birthday));
            list.add(new Params("salary", salary));
            list.add(new Params("email", email));
            list.add(new Params("interest", interest));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("添加完善资料接口(写入)数据出错:"+e.getMessage());
        }
    }
    //读取资料接口
    public static void getInfo(HttpRequestListener listener){
        try{
            String url=AppConfig.SUPPLY_INFO_READ_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("添加完善资料接口(读取)数据出错:"+e.getMessage());
        }
    }
}
