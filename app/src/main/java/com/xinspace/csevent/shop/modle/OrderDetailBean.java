package com.xinspace.csevent.shop.modle;

import com.xinspace.csevent.data.entity.GetAddressEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/5/18.
 */

public class OrderDetailBean implements Serializable{

    private OrderDetail orderDetail;

    private GetAddressEntity getAddressEntity;

    private List<OrderMiddleBean> goodsList;

    private ExpressBean expressBean;

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public GetAddressEntity getGetAddressEntity() {
        return getAddressEntity;
    }

    public void setGetAddressEntity(GetAddressEntity getAddressEntity) {
        this.getAddressEntity = getAddressEntity;
    }

    public List<OrderMiddleBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderMiddleBean> goodsList) {
        this.goodsList = goodsList;
    }

    public ExpressBean getExpressBean() {
        return expressBean;
    }

    public void setExpressBean(ExpressBean expressBean) {
        this.expressBean = expressBean;
    }
}
