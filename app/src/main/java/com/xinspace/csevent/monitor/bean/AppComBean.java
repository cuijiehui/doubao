package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/23.
 */

public class AppComBean implements Serializable{


    /**
     * id : 1
     * community_id : 17
     * status : 2
     * property : 崔的办公室
     * unit : 寰城海航广场销售部
     * community : 晟中社区体验区
     */

    private String id;
    private String community_id;
    private String status;
    private String property;
    private String unit;
    private String community;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }
}
