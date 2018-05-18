package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 广告实体类
 */
public class FullPageAdsEntity implements Serializable{
    private String address;//图片地址
    private String id;
    private String adlink;//广告地址
    private String is_order;//是否排序

    public FullPageAdsEntity() {
    }

    public FullPageAdsEntity(String address, String id, String adlink, String is_order) {
        this.address = address;
        this.id = id;
        this.adlink = adlink;
        this.is_order = is_order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdlink() {
        return adlink;
    }

    public void setAdlink(String adlink) {
        this.adlink = adlink;
    }

    public String getIs_order() {
        return is_order;
    }

    public void setIs_order(String is_order) {
        this.is_order = is_order;
    }

    @Override
    public String toString() {
        return "FullPageAdsEntity{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", adlink='" + adlink + '\'' +
                ", is_order='" + is_order + '\'' +
                '}';
    }
}
