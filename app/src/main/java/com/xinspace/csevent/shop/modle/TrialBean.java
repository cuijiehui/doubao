package com.xinspace.csevent.shop.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/6/15.
 */

public class TrialBean implements Serializable{

    /**
     * id : 4
     * title : 测试
     * total : 2
     * apply : 0
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/06/Npoo1Pj2PPPoqu13xhDX73dDCpofT3.jpg
     * success : 0
     */

    private String id;
    private String title;
    private String total;
    private String apply;
    private String thumb;
    private String success;
    private String starttime;
    private String endtime;
    private String price;
    private String status;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private List<RecommendBean> list;

    public List<RecommendBean> getList() {
        return list;
    }

    public void setList(List<RecommendBean> list) {
        this.list = list;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
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
}
