package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 获奖的信息实体
 */
public class WinningEnty implements Serializable{
    private String fid;//活动id
    private String name;//奖品名称
    private String cname;//奖品名称
    private String pid;//奖品id
    private String registration_id;//中奖记录id
    private String delivery_order_id;//派送单id
    private String is_registration;//是否记录在 派送单中

    public WinningEnty() {
    }

    public WinningEnty(String fid, String name, String cname, String pid, String registration_id, String delivery_order_id, String is_registration) {
        this.fid = fid;
        this.name = name;
        this.cname = cname;
        this.pid = pid;
        this.registration_id = registration_id;
        this.delivery_order_id = delivery_order_id;
        this.is_registration = is_registration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getDelivery_order_id() {
        return delivery_order_id;
    }

    public void setDelivery_order_id(String delivery_order_id) {
        this.delivery_order_id = delivery_order_id;
    }

    public String getIs_registration() {
        return is_registration;
    }

    public void setIs_registration(String is_registration) {
        this.is_registration = is_registration;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
    @Override
    public String toString() {
        return "WinningEnty{" +
                "fid='" + fid + '\'' +
                ", name='" + name + '\'' +
                ", cname='" + cname + '\'' +
                ", pid='" + pid + '\'' +
                ", registration_id='" + registration_id + '\'' +
                ", delivery_order_id='" + delivery_order_id + '\'' +
                ", is_registration='" + is_registration + '\'' +
                '}';
    }
}
