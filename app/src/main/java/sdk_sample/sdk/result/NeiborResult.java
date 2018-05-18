package sdk_sample.sdk.result;


import sdk_sample.sdk.bean.NeiborBean;

/**
 * Created by Android on 2017/3/23.
 */

public class NeiborResult extends BaseResult {
    private NeiborBean result;

    public NeiborResult() {
    }

    public NeiborBean getResult() {
        return this.result;
    }

    public void setResult(NeiborBean result) {
        this.result = result;
    }

    public String toString() {
        return "NeiborResult [result=" + this.result + "]";
    }
}
