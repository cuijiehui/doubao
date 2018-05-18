package com.xinspace.csevent.data.entity;

import java.io.Serializable;

public class PhotoEntity implements Serializable{
    private String name;
    private int size;
    private String type;
    private String url;
    private String deleteUrl;
    private String deleteType;

    public PhotoEntity() {
    }

    public PhotoEntity(String name, int size, String type, String url, String deleteUrl, String deleteType) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.url = url;
        this.deleteUrl = deleteUrl;
        this.deleteType = deleteType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }

    @Override
    public String toString() {
        return "PhotoEntity{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", deleteUrl='" + deleteUrl + '\'' +
                ", deleteType='" + deleteType + '\'' +
                '}';
    }
}
