package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/28.
 */

public class ScreenGoodsBean implements Serializable{

    private String isnew;            //新品
    private String ishot;            //热卖
    private String isrecommand;      //推荐
    private String isdiscount;       //折扣
    private String istime;           //限时
    private String issendfree;       //包邮
    private String keywords;         //搜索
    private String cate;             //分类

    private String order;     // 销量(sales),价格(minprice)
    private String by;        // asc(小到大),desc(大到小)

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getIsrecommand() {
        return isrecommand;
    }

    public void setIsrecommand(String isrecommand) {
        this.isrecommand = isrecommand;
    }

    public String getIsdiscount() {
        return isdiscount;
    }

    public void setIsdiscount(String isdiscount) {
        this.isdiscount = isdiscount;
    }

    public String getIstime() {
        return istime;
    }

    public void setIstime(String istime) {
        this.istime = istime;
    }

    public String getIssendfree() {
        return issendfree;
    }

    public void setIssendfree(String issendfree) {
        this.issendfree = issendfree;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
}
