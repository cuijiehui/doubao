package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 发现页面的"推荐软件"实体
 */
public class SoftwareEntity implements Serializable{
    private String id;//活动ID
    private String icon;//广告图片
    private String interaction;//广告链接
    private String remark;//发现备注
    private String integral;//奖励积分
    private String name;//广告名称


    public SoftwareEntity() {
    }

    public SoftwareEntity(String id, String icon, String interaction, String remark, String integral, String name) {
        this.id = id;
        this.icon = icon;
        this.interaction = interaction;
        this.remark = remark;
        this.integral = integral;
        this.name = name;
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
    @Override
    public String toString() {
        return "SoftwareEntity{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", interaction='" + interaction + '\'' +
                ", remark='" + remark + '\'' +
                ", integral='" + integral + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
