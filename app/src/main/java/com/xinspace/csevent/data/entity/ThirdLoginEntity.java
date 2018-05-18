package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/3.
 */
public class ThirdLoginEntity implements Serializable{
    private String result;
    private String msg;
    private String user_id;
    private String frist;
    private String integral;

    public ThirdLoginEntity() {
    }

    public ThirdLoginEntity(String result, String msg, String user_id, String frist, String integral) {
        this.result = result;
        this.msg = msg;
        this.user_id = user_id;
        this.frist = frist;
        this.integral = integral;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFrist() {
        return frist;
    }

    public void setFrist(String frist) {
        this.frist = frist;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    @Override
    public String toString() {
        return "ThirdLoginEntity{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", user_id='" + user_id + '\'' +
                ", frist='" + frist + '\'' +
                ", integral='" + integral + '\'' +
                '}';
    }
}
