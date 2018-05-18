package com.xinspace.csevent.baskorder.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2016/10/27.
 */
public class BaskOrder implements Serializable{

    private String name;
    private String startdate;
    private String match;
    private String noactivity;

    private String id;
    private String sid;
    private String comment;
    private String title;

    private String datetime;
    private String all_match;
    private String username;
    private String img;

    private List<String> smallList;
    private List<String> bigList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAll_match() {
        return all_match;
    }

    public void setAll_match(String all_match) {
        this.all_match = all_match;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<String> getSmallList() {
        return smallList;
    }

    public void setSmallList(List<String> smallList) {
        this.smallList = smallList;
    }

    public List<String> getBigList() {
        return bigList;
    }

    public void setBigList(List<String> bigList) {
        this.bigList = bigList;
    }
}
