package com.xinspace.csevent.sweepstake.modle;

import com.google.gson.Gson;

/**
 * Created by Android on 2017/9/27.
 */

public class WeChatPayInfo {

    /**
     * code : 200
     * message : success
     * data : {"orderid":176,"wechat":{"return_code":"SUCCESS","return_msg":"OK","appid":"wx5f8889d080750afe","mch_id":"1293401801","device_info":"ewei_shopv2","nonce_str":"3FV6f12RiAPisIqe","sign":"B75F4BE320D3F92F0B9397735E4D26B7","result_code":"SUCCESS","prepay_id":"wx201709271611419eb756ae050807498108","trade_type":"APP","success":true,"weixin_jie":true},"money":"0.01"}
     */

    private String code;
    private String message;
    private DataBean data;

    public static WeChatPayInfo objectFromData(String str) {

        return new Gson().fromJson(str, WeChatPayInfo.class);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderid : 176
         * wechat : {"return_code":"SUCCESS","return_msg":"OK","appid":"wx5f8889d080750afe","mch_id":"1293401801","device_info":"ewei_shopv2","nonce_str":"3FV6f12RiAPisIqe","sign":"B75F4BE320D3F92F0B9397735E4D26B7","result_code":"SUCCESS","prepay_id":"wx201709271611419eb756ae050807498108","trade_type":"APP","success":true,"weixin_jie":true}
         * money : 0.01
         */

        private int orderid;
        private WechatBean wechat;
        private String money;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public WechatBean getWechat() {
            return wechat;
        }

        public void setWechat(WechatBean wechat) {
            this.wechat = wechat;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public static class WechatBean {
            /**
             * return_code : SUCCESS
             * return_msg : OK
             * appid : wx5f8889d080750afe
             * mch_id : 1293401801
             * device_info : ewei_shopv2
             * nonce_str : 3FV6f12RiAPisIqe
             * sign : B75F4BE320D3F92F0B9397735E4D26B7
             * result_code : SUCCESS
             * prepay_id : wx201709271611419eb756ae050807498108
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

            public static WechatBean objectFromData(String str) {

                return new Gson().fromJson(str, WechatBean.class);
            }

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
    }
}
