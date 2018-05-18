package sdk_sample.sdk.result;


import sdk_sample.sdk.bean.CanBidingBean;

/**
 * Created by Android on 2017/3/23.
 */

public class CanBidingResult extends BaseResult {
    private CanBidingBean result;

    public CanBidingResult() {
    }

    public CanBidingBean getResult() {
        return this.result;
    }

    public void setResult(CanBidingBean result) {
        this.result = result;
    }

    public String toString() {
        return "CanBidingResult [result=" + this.result + "]";
    }
}
