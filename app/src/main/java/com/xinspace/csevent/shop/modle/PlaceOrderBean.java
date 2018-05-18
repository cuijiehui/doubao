package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/4/14.
 */

public class PlaceOrderBean implements Serializable{

    /**
     * openid : o0S_71apEecbaHDxTI_Ylp0135Jg
     * fromcart : 1
     * addressid : 133
     * goods : [{"goodsid":"198","total":"1","optionid":"395","marketprice":"11.90","cates":"1175"},{"goodsid":"215","total":"1","optionid":"0","marketprice":"30.00","cates":"1187"}]
     */

    private String openid;
    private String fromcart;
    private String addressid;
    private String goods;

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getFromcart() {
        return fromcart;
    }

    public void setFromcart(String fromcart) {
        this.fromcart = fromcart;
    }

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }


}
