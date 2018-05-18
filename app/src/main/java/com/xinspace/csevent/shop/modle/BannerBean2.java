package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/4/24.
 */

public class BannerBean2 implements Serializable{


    /**
     * id : 3
     * advname : 测试广告1
     * link : http://www.laf.cn/
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/06/Z5EnpExXzGuEGzRunUo5eeb6BrX2pi.png
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
