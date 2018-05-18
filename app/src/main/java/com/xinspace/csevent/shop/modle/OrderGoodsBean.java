package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/9.
 */

public class OrderGoodsBean implements Serializable{


    /**
     * goodsid : 198
     * total : 1
     * optionid : 395
     * marketprice : 11.90
     * cates : 1175
     */

    private String goodsid;
    private String total;
    private String optionid;
    private String marketprice;
    private String cates;

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
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
}
