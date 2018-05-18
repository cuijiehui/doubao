package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/16.
 */

public class RecommendBean implements Serializable{

    /**
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/05/dF4NFvavbaDGQFJjqUZgfFUBJZQf5V.jpg
     * title : 【冰穹】南极磷虾油 夹心型糖果 礼盒装 45g*3瓶 高标准 安全无添加  血管健康 健脑益智 预防老年痴呆
     * price : 99.00
     * gid : 213
     * total : 200
     * bargain : 0
     * productprice : 840.00
     * credit : null
     * ctype : null
     * gtype : null
     * sales : 134
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
