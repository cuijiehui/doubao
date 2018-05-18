package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/7.
 */

public class SeckillTimeBean implements Serializable{


    /**
     * id : 28
     * uniacid : 1
     * taskid : 8
     * time : 10
     * endtime : 1496818799
     * starttime : 1496800800
     * status : 1
     */

    private String id;
    private String uniacid;
    private String taskid;
    private String time;
    private String endtime;
    private String starttime;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniacid() {
        return uniacid;
    }

    public void setUniacid(String uniacid) {
        this.uniacid = uniacid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
