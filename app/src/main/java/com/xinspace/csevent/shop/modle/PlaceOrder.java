package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/4/14.
 */

public class PlaceOrder implements Serializable{

    private String allprice;
    private String provice;
    private String city;
    private String area;
    private String address;
    private String commodityid;
    private String branch;
    private String commodityname;

    public String getAllprice() {
        return allprice;
    }

    public void setAllprice(String allprice) {
        this.allprice = allprice;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommodityid() {
        return commodityid;
    }

    public void setCommodityid(String commodityid) {
        this.commodityid = commodityid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommodityname() {
        return commodityname;
    }

    public void setCommodityname(String commodityname) {
        this.commodityname = commodityname;
    }
}
