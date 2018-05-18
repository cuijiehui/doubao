package com.xinspace.csevent.util;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.data.entity.WXPayEntity2;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.sweepstake.modle.WeChatPayInfo;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.wxapi.Constants;
import com.xinspace.csevent.wxapi.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信支付工具类
 */
public class WXPayUtil2 implements HttpRequestListener{
    private Context context;
    private IWXAPI api;
    private String timeStamp;

    public WXPayUtil2(Context context){
        this.context=context;
        regToWX();
    }

    /**
     * @param tradeNo 订单号
     */
    public void pay(String tradeNo){
       try{
           String url = AppConfig.WA_ORDER_NUM2;
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

    public void pay(String tradeNo , String price){
        try{
            String url =AppConfig.WA_ORDER_NUM2;
            Toast.makeText(CoresunApp.instance, "获取订单中...", Toast.LENGTH_SHORT).show();
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(this);
            List<Params> list=new ArrayList<>();
            list.add(new Params("ordersn",tradeNo));
            list.add(new Params("price",price));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void pay(String uid , String token ,String id){
        String url = AppConfig.COMMUNITY_BASE_URL + AppConfig.PAY_ORDER;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(this);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("id" , id));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void payExChange(WeChatPayInfo info){
        WeChatPayInfo.DataBean.WechatBean weChat = info.getData().getWechat();
        String appId = weChat.getAppid();
        String partnerId = "1293401801";    // TODO: 2017/9/27  商户ID暂时写死
        String prepayId = weChat.getPrepay_id();
        String packageValue = "Sign=WXPay";
        String nonceStr = weChat.getNonce_str();
        String sign = weChat.getSign();
        String timeStamp=String.valueOf(System.currentTimeMillis() / 1000);
        LogUtil.e("e appid="+appId);
        LogUtil.e("e partnereId="+partnerId);
        LogUtil.e("e prepayId="+prepayId);
        LogUtil.e("e packageValue="+packageValue);
        LogUtil.e("e nonceStr="+nonceStr);
        LogUtil.e("e sign="+sign);
        LogUtil.e("e timeStamp="+timeStamp);
        //根据参数获取签名
        sign=getSign(weChat,timeStamp);

        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;

        api.sendReq(request);


    }


    //将app注册到微信
    private void regToWX() {
        //通过工厂获取IWXAPI实例
        api= WXAPIFactory.createWXAPI(context, Constants.APP_ID, true);
        //将应用的appid注册到微信
        api.registerApp(Constants.APP_ID);
    }

    @Override
    public void onHttpRequestFinish(String result) throws JSONException {
        LogUtil.i("微信支付返回:"+result);
        WXPayEntity2 enty = null;
        if (result == null && result.equals("")){
            return;
        }

        if (result.contains("code")){
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("code").equals("200")){
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                enty = (WXPayEntity2) JsonPaser.parserObj(dataJsonObject.toString(), WXPayEntity2.class);
            }else{
                ToastUtil.makeToast(jsonObject.getString("message"));
                return;
            }
        }else{
            enty = (WXPayEntity2) JsonPaser.parserObj(result, WXPayEntity2.class);
        }

        String appId=enty.getAppid();
        String partnerId=enty.getPartnerid();
        String prepayId=enty.getPrepayid();
        String packageValue="Sign=WXPay";
        String nonceStr=enty.getNoncestr();
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
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;

        api.sendReq(request);
    }

    @Override
    public void onHttpRequestError(String error) {

    }

    //获取签名
    private String getSign(WXPayEntity2 enty,String timeStamp) {
        String appId=enty.getAppid();
        String partnerId=enty.getPartnerid();
        String prepayId=enty.getPrepayid();
        String packageValue="Sign=WXPay";
        String nonceStr=enty.getNoncestr();

        String stringA="appid="+appId+"&noncestr="+nonceStr+"&package="+packageValue+"&partnerid="+partnerId+"&prepayid="+prepayId+"&timestamp="+timeStamp;
        String tempSign=stringA+"&key="+Constants.key;

        String sign= MD5.getMessageDigest(tempSign.getBytes()).toUpperCase();

        LogUtil.i("拼接StringA:"+stringA);
        LogUtil.i("拼接tempSign:"+tempSign);
        LogUtil.i("最后签名:"+sign);
        return sign;
    }

    //获取签名
    private String getSign(WeChatPayInfo.DataBean.WechatBean weChat,String timeStamp) {
        String appId = weChat.getAppid();
        String partnerId = "1293401801";// TODO: 2017/9/27  商户ID暂时写死
        String prepayId = weChat.getPrepay_id();
        String packageValue = "Sign=WXPay";
        String nonceStr = weChat.getNonce_str();

        String stringA = "appid="+appId+"&noncestr="+nonceStr+"&package="+packageValue+"&partnerid="+partnerId+"&prepayid="+prepayId+"&timestamp="+timeStamp;
        String tempSign = stringA+"&key="+Constants.key;

        String sign= MD5.getMessageDigest(tempSign.getBytes()).toUpperCase();

        LogUtil.e("拼接StringA:"+stringA);
        LogUtil.e("拼接tempSign:"+tempSign);
        LogUtil.e("最后签名:"+sign);
        return sign;
    }
}
