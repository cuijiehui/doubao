package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/26.
 */

public class PayRecordBean implements Serializable{

    /**
     * id : 6
     * instruct : 2017年三月份水费
     * sum : 9.00
     * pay_status : 1
     * cate : 水费
     */

    private String id;
    private String instruct;
    private String sum;
    private String pay_status;
    private String cate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
}
