package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 微信订单信息实体
 */
public class WXPayEntity implements Serializable{

    private String result_code;
    private String sign;
    private String mch_id;
    private String prepay_id;
    private String return_msg;
    private String appid;
    private String nonce_str;
    private String return_code;
    private String trade_type;

    public WXPayEntity() {
    }

    public WXPayEntity(String result_code, String sign, String mch_id, String prepay_id, String return_msg, String appid, String nonce_str, String return_code, String trade_type) {
        this.result_code = result_code;
        this.sign = sign;
        this.mch_id = mch_id;
        this.prepay_id = prepay_id;
        this.return_msg = return_msg;
        this.appid = appid;
        this.nonce_str = nonce_str;
        this.return_code = return_code;
        this.trade_type = trade_type;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
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

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    @Override
    public String toString() {
        return "WXPayEntity{" +
                "result_code='" + result_code + '\'' +
                ", sign='" + sign + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", appid='" + appid + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", return_code='" + return_code + '\'' +
                ", trade_type='" + trade_type + '\'' +
                '}';
    }
}
