package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 登录实体
 */
public class LoginEntity implements Serializable{
    private String result;
    private String msg;
    private String user_id;
    private String username;
    private String tel;
    private String openid;
    private String cToken;
    private String cUid;
    private String mobile;

    private String com_id;
    private String community_id;
    private String unit_id;
    private String property_id;

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LoginEntity() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getcToken() {
        return cToken;
    }

    public void setcToken(String cToken) {
        this.cToken = cToken;
    }

    public String getcUid() {
        return cUid;
    }

    public void setcUid(String cUid) {
        this.cUid = cUid;
    }
}
