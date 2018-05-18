package sdk_sample.sdk.bean;

/**
 * Created by Android on 2017/3/23.
 */

import java.io.Serializable;

public class NeiborBean implements Serializable {
    private int ffs_port;
    private String registrationTimeout;
    private UserBean user_msg;
    private String transport;
    private String fneib_name;
    private String ffs_ip;
    private String fip;
    private String neibor_id;
    private int fport;

    public NeiborBean() {
    }

    public void setFfs_port(int ffs_port) {
        this.ffs_port = ffs_port;
    }

    public void setRegistrationTimeout(String registrationTimeout) {
        this.registrationTimeout = registrationTimeout;
    }

    public void setUser_msg(UserBean user_msg) {
        this.user_msg = user_msg;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public void setFneib_name(String fneib_name) {
        this.fneib_name = fneib_name;
    }

    public void setFfs_ip(String ffs_ip) {
        this.ffs_ip = ffs_ip;
    }

    public void setFip(String fip) {
        this.fip = fip;
    }

    public void setNeibor_id(String neibor_id) {
        this.neibor_id = neibor_id;
    }

    public void setFport(int fport) {
        this.fport = fport;
    }

    public int getFfs_port() {
        return this.ffs_port;
    }

    public String getRegistrationTimeout() {
        return this.registrationTimeout;
    }

    public UserBean getUser_msg() {
        return this.user_msg;
    }

    public String getTransport() {
        return this.transport;
    }

    public String getFneib_name() {
        return this.fneib_name;
    }

    public String getFfs_ip() {
        return this.ffs_ip;
    }

    public String getFip() {
        return this.fip;
    }

    public String getNeibor_id() {
        return this.neibor_id;
    }

    public int getFport() {
        return this.fport;
    }

    public String toString() {
        return "NeiborBean [ffs_port=" + this.ffs_port + ", registrationTimeout=" + this.registrationTimeout + ", user_msg=" + this.user_msg + ", transport=" + this.transport + ", fneib_name=" + this.fneib_name + ", ffs_ip=" + this.ffs_ip + ", fip=" + this.fip + ", neibor_id=" + this.neibor_id + ", fport=" + this.fport + "]";
    }
}

