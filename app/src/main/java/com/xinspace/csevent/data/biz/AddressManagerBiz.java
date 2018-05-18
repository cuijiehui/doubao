package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;

/**
 * Created by lizhihong on 2015/12/8.
 * 发送请求返回地址列表
 */
public class AddressManagerBiz {
    /**
     * 获取地址列表
     * @param listener 回调
     */
    public static void getAddressList(String openid ,HttpRequestListener listener ){
        //得到收货地址的请求地址
        String url = AppConfig.GET_ADDRESS_URL;
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            //传入用户id参数
            ArrayList<Params> listParams = new ArrayList<>();
            listParams.add(new Params("openid", openid));
            httpUtil.sendPost4(url,listParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     *
     * @param listener
     */
    public static void addAddressList(String openid , String id , String aid , String realname , String mobile ,HttpRequestListener listener ){
        //得到收货地址的请求地址
        String url = AppConfig.GET_ADDRESS_URL;
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            //传入用户id参数
            ArrayList<Params> listParams = new ArrayList<>();
            listParams.add(new Params("openid", openid));
            listParams.add(new Params("id", id));
            listParams.add(new Params("aid", aid));
            listParams.add(new Params("realname", realname));
            listParams.add(new Params("mobile", mobile));
            httpUtil.sendPost4(url,listParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addAddressList2(String openid , String id , String aid , String realname , String mobile ,HttpRequestListener listener ){
        //得到收货地址的请求地址
        String url = AppConfig.ADD_TRY_ADDRESS_URL;
        try {
            HttpUtil httpUtil= new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            //传入用户id参数
            ArrayList<Params> listParams = new ArrayList<>();
            listParams.add(new Params("openid", openid));
            listParams.add(new Params("id", id));
            listParams.add(new Params("aid", aid));
            listParams.add(new Params("realname", realname));
            listParams.add(new Params("mobile", mobile));
            httpUtil.sendPost(url,listParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
