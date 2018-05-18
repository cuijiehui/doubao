package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/12.
 */

public class GroupFailBean implements Serializable{


    /**
     * id : 58
     * price : 38.80
     * freight : 0.00
     * creditmoney : 0.00
     * goodid : 10
     * teamid : 58
     * orderno : PT20170608164507088488
     * status : -1
     * title : 【两面针】两面针 中药牙膏药劲香型 消痛 深效修复牙膏  解决牙龈牙周等顽固问题引起的长期反复牙痛
     * gprice : 59.90
     * groupsprice : 38.80
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/04/vDDoe55lTe2b1Wx8X2tel8bEL10XE2.jpg
     * units : 件
     * goodsnum : 1
     * amount : 38.8
     * itemnum : 0
     * lasttime : -254443
     * starttime : 1970-01-01 08:00:00
     */

    private String id;
    private String price;
    private String freight;
    private String creditmoney;
    private String goodid;
    private String teamid;
    private String orderno;
    private String status;
    private String title;
    private String gprice;
    private String groupsprice;
    private String thumb;
    private String units;
    private String goodsnum;
    private double amount;
    private int itemnum;
    private int lasttime;
    private String starttime;
    private String url;
    private String stock;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getCreditmoney() {
        return creditmoney;
    }

    public void setCreditmoney(String creditmoney) {
        this.creditmoney = creditmoney;
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
    }

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGprice() {
        return gprice;
    }

    public void setGprice(String gprice) {
        this.gprice = gprice;
    }

    public String getGroupsprice() {
        return groupsprice;
    }

    public void setGroupsprice(String groupsprice) {
        this.groupsprice = groupsprice;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getItemnum() {
        return itemnum;
    }

    public void setItemnum(int itemnum) {
        this.itemnum = itemnum;
    }

    public int getLasttime() {
        return lasttime;
    }

    public void setLasttime(int lasttime) {
        this.lasttime = lasttime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
}
