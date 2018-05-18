package com.xinspace.csevent.sweepstake.modle;

import java.io.Serializable;

/**
 * Created by Android on 2016/10/11.
 */
public class OrderNumBean implements Serializable{

    private String order_number;
    private String total_amount;
    private String typeId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

}
