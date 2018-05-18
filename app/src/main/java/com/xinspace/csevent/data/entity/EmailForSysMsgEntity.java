package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 用户邮箱中系统推送信息的实体类
 */

public class EmailForSysMsgEntity implements Serializable{
    private String id;//反馈ID
    private String tel;//用户手机号
    private String feedback;//反馈内容
    private String reply;//管理员回复
    private String uid;//用户ID
    private String state;//是否读取
    private String date;//时间

    public EmailForSysMsgEntity(String id, String tel, String feedback, String reply, String uid, String state, String date) {
        this.id = id;
        this.tel = tel;
        this.feedback = feedback;
        this.reply = reply;
        this.uid = uid;
        this.state = state;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "EmailForSysMsgEntity{" +
                "id='" + id + '\'' +
                ", tel='" + tel + '\'' +
                ", feedback='" + feedback + '\'' +
                ", reply='" + reply + '\'' +
                ", uid='" + uid + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
