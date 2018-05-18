package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 置顶活动实体
 */
public class TopActivityEntity implements Serializable{
    private String state;
    private String endtime;
    private int id;
    private String image;
    private String special;
    private String starttime;
    private String name;

    public TopActivityEntity() {
    }

    public TopActivityEntity(String state, String endtime, int id, String image, String special, String starttime, String name) {
        this.state = state;
        this.endtime = endtime;
        this.id = id;
        this.image = image;
        this.special = special;
        this.starttime = starttime;
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TopActivityEntity{" +
                "state='" + state + '\'' +
                ", endtime='" + endtime + '\'' +
                ", id=" + id +
                ", image='" + image + '\'' +
                ", special='" + special + '\'' +
                ", starttime='" + starttime + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
