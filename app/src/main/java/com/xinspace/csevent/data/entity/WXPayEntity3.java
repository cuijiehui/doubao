package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 微信订单信息实体
 */
public class WXPayEntity3 implements Serializable{


    /**
     * return_code : SUCCESS
     * return_msg : OK
     * appid : wx5f8889d080750afe
     * mch_id : 1293401801
     * device_info : ewei_shopv2
     * nonce_str : lPUKsXJ6dtCbN6op
     * sign : 21F7F1A7924B3B7384035E5714FB62B2
     * result_code : SUCCESS
     * prepay_id : wx201706091542066cb434c9c80543865478
     * trade_type : APP
     * success : true
     * weixin_jie : true
     */

    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String prepay_id;
    private String trade_type;
    private boolean success;
    private boolean weixin_jie;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isWeixin_jie() {
        return weixin_jie;
    }

    public void setWeixin_jie(boolean weixin_jie) {
        this.weixin_jie = weixin_jie;
    }
}
