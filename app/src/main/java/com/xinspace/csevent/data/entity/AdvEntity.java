package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 广告实体类
 */
public class AdvEntity implements Serializable{
    private String address;
    private String adname;
    private String adlink;
    private String type;
    public AdvEntity() {
    }

    public AdvEntity(String address, String adname, String adlink, String type) {
        this.address = address;
        this.adname = adname;
        this.adlink = adlink;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public String getAdlink() {
        return adlink;
    }

    public void setAdlink(String adlink) {
        this.adlink = adlink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AdvEntity{" +
                "address='" + address + '\'' +
                ", adname='" + adname + '\'' +
                ", adlink='" + adlink + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
