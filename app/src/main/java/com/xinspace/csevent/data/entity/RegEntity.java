package com.xinspace.csevent.data.entity;

/**
 * 注册实体类
 */
public class RegEntity {
    private String msg;
    private String user_id;
    private String integral;

    public RegEntity() {
    }
    public RegEntity(String msg,String user_id,String integral) {
        this.msg=msg;
        this.user_id=user_id;
        this.integral=integral;
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

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    @Override
    public String toString() {
        return "RegEntity{" +
                "msg='" + msg + '\'' +
                ", user_id='" + user_id + '\'' +
                ", integral='" + integral + '\'' +
                '}';
    }
}
