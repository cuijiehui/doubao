package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/2.
 */

public class ClassBean implements Serializable{
    /**
     * text : 玩具乐器
     * linkurl : http://shop.coresun.net/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=goods&cate=1192
     * style : background:#ffffff; color:#666666;
     * imgurl :
     * thumb :
     * cate : 1192
     */

    private String text;
    private String linkurl;
    private String style;
    private String imgurl;
    private String thumb;
    private String cate;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
}
