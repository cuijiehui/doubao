package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * Created by lizhihong on 2015/11/26.
 * 地址实体类
 */
public class GetAddressEntity implements Serializable{


    /**
     * id : 139
     * uniacid : 1
     * openid : wap_user_1_18810199893
     * realname : 王先森
     * mobile : 18810199893
     * province : 广东省
     * city : 广州市
     * area : 白云区
     * address : 该词可口可乐白龙马
     * isdefault : 0
     * zipcode :
     * deleted : 0
     * street :
     * datavalue :
     * streetdatavalue :
     */

    private String id;
    private String uniacid;
    private String openid;
    private String realname;
    private String mobile;
    private String province;
    private String city;
    private String area;
    private String address;
    private String isdefault;
    private String zipcode;
    private String deleted;
    private String street;
    private String datavalue;
    private String streetdatavalue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniacid() {
        return uniacid;
    }

    public void setUniacid(String uniacid) {
        this.uniacid = uniacid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDatavalue() {
        return datavalue;
    }

    public void setDatavalue(String datavalue) {
        this.datavalue = datavalue;
    }

    public String getStreetdatavalue() {
        return streetdatavalue;
    }

    public void setStreetdatavalue(String streetdatavalue) {
        this.streetdatavalue = streetdatavalue;
    }
}

