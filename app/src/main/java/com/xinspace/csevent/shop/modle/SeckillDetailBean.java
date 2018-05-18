package com.xinspace.csevent.shop.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/6/12.
 */

public class SeckillDetailBean implements Serializable{


    /**
     * goodsid : 240
     * price : 39.50
     * total : 5
     * totalmaxbuy : 1
     * title : 【adpo】阿迪普 新款超薄便携电源 聚合物超薄卡片移动电源定制5000mah 机线一体 弃旧充电线、苹果转接心（选配）
     * thumb : images/1/2017/05/t4OnSZfM00n6WjwA04JVN00NJIhfzA.jpg
     * marketprice : 45.00
     * cates :
     * hasoption : 0
     * thumb_url : a:2:{i:1;s:51:"images/1/2017/04/pussH3kfwItGG5bt5T1HgG11gkGk5h.jpg";i:2;s:51:"images/1/2017/04/fHH7Hc3IAi3Fc5FepDI7C7cZAPEzd5.jpg";}
     * thumb_first : 0
     * subtitle : 优质用料，2.5D圆弧轮廓，勾勒精美弧线，9mm纤薄厚度，扁平化设计，携带方便，让您爱不释手，八重安全电路保护，高效转化聚合物电芯，多向充电，隐藏式接口，诠释高贵品味
     * isdiscount : 0
     * isdiscount_title :
     * isdiscount_time : 1493385540
     * isdiscount_discounts : {"type":0,"default":{"option0":""}}
     * content : <p><img src="http://wx.szshide.shop/attachment/images/1/2017/04/p5HJsrc8BJsuzjSy0s0SBbFwrBJ8Yf.jpg" width="100%" alt="images/1/2017/04/p5HJsrc8BJsuzjSy0s0SBbFwrBJ8Yf.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/h0m3AAHga999M90h9C5V017Y3gb059.jpg" width="100%" alt="images/1/2017/04/h0m3AAHga999M90h9C5V017Y3gb059.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/ad55MTEEj3eedjM93jEmcLClR7j573.jpg" width="100%" alt="images/1/2017/04/ad55MTEEj3eedjM93jEmcLClR7j573.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/Qn6XnpYhF805Ja2n05x8m6F5mfBQHM.jpg" width="100%" alt="images/1/2017/04/Qn6XnpYhF805Ja2n05x8m6F5mfBQHM.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/J71o787wtj4I84F4OjqPOiJ8JggoIG.jpg" width="100%" alt="images/1/2017/04/J71o787wtj4I84F4OjqPOiJ8JggoIG.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/DXLkOFq99dKK45KmomePy8PZ65YmdW.jpg" width="100%" alt="images/1/2017/04/DXLkOFq99dKK45KmomePy8PZ65YmdW.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/EKb8FQq1QcoLzQiQcLfI9jssz9sIcL.jpg" width="100%" alt="images/1/2017/04/EKb8FQq1QcoLzQiQcLfI9jssz9sIcL.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/YVmB460UMkB2B0KYbYYkZBMvUUY7eY.jpg" width="100%" alt="images/1/2017/04/YVmB460UMkB2B0KYbYYkZBMvUUY7eY.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/u5Pa4ZmAfAOnPep1app0TA0eeX4P09.jpg" width="100%" alt="images/1/2017/04/u5Pa4ZmAfAOnPep1app0TA0eeX4P09.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/pDs3qlAiIVhKIlIMD48gGLU2G33CAu.jpg" width="100%" alt="images/1/2017/04/pDs3qlAiIVhKIlIMD48gGLU2G33CAu.jpg"/><img src="http://wx.szshide.shop/attachment/images/1/2017/04/d00g3q7W66YkU0i33GF00HH67qw7X0.jpg" width="100%" alt="images/1/2017/04/d00g3q7W66YkU0i33GF00HH67qw7X0.jpg"/></p>
     * percen : 20
     */

    private String goodsid;
    private String price;
    private String total;
    private String totalmaxbuy;
    private String title;
    private String thumb;
    private String marketprice;
    private String cates;
    private String hasoption;
    private String thumb_url;
    private String thumb_first;
    private String subtitle;
    private String isdiscount;
    private String isdiscount_title;
    private String isdiscount_time;
    private String content;
    private int percen;


    private int starttime;
    private int endtime;
    private int resttime;
    private int status;
    private List<ParamsBean> paramsBeanList;


    private String goodsNum;

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public void setParamsBeanList(List<ParamsBean> paramsBeanList) {
        this.paramsBeanList = paramsBeanList;
    }

    public List<ParamsBean> getParamsBeanList() {
        return paramsBeanList;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getResttime() {
        return resttime;
    }

    public void setResttime(int resttime) {
        this.resttime = resttime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private List<String> imgUrlList;

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

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

    public String getCates() {
        return cates;
    }

    public void setCates(String cates) {
        this.cates = cates;
    }

    public String getHasoption() {
        return hasoption;
    }

    public void setHasoption(String hasoption) {
        this.hasoption = hasoption;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getThumb_first() {
        return thumb_first;
    }

    public void setThumb_first(String thumb_first) {
        this.thumb_first = thumb_first;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIsdiscount() {
        return isdiscount;
    }

    public void setIsdiscount(String isdiscount) {
        this.isdiscount = isdiscount;
    }

    public String getIsdiscount_title() {
        return isdiscount_title;
    }

    public void setIsdiscount_title(String isdiscount_title) {
        this.isdiscount_title = isdiscount_title;
    }

    public String getIsdiscount_time() {
        return isdiscount_time;
    }

    public void setIsdiscount_time(String isdiscount_time) {
        this.isdiscount_time = isdiscount_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPercen() {
        return percen;
    }

    public void setPercen(int percen) {
        this.percen = percen;
    }
}
