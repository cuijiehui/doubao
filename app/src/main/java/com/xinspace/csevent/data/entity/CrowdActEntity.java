package com.xinspace.csevent.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 抽奖活动的活动信息
 */
public class CrowdActEntity implements Serializable{

    private String name;
    private String price;
    private String pid;
    private String total_person;
    private String id;
    private String consume;
    private String abbreviation;
    private String noactivity;
    private String remark;
    private String Already_participate;
    private List<String> imgList;
    private String img;
    private String type;
    private String surplus_prize;

    private String cost_price;
    private String discount;

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTotal_person() {
        return total_person;
    }

    public void setTotal_person(String total_person) {
        this.total_person = total_person;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAlready_participate() {
        return Already_participate;
    }

    public void setAlready_participate(String already_participate) {
        Already_participate = already_participate;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSurplus_prize() {
        return surplus_prize;
    }

    public void setSurplus_prize(String surplus_prize) {
        this.surplus_prize = surplus_prize;
    }
}
