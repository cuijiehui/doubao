package com.xinspace.csevent.data.entity;

/**
 * Created by lizhihong on 2015/11/28.
 */
public class EditAddressEntity {
    private String result;
    private String msg;

    public EditAddressEntity() {
    }

    public EditAddressEntity(String result, String msg) {
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
        return "EditAddressEntity{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
