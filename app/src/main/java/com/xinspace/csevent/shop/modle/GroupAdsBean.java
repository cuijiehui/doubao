package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/8.
 */

public class GroupAdsBean implements Serializable{

    /**
     * id : 3
     * advname : 拼团活动
     * link : ./index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/05/m34Q0dJQmecln4ZQmGLzQqMNquL3QN.jpg
     */

    private String id;
    private String advname;
    private String link;
    private String thumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvname() {
        return advname;
    }

    public void setAdvname(String advname) {
        this.advname = advname;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
