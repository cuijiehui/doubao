package com.xinspace.csevent.shop.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/6/27.
 */

public class ChildBean implements Serializable{

    private String id;
    private List<ChildGoodsBean> childBeanList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChildGoodsBean> getChildBeanList() {
        return childBeanList;
    }

    public void setChildBeanList(List<ChildGoodsBean> childBeanList) {
        this.childBeanList = childBeanList;
    }
}
