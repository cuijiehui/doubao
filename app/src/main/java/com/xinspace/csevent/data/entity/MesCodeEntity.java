package com.xinspace.csevent.data.entity;

/**
 * 验证码实体类
 */
public class MesCodeEntity {

    /**
     * msg : 手机号已注册
     * result : 202
     */

    private String message;
    private String result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
