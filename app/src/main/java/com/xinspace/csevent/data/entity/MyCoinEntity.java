package com.xinspace.csevent.data.entity;

/**
 * 金币兑换积分比率实体类
 */
public class MyCoinEntity {
    private String integral;//积分
    private String currency;//金币

    public MyCoinEntity(String integral, String currency) {
        this.integral = integral;
        this.currency = currency;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "MyCoinEntity{" +
                "integral='" + integral + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
