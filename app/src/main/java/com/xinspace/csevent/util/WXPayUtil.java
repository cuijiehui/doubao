package com.xinspace.csevent.util;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.data.entity.WXPayEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.wxapi.Constants;
import com.xinspace.csevent.wxapi.MD5;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信支付工具类
 */
public class WXPayUtil implements HttpRequestListener{
    private Context context;
    private IWXAPI api;
    private String timeStamp;

    public WXPayUtil(Context context){
        this.context=context;
        regToWX();
    }

    /**
     * @param tradeNo 订单号
     */
    public void pay(String tradeNo){
       try{
           String url = AppConfig.PAY_INFO;
           Toast.makeText(CoresunApp.instance, "获取订单中...", Toast.LENGTH_SHORT).show();
           HttpUtil http=new HttpUtil();
           http.setOnHttpRequestFinishListener(this);

           List<Params> list=new ArrayList<>();
           list.add(new Params("out_trade_no",tradeNo));
           http.sendPost(url,list);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    //将app注册到微信
    private void regToWX() {
        //通过工厂获取IWXAPI实例
        api= WXAPIFactory.createWXAPI(context, Constants.APP_ID, true);
        //将应用的appid注册到微信
        api.registerApp(Constants.APP_ID);
    }

    @Override
    public void onHttpRequestFinish(String result) {
        LogUtil.i("微信支付:"+result);
        if (result == null && result.equals("")){
            return;
        }

        WXPayEntity enty = (WXPayEntity) JsonPaser.parserObj(result, WXPayEntity.class);

        String appId=enty.getAppid();
        String partnerId=enty.getMch_id();
        String prepayId=enty.getPrepay_id();
        String packageValue="Sign=WXPay";
        String nonceStr=enty.getNonce_str();
        String sign=enty.getSign();
        String timeStamp=String.valueOf(System.currentTimeMillis() / 1000);
        LogUtil.i("appid="+appId);
        LogUtil.i("partnerId="+partnerId);
        LogUtil.i("prepayId="+prepayId);
        LogUtil.i("packageValue="+packageValue);
        LogUtil.i("nonceStr="+nonceStr);
        LogUtil.i("sign="+sign);
        LogUtil.i("timeStamp="+timeStamp);
        //根据参数获取签名
        sign=getSign(enty,timeStamp);
        
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId =partnerId;
        request.prepayId=prepayId;
        request.packageValue = packageValue;
        request.nonceStr= nonceStr;
        request.timeStamp= timeStamp;
        request.sign= sign;

        api.sendReq(request);
    }

    @Override
    public void onHttpRequestError(String error) {

    }

    //获取签名
    private String getSign(WXPayEntity enty,String timeStamp) {
        String appId=enty.getAppid();
        String partnerId=enty.getMch_id();
        String prepayId=enty.getPrepay_id();
        String packageValue="Sign=WXPay";
        String nonceStr=enty.getNonce_str();

        String stringA="appid="+appId+"&noncestr="+nonceStr+"&package="+packageValue+"&partnerid="+partnerId+"&prepayid="+prepayId+"&timestamp="+timeStamp;
        String tempSign=stringA+"&key="+Constants.key;

        String sign= MD5.getMessageDigest(tempSign.getBytes()).toUpperCase();

        LogUtil.i("拼接StringA:"+stringA);
        LogUtil.i("拼接tempSign:"+tempSign);
        LogUtil.i("最后签名:"+sign);
        return sign;
    }
}
