package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * Created by Android on 2016/9/23.
 */
public class GoodsModle implements Serializable{

    /**
     * {
     * id : 202
     * title : 【两面针】爱牙牙儿童牙膏 哈密瓜香型 40g 益齿钙+木糖醇 呵护儿童敏感口腔
     * thumb : http://shop.coresun.net/attachment/images/1/2017/04/E5xJ125510VN1Vx239n3odn2N0R7j8.jpg
     * marketprice : 8.80
     * productprice : 15.60
     * minprice : 8.80
     * maxprice : 8.80
     * isdiscount : 0
     * isdiscount_time : 1492691400
     * isdiscount_discounts : {"type":0,"default":{"option0":""}}
     * sales : 86
     * salesreal : 1
     * total : 699
     * description :
     * bargain : 0
     * type : 1
     * ispresell : 0
     * hasoption : 0
     * }
     */

    private String id;
    private String title;
    private String thumb;
    private String marketprice;
    private String productprice;
    private String minprice;
    private String maxprice;
    private String isdiscount;
    private String isdiscount_time;
    private String isdiscount_discounts;
    private String sales;
    private String salesreal;
    private String total;
    private String description;
    private String bargain;
    private String type;
    private String ispresell;
    private String hasoption;

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

    public String getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(String marketprice) {
        this.marketprice = marketprice;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getMinprice() {
        return minprice;
    }

    public void setMinprice(String minprice) {
        this.minprice = minprice;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public String getIsdiscount() {
        return isdiscount;
    }

    public void setIsdiscount(String isdiscount) {
        this.isdiscount = isdiscount;
    }

    public String getIsdiscount_time() {
        return isdiscount_time;
    }

    public void setIsdiscount_time(String isdiscount_time) {
        this.isdiscount_time = isdiscount_time;
    }

    public String getIsdiscount_discounts() {
        return isdiscount_discounts;
    }

    public void setIsdiscount_discounts(String isdiscount_discounts) {
        this.isdiscount_discounts = isdiscount_discounts;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getSalesreal() {
        return salesreal;
    }

    public void setSalesreal(String salesreal) {
        this.salesreal = salesreal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBargain() {
        return bargain;
    }

    public void setBargain(String bargain) {
        this.bargain = bargain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIspresell() {
        return ispresell;
    }

    public void setIspresell(String ispresell) {
        this.ispresell = ispresell;
    }

    public String getHasoption() {
        return hasoption;
    }

    public void setHasoption(String hasoption) {
        this.hasoption = hasoption;
    }
}
