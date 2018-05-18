package sdk_sample.sdk.result;

import java.io.Serializable;

/**
 * Created by Android on 2017/7/11.
 */

public class LoginSipBean implements Serializable{

    private String path_url;
    private String token;
    private String userName;
    private String neigbor_id;
    private long deal_time;
    private String sip_number;

    private String sip_password;
    private String sip_domin;
    private int sip_port;

    public String getPath_url() {
        return path_url;
    }

    public void setPath_url(String path_url) {
        this.path_url = path_url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNeigbor_id() {
        return neigbor_id;
    }

    public void setNeigbor_id(String neigbor_id) {
        this.neigbor_id = neigbor_id;
    }

    public long getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(long deal_time) {
        this.deal_time = deal_time;
    }

    public String getSip_number() {
        return sip_number;
    }

    public void setSip_number(String sip_number) {
        this.sip_number = sip_number;
    }

    public String getSip_password() {
        return sip_password;
    }

    public void setSip_password(String sip_password) {
        this.sip_password = sip_password;
    }

    public String getSip_domin() {
        return sip_domin;
    }

    public void setSip_domin(String sip_domin) {
        this.sip_domin = sip_domin;
    }

    public int getSip_port() {
        return sip_port;
    }

    public void setSip_port(int sip_port) {
        this.sip_port = sip_port;
    }
}
