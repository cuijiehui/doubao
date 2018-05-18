package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 奖品的信息实体类
 */
public class AwardInfoEntity implements Serializable{
    private String list_image_thumbnail;//奖品图片缩略图
    private String list_image;//奖品图片
    private int pnum;//奖品数量
    private String cname;//奖品名
    private String name;//等级奖名称

    public AwardInfoEntity() {
    }

    public AwardInfoEntity(String list_image_thumbnail, String list_image, int pnum, String cname, String name) {
        this.list_image_thumbnail = list_image_thumbnail;
        this.list_image = list_image;
        this.pnum = pnum;
        this.cname = cname;
        this.name = name;
    }

    @Override
    public String toString() {
        return "AwardInfoEntity{" +
                "list_image_thumbnail='" + list_image_thumbnail + '\'' +
                ", list_image='" + list_image + '\'' +
                ", pnum=" + pnum +
                ", cname='" + cname + '\'' +
                ", name='" + name + '\'' +
                '}';
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

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
