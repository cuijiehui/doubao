package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/26.
 */

public class OpenLockBean implements Serializable{


    /**
     * create_time : 2017-06-26 16:11:01
     * id : 18
     * name : 一
     * phone : 13560115020
     */

    private String create_time;
    private String id;
    private String name;
    private String phone;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
