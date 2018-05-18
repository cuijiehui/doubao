package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 没有地址的奖品插入地址后返回的数据实体
 */
public class ResultForNoAddressAwardEntity implements Serializable{
    private String result;
    private String msg;
    private String id;//插入后的新表的记录id

    public ResultForNoAddressAwardEntity(String result, String msg, String id) {
        this.result = result;
        this.msg = msg;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ResultForNoAddressAwardEntity{" +
                "result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
