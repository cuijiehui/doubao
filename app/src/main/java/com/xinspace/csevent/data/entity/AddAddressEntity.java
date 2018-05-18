package com.xinspace.csevent.data.entity;

/**
 * Created by lizhihong on 2015/11/27.
 * 此类为添加地址实体类
 */
public class AddAddressEntity {
    private String result;
    private String msg;

    public AddAddressEntity() {
    }

    public AddAddressEntity(String msg, String result) {
        this.msg = msg;
        this.result = result;
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

    @Override
    public String toString() {
        return "AddAddressEntity{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
