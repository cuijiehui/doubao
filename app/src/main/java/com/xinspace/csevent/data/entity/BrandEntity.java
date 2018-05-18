package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 品牌图标
 */
public class BrandEntity implements Serializable{
    /**
     * logo : http://www.xinspace.com.cn/sz_web/upload/brand/5/56d036e3d19c4.jpeg
     * name : Givenchy
     */
    private String id;
    private String logo;
    private String name;

    public BrandEntity() {
    }

    public BrandEntity(String id, String logo, String name) {
        this.id = id;
        this.logo = logo;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BrandEntity{" +
                "id='" + id + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
