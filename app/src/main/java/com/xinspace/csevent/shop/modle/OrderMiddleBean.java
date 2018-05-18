package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/16.
 */

public class OrderMiddleBean implements Serializable{


    /**
     * goodsid : 270
     * total : 2
     * title : 三生三世百变气垫CC霜，5大功效合一，修颜、保湿、遮瑕、隔离、防水低温，滋润提高保温肌肤，如羽毛般的轻盈质地，清爽不粘腻，带给肌肤极致的舒适感。
     * thumb : http://shop.coresun.net/attachment/images/1/2017/05/D6l43Zyl536l7PA6SpSjAA9wyZJvmM.jpg?t=fj8JZf955fTR0tj98TrZjwHF9fURy1Cj88j5TJJm088518jT99
     * status : 1
     * price : 277.20
     * optiontitle :
     * optionid : 0
     * specs : null
     * merchid : 0
     * seckill : 0
     * seckill_taskid : 0
     * sendtype : 0
     * expresscom :
     * expresssn :
     * express :
     * sendtime : 0
     * finishtime : 0
     * remarksend :
     * seckilltask : false
     */

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String goodsid;
    private String total;
    private String title;
    private String thumb;
    private String status;
    private String price;
    private String optiontitle;
    private String optionid;
    private Object specs;
    private String merchid;
    private String seckill;
    private String seckill_taskid;
    private String sendtype;
    private String expresscom;
    private String expresssn;
    private String express;
    private String sendtime;
    private String finishtime;
    private String remarksend;
    private boolean seckilltask;

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOptiontitle() {
        return optiontitle;
    }

    public void setOptiontitle(String optiontitle) {
        this.optiontitle = optiontitle;
    }

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
    }

    public Object getSpecs() {
        return specs;
    }

    public void setSpecs(Object specs) {
        this.specs = specs;
    }

    public String getMerchid() {
        return merchid;
    }

    public void setMerchid(String merchid) {
        this.merchid = merchid;
    }

    public String getSeckill() {
        return seckill;
    }

    public void setSeckill(String seckill) {
        this.seckill = seckill;
    }

    public String getSeckill_taskid() {
        return seckill_taskid;
    }

    public void setSeckill_taskid(String seckill_taskid) {
        this.seckill_taskid = seckill_taskid;
    }

    public String getSendtype() {
        return sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }

    public String getExpresscom() {
        return expresscom;
    }

    public void setExpresscom(String expresscom) {
        this.expresscom = expresscom;
    }

    public String getExpresssn() {
        return expresssn;
    }

    public void setExpresssn(String expresssn) {
        this.expresssn = expresssn;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getRemarksend() {
        return remarksend;
    }

    public void setRemarksend(String remarksend) {
        this.remarksend = remarksend;
    }

    public boolean isSeckilltask() {
        return seckilltask;
    }

    public void setSeckilltask(boolean seckilltask) {
        this.seckilltask = seckilltask;
    }
}
