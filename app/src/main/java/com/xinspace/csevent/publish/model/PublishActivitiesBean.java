package com.xinspace.csevent.publish.model;

import java.io.Serializable;

/**
 * Created by Android on 2016/10/21.
 */
public class PublishActivitiesBean implements Serializable{

    private String noactivity;
    private String wintime;
    private String lottery_number;

    private String uid;
    private String uname;
    private String match;

    private String username;
    private String user_img;

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }
}
