package com.xinspace.csevent.publish.model;

import java.io.Serializable;

/**
 * Created by Android on 2016/10/21.
 */
public class PartRecordBean implements Serializable{


    private String noactivity;
    private String uid;

    private String nickname;
    private String participate;
    private String t_name;
    private String match;

    private String username;
    private String user_img;

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
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

    public String getParticipate() {
        return participate;
    }

    public void setParticipate(String participate) {
        this.participate = participate;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
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
