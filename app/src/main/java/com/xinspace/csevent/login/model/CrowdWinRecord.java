package com.xinspace.csevent.login.model;

import java.io.Serializable;

/**
 * Created by Android on 2016/10/18.
 */
public class CrowdWinRecord implements Serializable{

    private String id;
    private String name;

    private String aid;
    private String noactivity;

    private String price;
    private String confirmation_time;

    private String wintime;
    private String confirm;

    private String icmid;
    private String number;

    private String start;
    private String number_participation;
    private String showimg;

    private String dname;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
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

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getConfirmation_time() {
        return confirmation_time;
    }

    public void setConfirmation_time(String confirmation_time) {
        this.confirmation_time = confirmation_time;
    }

    public String getWintime() {
        return wintime;
    }

    public void setWintime(String wintime) {
        this.wintime = wintime;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getIcmid() {
        return icmid;
    }

    public void setIcmid(String icmid) {
        this.icmid = icmid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getNumber_participation() {
        return number_participation;
    }

    public void setNumber_participation(String number_participation) {
        this.number_participation = number_participation;
    }

    public String getShowimg() {
        return showimg;
    }

    public void setShowimg(String showimg) {
        this.showimg = showimg;
    }
}
