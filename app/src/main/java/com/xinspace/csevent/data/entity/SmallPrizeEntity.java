package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/***
 * 小奖品实体类
 */
public class SmallPrizeEntity implements Serializable{
    private String id;
    private String pname;
    private String cid;
    private String list_image_thumbnail;
    private String list_image;

    public SmallPrizeEntity(String id, String pname, String cid, String list_image_thumbnail, String list_image) {
        this.id = id;
        this.pname = pname;
        this.cid = cid;
        this.list_image_thumbnail = list_image_thumbnail;
        this.list_image = list_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getList_image_thumbnail() {
        return list_image_thumbnail;
    }

    public void setList_image_thumbnail(String list_image_thumbnail) {
        this.list_image_thumbnail = list_image_thumbnail;
    }

    public String getList_image() {
        return list_image;
    }

    public void setList_image(String list_image) {
        this.list_image = list_image;
    }

    @Override
    public String toString() {
        return "SmallPrizeEntity{" +
                "id='" + id + '\'' +
                ", pname='" + pname + '\'' +
                ", cid='" + cid + '\'' +
                ", list_image_thumbnail='" + list_image_thumbnail + '\'' +
                ", list_image='" + list_image + '\'' +
                '}';
    }
}
