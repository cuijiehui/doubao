package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/20.
 */

public class ExpressBean implements Serializable{

    private String time;
    private String step;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
