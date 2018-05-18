package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/9.
 */

public class GroupOrderBean implements Serializable{


    /**
     * openid : o0S_71apEecbaHDxTI_Ylp0135Jg
     * id : 12
     * aid : 85
     * type : groups
     * realname : 姚测试
     * mobile : 15913390911
     * teamid : 56
     * message :
     */

    private String openid;
    private String id;
    private String aid;
    private String type;
    private String realname;
    private String mobile;
    private String teamid;
    private String optionid;

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

}
