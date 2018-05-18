package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/3.
 */

public class JiuGoodsBean implements Serializable{


    /**
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/06/xl69J74DKss7ZeG5zQm1O9uM7Sok9O.jpg
     * title : 【买二送一】时尚领袖 USB随身迷你小风扇 微型静音 携带便捷 可插在充电宝笔记本USB的迷你小风扇
     * price : 9.90
     * gid : 297
     * total : 1199
     * bargain : 0
     * productprice : 19.90
     * credit :
     * ctype :
     * gtype :
     * sales : 55
     * imgurl :
     * linkurl :
     */

    private String thumb;
    private String title;
    private String price;
    private String gid;
    private String total;
    private String bargain;
    private String productprice;
    private String credit;
    private String ctype;
    private String gtype;
    private int sales;
    private String imgurl;
    private String linkurl;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBargain() {
        return bargain;
    }

    public void setBargain(String bargain) {
        this.bargain = bargain;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getGtype() {
        return gtype;
    }

    public void setGtype(String gtype) {
        this.gtype = gtype;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }
}
