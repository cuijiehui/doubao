package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/6/2.
 */

public class CommunityBean implements Serializable{

    private String id;
    private String name;
    private List<ChildCommunityBean> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildCommunityBean> getList() {
        return list;
    }

    public void setList(List<ChildCommunityBean> list) {
        this.list = list;
    }
}
