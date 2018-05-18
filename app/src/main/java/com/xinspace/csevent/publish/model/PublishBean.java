package com.xinspace.csevent.publish.model;

import java.io.Serializable;

/**
 * Created by Android on 2016/10/19.
 */
public class PublishBean implements Serializable{

    private String pid;
    private String activeid;
    private String t_name;
    private String uid;
    private String nickname;
    private String lottery_num;
    private String name;
    private String total_person;
    private String noactivity;
    private String number_participation;
    private String id;
    private String wintime;
    private String lottery_number;
    private String number_min;
    private String number_max;
    private String username;
    private String thumbnail;
    private String user_thumbnail;

    private long downTime;

    private long chaTime;

    public long getChaTime() {
        return chaTime;
    }

    public void setChaTime(long chaTime) {
        this.chaTime = chaTime;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getActiveid() {
        return activeid;
    }

    public void setActiveid(String activeid) {
        this.activeid = activeid;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLottery_num() {
        return lottery_num;
    }

    public void setLottery_num(String lottery_num) {
        this.lottery_num = lottery_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_person() {
        return total_person;
    }

    public void setTotal_person(String total_person) {
        this.total_person = total_person;
    }

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    public String getNumber_participation() {
        return number_participation;
    }

    public void setNumber_participation(String number_participation) {
        this.number_participation = number_participation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWintime() {
        return wintime;
    }

    public void setWintime(String wintime) {
        this.wintime = wintime;
    }

    public String getLottery_number() {
        return lottery_number;
    }

    public void setLottery_number(String lottery_number) {
        this.lottery_number = lottery_number;
    }

    public String getNumber_min() {
        return number_min;
    }

    public void setNumber_min(String number_min) {
        this.number_min = number_min;
    }

    public String getNumber_max() {
        return number_max;
    }

    public void setNumber_max(String number_max) {
        this.number_max = number_max;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUser_thumbnail() {
        return user_thumbnail;
    }

    public void setUser_thumbnail(String user_thumbnail) {
        this.user_thumbnail = user_thumbnail;
    }
}
