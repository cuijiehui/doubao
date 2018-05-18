package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/4.
 */


public class ShopCartBean implements Serializable{

    /**
     * id : 210
     * total : 3
     * goodsid : 248
     * stock : 30
     * preselltimeend : 0
     * gpprice : 0.00
     * hasoption : 1
     * optionstock : 30
     * presellprice : 0.00
     * ispresell : 0
     * maxbuy : 0
     * title : 【卡农】KANON AKS系列-复古民谣吉他 38寸 致敬吉他理想时代 卡农让我们与众不同 圆缺角两款可选 赠送配件大礼包
     * thumb : http://shop.coresun.net/attachment/images/1/2017/04/L2N4q1i994889ZQiUU9I4li688Q8J4.jpg
     * marketprice : 440
     * productprice : 760.00
     * optiontitle : 圆角 卡其色
     * optionid : 475
     * specs : 325_328
     * minbuy : 0
     * unit : 件
     * merchid : 0
     * checked : 0
     * isdiscount_discounts : {"type":1,"default":{"option474":"","option475":"","option476":"","option477":"","option478":"","option479":""}}
     * isdiscount : 0
     * isdiscount_time : 1493520480
     * isnodiscount : 0
     * discounts : {"type":"0","default":"","default_pay":""}
     * merchsale : 0
     * selected : 1
     * ggprice : 440
     * totalmaxbuy : 30
     */

    private String id;
    private String total;
    private String goodsid;
    private String stock;
    private String preselltimeend;
    private String gpprice;
    private String hasoption;
    private String optionstock;
    private String presellprice;
    private String ispresell;
    private String maxbuy;
    private String title;
    private String thumb;
    private String marketprice;
    private String productprice;
    private String optiontitle;
    private String optionid;
    private String specs;
    private String minbuy;
    private String unit;
    private String merchid;
    private String checked;
    private String isdiscount;
    private String isdiscount_time;
    private String isnodiscount;
    private String merchsale;
    private String selected;
    private String ggprice;
    private String totalmaxbuy;


    /** 是否是编辑状态 */
    private boolean isEditing;
    /** 是否被选中 */
    private boolean isChildSelected;

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public boolean isChildSelected() {
        return isChildSelected;
    }

    public void setChildSelected(boolean childSelected) {
        isChildSelected = childSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPreselltimeend() {
        return preselltimeend;
    }

    public void setPreselltimeend(String preselltimeend) {
        this.preselltimeend = preselltimeend;
    }

    public String getGpprice() {
        return gpprice;
    }

    public void setGpprice(String gpprice) {
        this.gpprice = gpprice;
    }

    public String getHasoption() {
        return hasoption;
    }

    public void setHasoption(String hasoption) {
        this.hasoption = hasoption;
    }

    public String getOptionstock() {
        return optionstock;
    }

    public void setOptionstock(String optionstock) {
        this.optionstock = optionstock;
    }

    public String getPresellprice() {
        return presellprice;
    }

    public void setPresellprice(String presellprice) {
        this.presellprice = presellprice;
    }

    public String getIspresell() {
        return ispresell;
    }

    public void setIspresell(String ispresell) {
        this.ispresell = ispresell;
    }

    public String getMaxbuy() {
        return maxbuy;
    }

    public void setMaxbuy(String maxbuy) {
        this.maxbuy = maxbuy;
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

    public String getOptiontitle() {
        return optiontitle;
    }

    public void setOptiontitle(String optiontitle) {
        this.optiontitle = optiontitle;
    }

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getMinbuy() {
        return minbuy;
    }

    public void setMinbuy(String minbuy) {
        this.minbuy = minbuy;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMerchid() {
        return merchid;
    }

    public void setMerchid(String merchid) {
        this.merchid = merchid;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
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

    public String getIsnodiscount() {
        return isnodiscount;
    }

    public void setIsnodiscount(String isnodiscount) {
        this.isnodiscount = isnodiscount;
    }

    public String getMerchsale() {
        return merchsale;
    }

    public void setMerchsale(String merchsale) {
        this.merchsale = merchsale;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getGgprice() {
        return ggprice;
    }

    public void setGgprice(String ggprice) {
        this.ggprice = ggprice;
    }

    public String getTotalmaxbuy() {
        return totalmaxbuy;
    }

    public void setTotalmaxbuy(String totalmaxbuy) {
        this.totalmaxbuy = totalmaxbuy;
    }
}
