package com.xinspace.csevent.shop.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/5/16.
 */

public class AllOrderBean implements Serializable{


    /**
     * id : 659
     * addressid : 139
     * ordersn : SH20170516144027453492
     * price : 439.20
     * dispatchprice : 0.00
     * status : 0
     * iscomment : 0
     * isverify : 0
     * verifyendtime : 0
     * verified : 0
     * verifycode :
     * verifytype : 0
     * refundid : 0
     * expresscom :
     * express :
     * expresssn :
     * finishtime : 0
     * virtual : 0
     * sendtype : 0
     * paytype : 0
     * refundstate : 0
     * dispatchtype : 0
     * verifyinfo : a:0:{}
     * merchid : 0
     * isparent : 0
     * userdeleted : 0
     * goods : [{"shopname":"拾得折扣商城","goods":[{"goodsid":"270","total":"2","title":"三生三世百变气垫CC霜，5大功效合一，修颜、保湿、遮瑕、隔离、防水低温，滋润提高保温肌肤，如羽毛般的轻盈质地，清爽不粘腻，带给肌肤极致的舒适感。","thumb":"http://shop.coresun.net/attachment/images/1/2017/05/D6l43Zyl536l7PA6SpSjAA9wyZJvmM.jpg?t=fj8JZf955fTR0tj98TrZjwHF9fURy1Cj88j5TJJm088518jT99","status":"1","price":"277.20","optiontitle":"","optionid":"0","specs":null,"merchid":"0","seckill":"0","seckill_taskid":"0","sendtype":"0","expresscom":"","expresssn":"","express":"","sendtime":"0","finishtime":"0","remarksend":"","seckilltask":false},{"goodsid":"277","total":"2","title":"【中科牌】天麻提取物胶囊 改善睡眠 0.35g/粒x10粒 每100克含天麻素2.0克 适宜睡眠状况不佳人群 带来一夜好睡眠","thumb":"http://shop.coresun.net/attachment/images/1/2017/05/OIPVPIQrVZPPILP5tpmim5Hpq11PqT.jpg?t=o855YVc4V0VFYKEVzPVH84CenoP05kXzk4CQ8y8fu5YfOuCXkc","status":"1","price":"162.00","optiontitle":"","optionid":"0","specs":null,"merchid":"0","seckill":"0","seckill_taskid":"0","sendtype":"0","expresscom":"","expresssn":"","express":"","sendtime":"0","finishtime":"0","remarksend":"","seckilltask":false}]}]
     * goods_num : 2
     * statusstr : 待付款
     * statuscss : text-cancel
     * canrefund : false
     * canverify : false
     */

    private String id;
    private String addressid;
    private String ordersn;
    private String price;
    private String dispatchprice;
    private String status;
    private String iscomment;
    private String isverify;
    private String verifyendtime;
    private String verified;
    private String verifycode;
    private String verifytype;
    private String refundid;
    private String expresscom;
    private String express;
    private String expresssn;
    private String finishtime;
    private String virtual;
    private String sendtype;
    private String paytype;
    private String refundstate;
    private String dispatchtype;
    private String verifyinfo;
    private String merchid;
    private String isparent;
    private String userdeleted;
    private String goods_num;
    private String statusstr;
    private String statuscss;
    private boolean canrefund;
    private boolean canverify;

    List<OrderMiddleBean> middleBeanList;

    public List<OrderMiddleBean> getMiddleBeanList() {
        return middleBeanList;
    }

    public void setMiddleBeanList(List<OrderMiddleBean> middleBeanList) {
        this.middleBeanList = middleBeanList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDispatchprice() {
        return dispatchprice;
    }

    public void setDispatchprice(String dispatchprice) {
        this.dispatchprice = dispatchprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIscomment() {
        return iscomment;
    }

    public void setIscomment(String iscomment) {
        this.iscomment = iscomment;
    }

    public String getIsverify() {
        return isverify;
    }

    public void setIsverify(String isverify) {
        this.isverify = isverify;
    }

    public String getVerifyendtime() {
        return verifyendtime;
    }

    public void setVerifyendtime(String verifyendtime) {
        this.verifyendtime = verifyendtime;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

    public String getVerifytype() {
        return verifytype;
    }

    public void setVerifytype(String verifytype) {
        this.verifytype = verifytype;
    }

    public String getRefundid() {
        return refundid;
    }

    public void setRefundid(String refundid) {
        this.refundid = refundid;
    }

    public String getExpresscom() {
        return expresscom;
    }

    public void setExpresscom(String expresscom) {
        this.expresscom = expresscom;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpresssn() {
        return expresssn;
    }

    public void setExpresssn(String expresssn) {
        this.expresssn = expresssn;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getVirtual() {
        return virtual;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }

    public String getSendtype() {
        return sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getRefundstate() {
        return refundstate;
    }

    public void setRefundstate(String refundstate) {
        this.refundstate = refundstate;
    }

    public String getDispatchtype() {
        return dispatchtype;
    }

    public void setDispatchtype(String dispatchtype) {
        this.dispatchtype = dispatchtype;
    }

    public String getVerifyinfo() {
        return verifyinfo;
    }

    public void setVerifyinfo(String verifyinfo) {
        this.verifyinfo = verifyinfo;
    }

    public String getMerchid() {
        return merchid;
    }

    public void setMerchid(String merchid) {
        this.merchid = merchid;
    }

    public String getIsparent() {
        return isparent;
    }

    public void setIsparent(String isparent) {
        this.isparent = isparent;
    }

    public String getUserdeleted() {
        return userdeleted;
    }

    public void setUserdeleted(String userdeleted) {
        this.userdeleted = userdeleted;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getStatusstr() {
        return statusstr;
    }

    public void setStatusstr(String statusstr) {
        this.statusstr = statusstr;
    }

    public String getStatuscss() {
        return statuscss;
    }

    public void setStatuscss(String statuscss) {
        this.statuscss = statuscss;
    }

    public boolean isCanrefund() {
        return canrefund;
    }

    public void setCanrefund(boolean canrefund) {
        this.canrefund = canrefund;
    }

    public boolean isCanverify() {
        return canverify;
    }

    public void setCanverify(boolean canverify) {
        this.canverify = canverify;
    }
}
