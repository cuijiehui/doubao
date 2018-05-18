package com.xinspace.csevent.shop.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/6/21.
 */

public class ShopBean implements Serializable{

    private String shopName;
    private String shopId;
    private List<ShopCartBean> list;
    /** 是否处于编辑状态 */
    private boolean isEditing;
    /** 组是否被选中 */
    private boolean isGroupSelected;

    public boolean isGroupSelected() {
        return isGroupSelected;
    }

    public void setGroupSelected(boolean groupSelected) {
        isGroupSelected = groupSelected;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<ShopCartBean> getList() {
        return list;
    }

    public void setList(List<ShopCartBean> list) {
        this.list = list;
    }
}
