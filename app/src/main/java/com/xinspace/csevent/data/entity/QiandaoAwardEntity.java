package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 签到获取的奖励的实体
 *
 */
public class QiandaoAwardEntity implements Serializable{
    private String type;//奖品类型 ，gold（金币），integral（积分），award_count（抽奖次数），commodity（商品）
    private String num;//数量
    private String radius;//  0金币 1积分  2抽奖次数 3商品
    private String name;//奖品名称

    public QiandaoAwardEntity(String type, String num, String radius, String name) {
        this.type = type;
        this.num = num;
        this.radius = radius;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "QiandaoAwardEntity{" +
                "type='" + type + '\'' +
                ", num='" + num + '\'' +
                ", radius='" + radius + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
