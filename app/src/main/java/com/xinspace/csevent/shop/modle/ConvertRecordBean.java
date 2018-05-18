package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/19.
 */

public class ConvertRecordBean implements Serializable{

    /**
     * id : 119
     * orderno : EL20170619142313784835
     * goodsid : 26
     * title : 【adpo】阿迪普 磁力充电线 智能免插式金属数据线 磁铁吸附线
     * thumb : http://wx.szshide.shop/attachment/images/1/2017/04/KzX0N9f2FL5XFgkf0q9xPFLlP0KQ59.jpg
     * credit : 100
     * type : 1
     * money : 0.00
     * createtime : 2017-06-19 14:23:13
     * status : 2
     * acttype : 1
     */

    private String id;
    private String orderno;
    private String goodsid;
    private String title;
    private String thumb;
    private String credit;
    private String type;
    private String money;
    private String createtime;
    private String status;
    private int acttype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
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

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getActtype() {
        return acttype;
    }

    public void setActtype(int acttype) {
        this.acttype = acttype;
    }
}
