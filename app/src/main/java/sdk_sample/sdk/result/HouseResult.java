package sdk_sample.sdk.result;


import java.util.List;

import sdk_sample.sdk.bean.CommunityBean;

/**
 * Created by Android on 2017/3/23.
 */

public class HouseResult extends BaseResult {
    private List<CommunityBean> result;

    public HouseResult() {
    }

    public List<CommunityBean> getResult() {
        return this.result;
    }

    public void setResult(List<CommunityBean> result) {
        this.result = result;
    }

    public String toString() {
        return "HouseResult [result=" + this.result + "]";
    }
}
