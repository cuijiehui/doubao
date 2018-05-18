package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhihong on 2015/12/8.
 * 地址编辑业务
 */
public class AddressEditBiz {


    /**
     * 添加地址
     */
    public static void addAddress(String valueAddress, String valueName, String valuePhoneNum, String province,
                                   String city , String area ,HttpRequestListener listener){
        String url = AppConfig.ADD_ADDRESS_URL;
        List<Params> list = new ArrayList<>();
        list.add(new Params("openid", CoresunApp.USER_ID));
        list.add(new Params("tel", valuePhoneNum));
        list.add(new Params("name", valueName));
        list.add(new Params("address", valueAddress));
        list.add(new Params("province", province));
        list.add(new Params("city", city));
        list.add(new Params("area", area));
        try {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            httpUtil.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void addAddress2(String  openid,  String valueName, String valuePhoneNum, String areas, String valueAddress, HttpRequestListener listener){
        String url = AppConfig.ADD_ADDRESS_URL;
        List<Params> list = new ArrayList<>();
        list.add(new Params("openid", openid));
        list.add(new Params("mobile", valuePhoneNum));
        list.add(new Params("realname", valueName));
        list.add(new Params("areas", areas));
        list.add(new Params("address", valueAddress));
        try {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            httpUtil.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 编辑地址
     */
    public static void editAddress(String addressId,String  openid,  String valueName, String valuePhoneNum, String areas,
                                   String valueAddress, HttpRequestListener listener){

        //发送请求更新编辑后地址
        String url = AppConfig.ADD_ADDRESS_URL;//得到编辑地址的请求地址
        List<Params> list = new ArrayList<>();
        list.add(new Params("id", addressId));
        list.add(new Params("openid", openid));
        list.add(new Params("mobile", valuePhoneNum));
        list.add(new Params("realname", valueName));
        list.add(new Params("areas", areas));
        list.add(new Params("address", valueAddress));
        try {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            httpUtil.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
