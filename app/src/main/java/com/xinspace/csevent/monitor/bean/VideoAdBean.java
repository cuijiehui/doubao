package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/7/12.
 */

public class VideoAdBean implements Serializable{

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
