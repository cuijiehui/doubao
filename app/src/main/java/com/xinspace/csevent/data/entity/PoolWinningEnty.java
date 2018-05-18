package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 抽奖池抽奖中奖信息实体
 */
public class PoolWinningEnty implements Serializable{
    private String fid;//活动id
    private String id;
    private String registration_id;//中奖记录id
    private String is_registration;//是否记录在 派送单中
    private String delivery_order_id;//派送单id
    private String pnum;//中奖数量
    private String name;//奖品名称
    private String probability;
    private String pid;//奖品id
    private String type;//类型 gold 金币， integral积分，product产品
    private String cname;//奖品名称

    public PoolWinningEnty() {
    }

    public PoolWinningEnty(String fid, String id, String registration_id, String is_registration, String delivery_order_id, String pnum, String name, String probability, String pid, String type, String cname) {
        this.fid = fid;
        this.id = id;
        this.registration_id = registration_id;
        this.is_registration = is_registration;
        this.delivery_order_id = delivery_order_id;
        this.pnum = pnum;
        this.name = name;
        this.probability = probability;
        this.pid = pid;
        this.type = type;
        this.cname = cname;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getIs_registration() {
        return is_registration;
    }

    public void setIs_registration(String is_registration) {
        this.is_registration = is_registration;
    }

    public String getDelivery_order_id() {
        return delivery_order_id;
    }

    public void setDelivery_order_id(String delivery_order_id) {
        this.delivery_order_id = delivery_order_id;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "PoolWinningEnty{" +
                "fid='" + fid + '\'' +
                ", id='" + id + '\'' +
                ", registration_id='" + registration_id + '\'' +
                ", is_registration='" + is_registration + '\'' +
                ", delivery_order_id='" + delivery_order_id + '\'' +
                ", pnum='" + pnum + '\'' +
                ", name='" + name + '\'' +
                ", probability='" + probability + '\'' +
                ", pid='" + pid + '\'' +
                ", type='" + type + '\'' +
                ", cname='" + cname + '\'' +
                '}';
    }
}
