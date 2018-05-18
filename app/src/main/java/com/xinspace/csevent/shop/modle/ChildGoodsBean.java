package com.xinspace.csevent.shop.modle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/27.
 */

public class ChildGoodsBean implements Serializable{


    /**
     * 1174 : {"level":2}
     * id : 1175
     * uniacid : 1
     * name : 洗发护发
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/04/d9qyTqPc1vRe414PVMZVH4XxR3eJhm.jpg
     * parentid : 1174
     * isrecommand : 0
     * description :
     * displayorder : 1
     * enabled : 1
     * ishome : 0
     * advimg :
     * advurl :
     * level : 2
     */

    private String id;
    private String uniacid;
    private String name;
    private String thumb;
    private String parentid;
    private String isrecommand;
    private String description;
    private String displayorder;
    private String enabled;
    private String ishome;
    private String advimg;
    private String advurl;
    private String level;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniacid() {
        return uniacid;
    }

    public void setUniacid(String uniacid) {
        this.uniacid = uniacid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getIsrecommand() {
        return isrecommand;
    }

    public void setIsrecommand(String isrecommand) {
        this.isrecommand = isrecommand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(String displayorder) {
        this.displayorder = displayorder;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getIshome() {
        return ishome;
    }

    public void setIshome(String ishome) {
        this.ishome = ishome;
    }

    public String getAdvimg() {
        return advimg;
    }

    public void setAdvimg(String advimg) {
        this.advimg = advimg;
    }

    public String getAdvurl() {
        return advurl;
    }

    public void setAdvurl(String advurl) {
        this.advurl = advurl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
