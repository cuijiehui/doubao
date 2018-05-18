package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/16.
 */

public class ExGoodsBean implements Serializable{


    /**
     * id : 26
     * title : 【adpo】阿迪普 磁力充电线 智能免插式金属数据线 磁铁吸附线
     * thumb : http://wx.szshide.shop/attachment/images/1/2017/04/KzX0N9f2FL5XFgkf0q9xPFLlP0KQ59.jpg
     * price : 45.00
     * credit : 100
     * money : 0.00
     * hasoption : 0
     */

    private String id;
    private String title;
    private String thumb;
    private String price;
    private String credit;
    private String money;
    private String hasoption;
    private int tab;
    private String specId;
    private String detail_url;

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getHasoption() {
        return hasoption;
    }

    public void setHasoption(String hasoption) {
        this.hasoption = hasoption;
    }
}
