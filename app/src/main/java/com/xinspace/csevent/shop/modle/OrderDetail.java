package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/18.
 */

public class OrderDetail implements Serializable{


    /**
     * ordersn : SH20170518102107636428
     * status : 0
     * goodsprice : 81.00
     * oldprice : 81.00
     * olddispatchprice : 0.00
     * address : {"id":"139","uniacid":"1","openid":"wap_user_1_18810199893","realname":"王先森","mobile":"18810199893","province":"福建省","city":"福州市","area":"仓山区","address":"该词可口可乐白龙马","isdefault":"1","zipcode":"","deleted":"0","street":"","datavalue":"","streetdatavalue":""}
     * createtime : 2017-05-18 10:21:07
     * finishtime :
     * paytime :
     * goods : [{"goodsid":"277","price":"81.00","title":"【中科牌】天麻提取物胶囊 改善睡眠 0.35g/粒x10粒 每100克含天麻素2.0克 适宜睡眠状况不佳人群 带来一夜好睡眠","thumb":"images/1/2017/05/OIPVPIQrVZPPILP5tpmim5Hpq11PqT.jpg","status":"1","cannotrefund":"0","total":"1","credit":"","optionid":"0","optiontitle":"","isverify":"1","storeids":"","seckill":"0","isfullback":"0","seckill_taskid":"0","diyformfields":[],"diyformdata":[],"seckill_task":false}]
     */
    private String ordersn;
    private String status;
    private String goodsprice;
    private String oldprice;
    private String olddispatchprice;
    private String createtime;
    private String finishtime;
    private String paytime;
    private String refundstate;

    private String ccard;
    private String paytype;

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getSendtype() {
        return sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }

    private String sendtype;

    public String getCcard() {
        return ccard;
    }

    public void setCcard(String ccard) {
        this.ccard = ccard;
    }

    public String getRefundstate() {
        return refundstate;
    }

    public void setRefundstate(String refundstate) {
        this.refundstate = refundstate;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getOlddispatchprice() {
        return olddispatchprice;
    }

    public void setOlddispatchprice(String olddispatchprice) {
        this.olddispatchprice = olddispatchprice;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }
}
