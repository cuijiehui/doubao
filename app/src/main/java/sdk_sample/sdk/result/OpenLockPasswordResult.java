package sdk_sample.sdk.result;


import sdk_sample.sdk.bean.OpenLockPasswordBean;

/**
 * Created by Android on 2017/3/23.
 */

public class OpenLockPasswordResult extends BaseResult {
    private OpenLockPasswordBean result;
    private String msg;
    private int code;

    public OpenLockPasswordResult() {
    }

    public OpenLockPasswordBean getResult() {
        return this.result;
    }

    public void setResult(OpenLockPasswordBean result) {
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
        return "OpenLockPasswordResult [result=" + this.result + ", msg=" + this.msg + ", code=" + this.code + "]";
    }
}
