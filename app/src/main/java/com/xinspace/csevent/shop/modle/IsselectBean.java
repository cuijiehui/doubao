package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/15.
 */

public class IsselectBean implements Serializable{


    /**
     * id : 4
     * title : 测试
     * total : 2
     * apply : 0
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/06/Npoo1Pj2PPPoqu13xhDX73dDCpofT3.jpg
     * success :
     * ishot : 0
     * isselect : 1
     */

    private String id;
    private String title;
    private String total;
    private String apply;
    private String thumb;
    private String success;
    private String ishot;
    private String isselect;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getIsselect() {
        return isselect;
    }

    public void setIsselect(String isselect) {
        this.isselect = isselect;
    }
}
