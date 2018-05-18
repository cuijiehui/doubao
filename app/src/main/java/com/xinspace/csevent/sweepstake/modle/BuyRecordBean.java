package com.xinspace.csevent.sweepstake.modle;

import java.io.Serializable;

/**
 * Created by Android on 2016/10/12.
 */
public class BuyRecordBean implements Serializable{

    private String id;
    private String name;
    private String aid;
    private String price;
    private String wintime;
    private String confirm;
    private String icmid;
    private String match;
    private String number;

    private String dname;
    private String start;
    private String showimg;


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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getShowimg() {
        return showimg;
    }

    public void setShowimg(String showimg) {
        this.showimg = showimg;
    }
}
