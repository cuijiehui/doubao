package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 中奖记录列表实体类
 */

public class AwardsRecordEntity implements Serializable{
    private String aid;
    private String pname;//活动名称
    private String start;//发货状态  0(未发货) 1(已发货)
    private String count;//中奖人数
    private String id;
    private String type;
    private String name;
    private String startdate;
    private String image;
    private String price;//商品价格
    private String user_confirm;//用户是否确地址(0,未确认/1,已确认)
    private String noactivity;
    private String confirm;//用户是否确认送单(0,未确认/1,已确认)
    private String match;
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public AwardsRecordEntity(String aid, String pname, String start, String count, String id, String type, String name, String startdate, String image, String price, String user_confirm) {
        this.aid = aid;
        this.pname = pname;
        this.start = start;
        this.count = count;
        this.id = id;
        this.type = type;
        this.name = name;
        this.startdate = startdate;
        this.image = image;
        this.price = price;
        this.user_confirm = user_confirm;
    }


    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }


    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }


    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    public String getUser_confirm() {
        return user_confirm;
    }

    public void setUser_confirm(String user_confirm) {
        this.user_confirm = user_confirm;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AwardsRecordEntity{" +
                "aid='" + aid + '\'' +
                ", pname='" + pname + '\'' +
                ", start='" + start + '\'' +
                ", count='" + count + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", startdate='" + startdate + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
