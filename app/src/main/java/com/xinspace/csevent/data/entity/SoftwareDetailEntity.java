package com.xinspace.csevent.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 发现页面的"推荐软件详细信息"实体
 */
public class SoftwareDetailEntity implements Serializable{
    private String id;//活动ID
    private String icon;//广告图片
    private String interaction;//广告链接
    private String remark;//发现备注
    private String integral;//奖励积分
    private String name;//广告名称
    private String date;//发布日期
    private String size;//大小
    private String version;//版本
    private List<String> img_url;//原图
    private List<String> img_thumbnailurl;//缩略图

    public SoftwareDetailEntity() {

    }

    public SoftwareDetailEntity(String id, String icon, String interaction, String remark, String integral, String name, String date, String size, String version, List<String> img_url, List<String> img_thumbnailurl) {
        this.id = id;
        this.icon = icon;
        this.interaction = interaction;
        this.remark = remark;
        this.integral = integral;
        this.name = name;
        this.date = date;
        this.size = size;
        this.version = version;
        this.img_url = img_url;
        this.img_thumbnailurl = img_thumbnailurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInteraction() {
        return interaction;
    }

    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }

    public List<String> getImg_thumbnailurl() {
        return img_thumbnailurl;
    }

    public void setImg_thumbnailurl(List<String> img_thumbnailurl) {
        this.img_thumbnailurl = img_thumbnailurl;
    }

    @Override
    public String toString() {
        return "SoftwareDetailEntity{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", interaction='" + interaction + '\'' +
                ", remark='" + remark + '\'' +
                ", integral='" + integral + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", size='" + size + '\'' +
                ", version='" + version + '\'' +
                ", img_url=" + img_url +
                ", img_thumbnailurl=" + img_thumbnailurl +
                '}';
    }
}
