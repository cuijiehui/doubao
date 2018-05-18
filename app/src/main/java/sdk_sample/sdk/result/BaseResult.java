package sdk_sample.sdk.result;

import java.io.Serializable;

/**
 * Created by Android on 2017/3/23.
 */

public class BaseResult implements Serializable {
    private static final long serialVersionUID = 6818693284083308012L;
    private int code;
    private String msg;
    private LoginSipBean loginSipBean;

    public LoginSipBean getLoginSipBean() {
        return loginSipBean;
    }

    public void setLoginSipBean(LoginSipBean loginSipBean) {
        this.loginSipBean = loginSipBean;
    }

    public BaseResult() {
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "BaseResult [code=" + this.code + ", msg=" + this.msg + "]";
    }
}
