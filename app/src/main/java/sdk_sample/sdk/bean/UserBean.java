package sdk_sample.sdk.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/3/23.
 */

public class UserBean implements Serializable {
    private String user_sip;
    private int fs_port;
    private String user_password;
    private String fs_ip;

    public UserBean() {
    }

    public void setUser_sip(String user_sip) {
        this.user_sip = user_sip;
    }

    public void setFs_port(int fs_port) {
        this.fs_port = fs_port;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setFs_ip(String fs_ip) {
        this.fs_ip = fs_ip;
    }

    public String getUser_sip() {
        return this.user_sip;
    }

    public int getFs_port() {
        return this.fs_port;
    }

    public String getUser_password() {
        return this.user_password;
    }

    public String getFs_ip() {
        return this.fs_ip;
    }

    public String toString() {
        return "UserBean [user_sip=" + this.user_sip + ", fs_port=" + this.fs_port + ", user_password=" + this.user_password + ", fs_ip=" + this.fs_ip + "]";
    }
}
