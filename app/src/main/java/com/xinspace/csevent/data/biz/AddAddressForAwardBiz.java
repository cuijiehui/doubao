package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.AddAddressForAwardActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 联网获取获奖记录
 */
public class AddAddressForAwardBiz {
    /**
     * 为没有收货地址的奖品添加地址
     * @param context 上下文
     * @param registration_id 没有地址的奖品的记录表中的id
     * @param name  姓名
     * @param remarks 备注
     * @param rtel 电话
     * @param address 地址
     */
    public static void commitAddress(Context context,String registration_id,String name,String remarks,String rtel,String address){
        String url=AppConfig.COMMIT_ADDRESS_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((AddAddressForAwardActivity)context);
            List<Params> list = new ArrayList<>();
            list.add(new Params("registration_id", registration_id));
            list.add(new Params("name", name));
            list.add(new Params("remarks", remarks));
            list.add(new Params("rtel", rtel));
            list.add(new Params("address", address));
            list.add(new Params("uid", CoresunApp.USER_ID));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("提交收货地址出了异常:" + e.getMessage());
        }

    }

    /**
     * 为小奖品添加收货地址
     * @param context 上下文
     * @param id 在小奖品记录表中的id
     * @param name 姓名
     * @param rtel 电话
     * @param address 地址
     */
    public static void addAddressForSmallPrize(Context context,String id,String name,String rtel,String address){
        String url=AppConfig.ADD_ADDRESS_FOR_SMALL_PRIZE_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((AddAddressForAwardActivity) context);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("name", name));
            list.add(new Params("rtel", rtel));
            list.add(new Params("address", address));
            list.add(new Params("uid", CoresunApp.USER_ID));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("添加小奖品收货地址出了异常:" + e.getMessage());
        }

    }

}
