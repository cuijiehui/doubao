package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/7/20.
 */

public class LeaseScreenBean implements Serializable{

    private String city; //当前定位城市，如：广州
    private String key;  //搜索关键字
    private String area; //区域
    private String house_type; //0:不限 1：一室 2、二室 3、三室 4、四室 5、四室以上
    private String orientations; //朝向 如：南北
    private String minprice; //	房租下边界
    private String maxprice; // 房租上边界
    private String rent_type; //	出租方式
    private String charge_pay; // 押付方式

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getOrientations() {
        return orientations;
    }

    public void setOrientations(String orientations) {
        this.orientations = orientations;
    }

    public String getMinprice() {
        return minprice;
    }

    public void setMinprice(String minprice) {
        this.minprice = minprice;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public String getRent_type() {
        return rent_type;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }

    public String getCharge_pay() {
        return charge_pay;
    }

    public void setCharge_pay(String charge_pay) {
        this.charge_pay = charge_pay;
    }
}
