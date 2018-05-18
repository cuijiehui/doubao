package sdk_sample.sdk.result;


import java.util.List;

import sdk_sample.sdk.bean.ComBean;
import sdk_sample.sdk.bean.LockBean;

/**
 * Created by Android on 2017/3/23.
 */

public class LockListResult extends BaseResult {
    private List<LockBean> lockList;
    private List<ComBean> comList;

    private List<LockBean> lockList2;
    private List<LockBean> lockList3;

    public List<LockBean> getLockList2() {
        return lockList2;
    }

    public void setLockList2(List<LockBean> lockList2) {
        this.lockList2 = lockList2;
    }

    public List<LockBean> getLockList3() {
        return lockList3;
    }

    public void setLockList3(List<LockBean> lockList3) {
        this.lockList3 = lockList3;
    }

    public List<ComBean> getComList() {
        return comList;
    }

    public void setComList(List<ComBean> comList) {
        this.comList = comList;
    }

    public LockListResult() {
    }

    public List<LockBean> getLockList() {
        return this.lockList;
    }

    public void setLockList(List<LockBean> lockList) {
        this.lockList = lockList;
    }



    public String toString() {
        return "LockListResult [lockList=" + this.lockList + "]";
    }
}
