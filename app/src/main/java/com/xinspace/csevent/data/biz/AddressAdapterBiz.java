package com.xinspace.csevent.data.biz;

import android.content.Context;
import android.widget.Adapter;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.adapter.AddressAdapter;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class AddressAdapterBiz {

    //向服务器发请求删除地址
    public static void addressDelete(Context context , String id,Adapter adapter){
        String url= AppConfig.DELETE_ADDRESS_URL;
        List<Params> list=new ArrayList<>();
        list.add(new Params("id", id));
        list.add(new Params("uid", CoresunApp.USER_ID));
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((AddressAdapter)adapter);
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addressDelete2(String id, String openid, HttpRequestListener listener){
        String url= AppConfig.DELETE_ADDRESS_URL2;
        List<Params> list=new ArrayList<>();
        list.add(new Params("id", id));
        list.add(new Params("openid",openid));
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //向服务器发请求修改默认地址
    public static void changeDefaultAddress(Context context , String id,Adapter adapter){
        String url= AppConfig.DEFAULT_ADDRESS_URL;
        List<Params> list=new ArrayList<>();
        list.add(new Params("aid", id));
        list.add(new Params("uid", CoresunApp.USER_ID));
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((AddressAdapter)adapter);
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void changeDefaultAddress2(String id, String openid , HttpRequestListener listener){
        String url= AppConfig.DEFAULT_ADDRESS_URL2;
        List<Params> list=new ArrayList<>();
        list.add(new Params("id", id));
        list.add(new Params("openid", openid));
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
