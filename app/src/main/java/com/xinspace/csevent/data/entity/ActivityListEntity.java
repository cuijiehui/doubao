package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 活动列表信息实体类
 */
public class ActivityListEntity implements Serializable{
    private int id;
    private String name;
    private String image;
    private int state;
    private String special;
    private String abbreviation;//简要描述
    private String time;//剩余时间
    private String winners;//中奖人数
    private String surplus_prize;//剩余奖品数量
    private int is_top;//是否热门活动 0普通 1热门
    private String type;//活动类型,1:普通抽奖,4:游戏抽奖

    public ActivityListEntity() {
    }

    public ActivityListEntity(int id, String name, String image, int state, String special, String abbreviation, String time, String winners, String surplus_prize, int is_top, String type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.state = state;
        this.special = special;
        this.abbreviation = abbreviation;
        this.time = time;
        this.winners = winners;
        this.surplus_prize = surplus_prize;
        this.is_top = is_top;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWinners() {
        return winners;
    }

    public void setWinners(String winners) {
        this.winners = winners;
    }

    public String getSurplus_prize() {
        return surplus_prize;
    }

    public void setSurplus_prize(String surplus_prize) {
        this.surplus_prize = surplus_prize;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ActivityListEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", state=" + state +
                ", special='" + special + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", time='" + time + '\'' +
                ", winners='" + winners + '\'' +
                ", surplus_prize='" + surplus_prize + '\'' +
                ", is_top=" + is_top +
                ", type='" + type + '\'' +
                '}';
    }
}
