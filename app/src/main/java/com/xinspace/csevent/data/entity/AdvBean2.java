package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 广告实体类
 */
public class AdvBean2 implements Serializable{
    private String id;
    private String pic;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
