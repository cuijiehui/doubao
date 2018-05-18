package sdk_sample.sdk.bean;

/**
 * Created by Android on 2017/3/23.
 */

import java.io.Serializable;

public class CanBidingBean implements Serializable {
    private boolean can_binding;
    private NeiborIdBean neibor_id;

    public CanBidingBean() {
    }

    public boolean getCan_binding() {
        return this.can_binding;
    }

    public NeiborIdBean getNeibor_id() {
        return this.neibor_id;
    }

    public void setNeibor_id(NeiborIdBean neibor_id) {
        this.neibor_id = neibor_id;
    }

    public void setCan_binding(boolean can_binding) {
        this.can_binding = can_binding;
    }

    public String toString() {
        return "CanbindBean [neibor_id = " + this.neibor_id + ", can_binding = " + this.can_binding + "]";
    }
}
