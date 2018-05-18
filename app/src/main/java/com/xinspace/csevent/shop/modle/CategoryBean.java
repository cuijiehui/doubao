package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/8.
 */

public class CategoryBean implements Serializable{

    /**
     * id : 6
     * name : 口腔护理
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/05/Gs9t18SsP78rY51211lAA91jZdjr2S.jpg
     */

    private String id;
    private String name;
    private String thumb;

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
