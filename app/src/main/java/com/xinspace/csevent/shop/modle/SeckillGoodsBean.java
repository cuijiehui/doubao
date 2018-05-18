package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/7.
 */

public class SeckillGoodsBean implements Serializable{

    /**
     * goodsid : 223
     * price : 26.60
     * total : 10
     * totalmaxbuy : 1
     * title : 【净安佳】鞋袜净味生物酶喷剂 250ml 源自宝岛台湾 生物酶抑菌净味技术 鞋袜除臭抑菌喷雾
     * thumb : http://wx.szshide.shop/attachment/images/1/2017/05/h4e90OG509zzeE7keLk9LogsilIhZE.jpg
     * marketprice : 30.40
     * hasoption : 0
     * percen : 0
     */

    private String goodsid;
    private String price;
    private String total;
    private String totalmaxbuy;
    private String title;
    private String thumb;
    private String marketprice;
    private String hasoption;
    private int percen;

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalmaxbuy() {
        return totalmaxbuy;
    }

    public void setTotalmaxbuy(String totalmaxbuy) {
        this.totalmaxbuy = totalmaxbuy;
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

    public String getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(String marketprice) {
        this.marketprice = marketprice;
    }

    public String getHasoption() {
        return hasoption;
    }

    public void setHasoption(String hasoption) {
        this.hasoption = hasoption;
    }

    public int getPercen() {
        return percen;
    }

    public void setPercen(int percen) {
        this.percen = percen;
    }
}
