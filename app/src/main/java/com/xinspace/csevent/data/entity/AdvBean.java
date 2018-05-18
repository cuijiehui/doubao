package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 广告实体类
 */
public class AdvBean implements Serializable{
    private String name;
    private String size;
    private String url;
    private String thumbnailUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
