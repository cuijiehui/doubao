package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/7/18.
 */

public class LeaseRoomBean implements Serializable{


    /**
     * id : 1
     * title : 宏正地产推荐上城国际精装中高层楼盘
     * pic : http://shop.coresun.net/attachment/images/1/2017/07/GNeUt23BuTZp02ocB2p912b3uTJ91u.jpg
     * address : 天河北路寰城海航广场27楼
     * house_type : 1室
     * rental : 1200
     * acreage : 50
     * orientations : 南北向
     * decoration : 精装
     * charge_pay : 押一付三
     * mobile : 15071349024
     * contact : 吴女士
     */

    private String id;
    private String title;
    private String pic;
    private String address;
    private String house_type;
    private String rental;
    private String acreage;
    private String orientations;
    private String decoration;
    private String charge_pay;
    private String mobile;
    private String contact;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getRental() {
        return rental;
    }

    public void setRental(String rental) {
        this.rental = rental;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getOrientations() {
        return orientations;
    }

    public void setOrientations(String orientations) {
        this.orientations = orientations;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getCharge_pay() {
        return charge_pay;
    }

    public void setCharge_pay(String charge_pay) {
        this.charge_pay = charge_pay;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
