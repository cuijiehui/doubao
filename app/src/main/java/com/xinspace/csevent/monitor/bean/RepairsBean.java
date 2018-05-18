package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/22.
 */

public class RepairsBean implements Serializable{

    /**
     * id : 6
     * name : 333333
     * phone : 18810199893
     * describe : 啦啦啦啦啦
     * pics :
     * create_time : 2017-05-22 16:01:36
     * uid : 19
     * status : 1
     * type : 2
     * suggestion : null
     * date : null
     * reject : null
     */

    private String id;
    private String name;
    private String phone;
    private String describe;
    private String pics;
    private String create_time;
    private String uid;
    private String status;
    private String type;
    private Object suggestion;
    private Object date;
    private Object reject;

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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Object suggestion) {
        this.suggestion = suggestion;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getReject() {
        return reject;
    }

    public void setReject(Object reject) {
        this.reject = reject;
    }
}
