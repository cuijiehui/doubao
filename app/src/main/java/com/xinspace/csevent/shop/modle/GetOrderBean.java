package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/11.
 */

public class GetOrderBean implements Serializable{

    private String ordersn;
    private String price;

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
