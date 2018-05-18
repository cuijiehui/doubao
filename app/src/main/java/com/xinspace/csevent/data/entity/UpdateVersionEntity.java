package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 软件更新实体类
 */
public class UpdateVersionEntity implements Serializable{


    /**
     * number : 1.0
     * describe : 大撒旦撒
     * url : http://community.coresun.net/public/uploads/apk/20170427/app-release.apk
     * code : 26
     */

    private String number;
    private String describe;
    private String url;
    private String code;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
