package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/4/12.
 */

public class ImageUrlBean implements Serializable{

    private String name;
    private String size;
    private String url;
    private String thumbnailUrl;
    private String deleteType;

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

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }
}
