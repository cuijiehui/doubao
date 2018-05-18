package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/8.
 */

public class GroupGoodsBean implements Serializable{


    /**
     * id : 12
     * title : 【移民汇】广东德庆特产 紫淮山1.5kg 新鲜紫山药 又称紫人参 3斤装  蔬菜之王 地中之宝 营养物质更加丰富
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/05/LtRrMJMMAwbR0McwRC50EsQqsRjSSW.jpg
     * price : 26.50
     * groupnum : 2
     * groupsprice : 21.00
     * isindex : 1
     * goodsnum : 1
     * units : 件
     * sales : 60
     * subtitle : 紫淮山含有皂甙、黏液质，有润滑，滋润的作用，故可益肺气，养肺阴，治疗肺虚痰嗽久咳之症。
     */

    private String id;
    private String title;
    private String thumb;
    private String price;
    private String groupnum;
    private String groupsprice;
    private String isindex;
    private String goodsnum;
    private String units;
    private String sales;
    private String subtitle;

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

    public String getGroupnum() {
        return groupnum;
    }

    public void setGroupnum(String groupnum) {
        this.groupnum = groupnum;
    }

    public String getGroupsprice() {
        return groupsprice;
    }

    public void setGroupsprice(String groupsprice) {
        this.groupsprice = groupsprice;
    }

    public String getIsindex() {
        return isindex;
    }

    public void setIsindex(String isindex) {
        this.isindex = isindex;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
