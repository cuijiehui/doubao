package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/4/24.
 */

public class FristGoodsBean implements Serializable{

    /**
     * thumb : http://shop.coresun.net/attachment/images/1/2017/04/Sc46Wwg6wzxE7wtc38G7QCk3twVuE4.jpg
     * title : 【东方喜炮】吉祥红 52度浓香型白酒 入口绵柔 回味尤香 绵柔甘冽
     * price : 52.80
     * gid : 205
     * total : 18
     * bargain : 0
     * productprice : 88.00
     * credit : null
     * ctype : null
     * gtype : null
     * sales : 166
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
    private Object credit;
    private Object ctype;
    private Object gtype;
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

    public Object getCredit() {
        return credit;
    }

    public void setCredit(Object credit) {
        this.credit = credit;
    }

    public Object getCtype() {
        return ctype;
    }

    public void setCtype(Object ctype) {
        this.ctype = ctype;
    }

    public Object getGtype() {
        return gtype;
    }

    public void setGtype(Object gtype) {
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
