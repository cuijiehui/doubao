package sdk_sample.sdk.bean;

/**
 * Created by Android on 2017/3/23.
 */

import java.io.Serializable;

public class CommunityBean implements Serializable {
    private int fport;
    private int fuser_id;
    private String fip;
    private String fneib_name;
    private String id;
    private String ffs_ip;
    private int ffs_port;

    public CommunityBean() {
    }

    public int getFport() {
        return this.fport;
    }

    public void setFport(int fport) {
        this.fport = fport;
    }

    public int getFuser_id() {
        return this.fuser_id;
    }

    public void setFuser_id(int fuser_id) {
        this.fuser_id = fuser_id;
    }

    public String getFip() {
        return this.fip;
    }

    public void setFip(String fip) {
        this.fip = fip;
    }

    public String getFneib_name() {
        return this.fneib_name;
    }

    public void setFneib_name(String fneib_name) {
        this.fneib_name = fneib_name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFfs_ip() {
        return this.ffs_ip;
    }

    public void setFfs_ip(String ffs_ip) {
        this.ffs_ip = ffs_ip;
    }

    public int getFfs_port() {
        return this.ffs_port;
    }

    public void setFfs_port(int ffs_port) {
        this.ffs_port = ffs_port;
    }

    public String toString() {
        return "HouseBean [fport=" + this.fport + ", fuser_id=" + this.fuser_id + ", fip=" + this.fip + ", fneib_name=" + this.fneib_name + ", id=" + this.id + ", ffs_ip=" + this.ffs_ip + ", ffs_port=" + this.ffs_port + "]";
    }
}

