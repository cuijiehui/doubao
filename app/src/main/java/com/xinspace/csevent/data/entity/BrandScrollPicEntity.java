package com.xinspace.csevent.data.entity;

/**
 * Created by Administrator on 2016/2/27.
 */
public class BrandScrollPicEntity {
    private String img_url;

    public BrandScrollPicEntity() {
    }

    public BrandScrollPicEntity(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "BrandScrollPicEntity{" +
                "img_url='" + img_url + '\'' +
                '}';
    }
}
