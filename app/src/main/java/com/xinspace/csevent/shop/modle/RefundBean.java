package com.xinspace.csevent.shop.modle;

/**
 * Created by Android on 2017/5/19.
 */

public class RefundBean {

    private String orderId;
    private String openId;
    private String rtype;
    private String reason;
    private String price;
    private String content;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
