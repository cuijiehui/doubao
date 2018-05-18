package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * Created by Android on 2017/3/19.
 */

public class GoodsEntity implements Serializable {

    private String mode_id;
    private String cid;
    private String sort;
    private String cname;
    private String mode_name;
    private String price;
    private String discount;
    private String allstock;
    private String img;

    public String getMode_id() {
        return mode_id;
    }

    public void setMode_id(String mode_id) {
        this.mode_id = mode_id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getMode_name() {
        return mode_name;
    }

    public void setMode_name(String mode_name) {
        this.mode_name = mode_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAllstock() {
        return allstock;
    }

    public void setAllstock(String allstock) {
        this.allstock = allstock;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
