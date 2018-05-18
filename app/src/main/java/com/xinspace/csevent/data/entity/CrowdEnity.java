package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * Created by Android on 2016/9/23.
 */
public class CrowdEnity implements Serializable{

    private String img;
    private String id;
    private String pid;
    private String participate;
    private String total_person;
    private String name;
    private String discount;
    private String cost_price;
    private String price;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getParticipate() {
        return participate;
    }

    public void setParticipate(String participate) {
        this.participate = participate;
    }

    public String getTotal_person() {
        return total_person;
    }

    public void setTotal_person(String total_person) {
        this.total_person = total_person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
