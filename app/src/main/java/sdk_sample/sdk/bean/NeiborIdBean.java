package sdk_sample.sdk.bean;

/**
 * Created by Android on 2017/3/23.
 */

import java.io.Serializable;

public class NeiborIdBean implements Serializable {
    private static final long serialVersionUID = 1243851838L;
    private String fip;
    private String fport;
    private String fneibname;
    private String faddress;
    private String department_id;
    private String fremark;
    private String neighborhoods_id;

    public NeiborIdBean() {
    }

    public String getFip() {
        return this.fip;
    }

    public void setFip(String fip) {
        this.fip = fip;
    }

    public String getFport() {
        return this.fport;
    }

    public void setFport(String fport) {
        this.fport = fport;
    }

    public String getFneibname() {
        return this.fneibname;
    }

    public void setFneibname(String fneibname) {
        this.fneibname = fneibname;
    }

    public String getFaddress() {
        return this.faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    public String getDepartment_id() {
        return this.department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getFremark() {
        return this.fremark;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getNeighborhoods_id() {
        return this.neighborhoods_id;
    }

    public void setNeighborhoods_id(String neighborhoods_id) {
        this.neighborhoods_id = neighborhoods_id;
    }

    public String toString() {
        return "Neibor_id [fneibname = " + this.fneibname + ", faddress = " + this.faddress + ", department_id = " + this.department_id + ", fremark = " + this.fremark + ", neighborhoods_id = " + this.neighborhoods_id + "]";
    }
}

