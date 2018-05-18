package com.xinspace.csevent.login.model;

import java.io.Serializable;

/**
 * Created by Android on 2016/10/17.
 */
public class CrowdRecord implements Serializable{

    private String id;
    private String commodity_name;
    private String aid;

    private String lucky_number;
    private String match;
    private String noactivity;

    private String iswin;
    private String participate;
    private String username;
    private String number_participation;
    private String winuid;
    private String commodity_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getLucky_number() {
        return lucky_number;
    }

    public void setLucky_number(String lucky_number) {
        this.lucky_number = lucky_number;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    public String getIswin() {
        return iswin;
    }

    public void setIswin(String iswin) {
        this.iswin = iswin;
    }

    public String getParticipate() {
        return participate;
    }

    public void setParticipate(String participate) {
        this.participate = participate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber_participation() {
        return number_participation;
    }

    public void setNumber_participation(String number_participation) {
        this.number_participation = number_participation;
    }

    public String getWinuid() {
        return winuid;
    }

    public void setWinuid(String winuid) {
        this.winuid = winuid;
    }

    public String getCommodity_img() {
        return commodity_img;
    }

    public void setCommodity_img(String commodity_img) {
        this.commodity_img = commodity_img;
    }
}
