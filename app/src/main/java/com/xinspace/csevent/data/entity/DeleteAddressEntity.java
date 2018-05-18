package com.xinspace.csevent.data.entity;

/**
 * Created by Administrator on 2015/11/27.
 * 删除地址实体类
 */
public class DeleteAddressEntity {
    private String result;
    private String msg;

    public DeleteAddressEntity() {
    }

    public DeleteAddressEntity(String result, String msg) {
        this.result = result;
        this.msg = msg;
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
        return "DeleteAddressEntity{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
