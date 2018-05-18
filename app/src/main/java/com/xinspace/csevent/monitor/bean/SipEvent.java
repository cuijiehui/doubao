package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/4/8.
 */

public class SipEvent implements Serializable{

    String path;
    String token;
    String userName;
    String domain_sn;
    String toSipNumber;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDomain_sn() {
        return domain_sn;
    }

    public void setDomain_sn(String domain_sn) {
        this.domain_sn = domain_sn;
    }

    public String getToSipNumber() {
        return toSipNumber;
    }

    public void setToSipNumber(String toSipNumber) {
        this.toSipNumber = toSipNumber;
    }



}
