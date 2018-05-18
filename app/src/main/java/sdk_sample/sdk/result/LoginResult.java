package sdk_sample.sdk.result;


import sdk_sample.sdk.bean.LoginBean;

/**
 * Created by Android on 2017/3/23.
 */

public class LoginResult extends BaseResult {
    private LoginBean result;
    private String msg;
    private int code;

    public LoginResult() {
    }

    public LoginBean getResult() {
        return this.result;
    }

    public void setResult(LoginBean result) {
        this.result = result;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        return "LoginResult [result=" + this.result + ", msg=" + this.msg + ", code=" + this.code + "]";
    }
}
