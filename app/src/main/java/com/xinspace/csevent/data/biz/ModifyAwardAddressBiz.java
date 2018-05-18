package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.ModifyAwardAddressActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 联网获取获奖记录
 */
public class ModifyAwardAddressBiz {

    public static void commitAddress(Context context,String registration_id,String name,String remarks,String rtel,String address){
        String url=AppConfig.COMMIT_ADDRESS_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((ModifyAwardAddressActivity)context);
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
            LogUtil.i("提交收货地址出了异常:"+e.getMessage());
        }

    }
    /**
     * 已经有地址的普通奖品修改地址
     * @param context 上下文
     * @param id 奖品在已经有地址的记录表(prizes_order_id)中的id
     * @param rid 地址列表中的id
     * @param remarks 备注
     */
    public static void modifyAddress(Context context,String id,String rid,String remarks){
        String url=AppConfig.MODIFY_ADDRESS_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((ModifyAwardAddressActivity) context);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("rid", rid));
            list.add(new Params("remarks", remarks));
            list.add(new Params("uid", CoresunApp.USER_ID));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("修改收货地址出了异常:" + e.getMessage());
        }
    }


    /**
     *  确认地址
     */
    public static void affirmAddress(Context context,String id,GetAddressEntity getAddressEntity ,  HttpRequestListener listener){
        String url=AppConfig.MODIFY_ADDRESS_URL;
        String time = TimeHelper.getDateString(String.valueOf(System.currentTimeMillis()));
        try {
            HttpUtil httpUtil=new HttpUtil();
            //httpUtil.setOnHttpRequestFinishListener((ModifyAwardAddressActivity) context);
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("aid", id));
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("uaddress", getAddressEntity.getProvince() + getAddressEntity.getCity()
                    + getAddressEntity.getArea() + getAddressEntity.getAddress()));
            list.add(new Params("uname",getAddressEntity.getRealname()));
            list.add(new Params("utel", getAddressEntity.getMobile()));
            list.add(new Params("confirmation_time",time));
            list.add(new Params("remarks","00"));
            httpUtil.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("修改收货地址出了异常:" + e.getMessage());
        }
    }



    public static void addAddressForSmallPrize(Context context,String id,String name,String rtel,String address){
        String url=AppConfig.ADD_ADDRESS_FOR_SMALL_PRIZE_URL;
        try {
            HttpUtil httpUtil=new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((ModifyAwardAddressActivity) context);
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
