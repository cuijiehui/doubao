package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 充值订单实体
 */
public class ChargeOrderEntity implements Serializable{
    private String order_number;
    private String total_amount;

    public ChargeOrderEntity() {
    }

    public ChargeOrderEntity(String order_number, String total_amount) {
        this.order_number = order_number;
        this.total_amount = total_amount;
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

    @Override
    public String toString() {
        return "ChargeOrderEntity{" +
                "order_number='" + order_number + '\'' +
                ", total_amount='" + total_amount + '\'' +
                '}';
    }
}
