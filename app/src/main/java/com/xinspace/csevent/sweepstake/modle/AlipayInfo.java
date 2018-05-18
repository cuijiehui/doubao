package com.xinspace.csevent.sweepstake.modle;

import com.google.gson.Gson;

/**
 * Created by Android on 2017/9/25.
 */

public class AlipayInfo {

    /**
     * code : 200
     * message : success
     * data : {"orderinfo":"partner=\"2088121179519266\"&seller_id=\"coresun@coresun.net\"&out_trade_no=\"SH20170614163032282862\"&subject=\"拾得折扣商城订单\"&body=\"1:0\"&total_fee=\"86.00\"&notify_url=\"http://localhost/renren/addons/ewei_shopv2/payment/alipay/notify.php\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&return_url=\"http://localhost/renren/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.pay_alipay.complete&openid=o0S_71apEecbaHDxTI_Ylp0135Jg&fromwechat=0&mid=2192\"&sign=\"NXeCuAIm4dOp5IEWPhfOgJC/PYWD2VRfLXZF/lvWwuNBDHIGKtVMfoFi9fxLEDarDo9WIlxtzTw24Y4KBVFy4wsGFBeefAeSe9aqrjnHsjO8yDyxMvFFxDyUmlvOTIG8CU5EhLIqDjNwLWqGNTHK0gX2GVmU8PobYkPF8hUOnUg=\"&sign_type=\"RSA\"","success":true}
     */

    private String code;
    private String message;
    private DataBean data;

    public static AlipayInfo objectFromData(String str) {

        return new Gson().fromJson(str, AlipayInfo.class);
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
         * orderinfo : partner="2088121179519266"&seller_id="coresun@coresun.net"&out_trade_no="SH20170614163032282862"&subject="拾得折扣商城订单"&body="1:0"&total_fee="86.00"&notify_url="http://localhost/renren/addons/ewei_shopv2/payment/alipay/notify.php"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&return_url="http://localhost/renren/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.pay_alipay.complete&openid=o0S_71apEecbaHDxTI_Ylp0135Jg&fromwechat=0&mid=2192"&sign="NXeCuAIm4dOp5IEWPhfOgJC/PYWD2VRfLXZF/lvWwuNBDHIGKtVMfoFi9fxLEDarDo9WIlxtzTw24Y4KBVFy4wsGFBeefAeSe9aqrjnHsjO8yDyxMvFFxDyUmlvOTIG8CU5EhLIqDjNwLWqGNTHK0gX2GVmU8PobYkPF8hUOnUg="&sign_type="RSA"
         * success : true
         */

        private String orderinfo;
        private boolean success;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public String getOrderinfo() {
            return orderinfo;
        }

        public void setOrderinfo(String orderinfo) {
            this.orderinfo = orderinfo;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
