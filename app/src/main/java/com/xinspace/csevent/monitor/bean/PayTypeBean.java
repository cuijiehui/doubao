package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/23.
 */

public class PayTypeBean implements Serializable{
    /**
     * id : 10
     * num : 2
     * price : 3.00元/吨
     * sum : 9.00
     * instruct : 2017年四月份水费
     */

    private String id;
    private String num;
    private String price;
    private String sum;
    private String instruct;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }
}
