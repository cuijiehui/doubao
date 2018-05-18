package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 独家页面的商品分类列表,用于排序的
 */
public class ActivityClassEntity implements Serializable{
    private String id;//分类id
    private String name;//分类名称

    public ActivityClassEntity() {
    }

    public ActivityClassEntity(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "ActivityClassEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
