package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/3.
 */

public class AdvBanner implements Serializable{


    /**
     * imgurl : https://wx.szshide.shop/attachment/images/1/2017/06/O6JEvzK0Xj6d0eh7Z00el0CShHKcht.jpg
     * linkurl :
     * thumb :
     */

    private String imgurl;
    private String linkurl;
    private String thumb;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
