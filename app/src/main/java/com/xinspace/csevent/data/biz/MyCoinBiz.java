package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 联网获取获奖记录
 */
public class MyCoinBiz {
//    /**
//     * 获取金币兑换积分的比率接口
//     */
//    public static void getExchangeRate(MyCoinFragment fragment){
//        String url=AppConfig.GET_EXCHANGE_RATE_URL;
//        try {
//            HttpUtil httpUtil=new HttpUtil();
//            httpUtil.setOnHttpRequestFinishListener(fragment);
//            httpUtil.sendGet(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.i("金币兑换积分比率接口出了异常:"+e.getMessage());
//        }
//    }
//
//    /**
//     * 兑换积分接口
//     * @param kim 需要兑换的积分数
//     */
//    public static void exchange(MyCoinFragment fragment,String kim){
//        String url=AppConfig.CHARGE_URL;
//        try {
//            HttpUtil httpUtil=new HttpUtil();
//            httpUtil.setOnHttpRequestFinishListener(fragment);
//            List<Params> list=new ArrayList<>();
//            list.add(new Params("kim", kim));
//            list.add(new Params("user_id", CoresunApp.USER_ID));
//            list.add(new Params("type", "0"));
//            httpUtil.sendPost(url,list);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.i("金币兑换积分接口出了异常:"+e.getMessage());
//        }
//    }
}
