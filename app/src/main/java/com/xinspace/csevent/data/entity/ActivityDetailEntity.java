package com.xinspace.csevent.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 活动详细信息实体类
 */
public class ActivityDetailEntity implements Serializable {
    private String result;//结果状态码
    private String msg;//提示消息
    //private List<AwardInfoEntity> activity_list;//奖品列表
    private ActivityInfoEntity activity;//该抽奖活动的信息
    private List<String> thumbnailList;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ActivityDetailEntity() {
    }

    public ActivityDetailEntity(String result, String msg, List<String> thumbnailList, ActivityInfoEntity activity , String imgUrl) {
        this.result = result;
        this.msg = msg;
        this.thumbnailList = thumbnailList;
        this.activity = activity;
        this.imgUrl = imgUrl;
    }


    public List<String> getThumbnailList() {
        return thumbnailList;
    }

    public void setThumbnailList(List<String> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public ActivityInfoEntity getActivity() {
        return activity;
    }

    public void setActivity(ActivityInfoEntity activity) {
        this.activity = activity;
    }

}
