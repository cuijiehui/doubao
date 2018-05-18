package sdk_sample.sdk.bean;

/**
 * Created by Android on 2017/3/23.
 */

import java.io.Serializable;

public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1866669700L;
    private String username;
    private String random_num;
    private String token;
    private String user_id;
    private String user_password;
    private String user_sip;
    private boolean register_yet;
    private String gestire_pwd;

    public LoginBean() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRandom_num() {
        return this.random_num;
    }

    public void setRandom_num(String random_num) {
        this.random_num = random_num;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_password() {
        return this.user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public boolean isRegister_yet() {
        return this.register_yet;
    }

    public void setRegister_yet(boolean register_yet) {
        this.register_yet = register_yet;
    }

    public String getGestire_pwd() {
        return this.gestire_pwd;
    }

    public void setGestire_pwd(String gestire_pwd) {
        this.gestire_pwd = gestire_pwd;
    }

    public String getUser_sip() {
        return this.user_sip;
    }

    public void setUser_sip(String user_sip) {
        this.user_sip = user_sip;
    }

    public String toString() {
        return "LoginBean [username=" + this.username + ", random_num=" + this.random_num + ", token=" + this.token + ", user_id=" + this.user_id + ", user_password=" + this.user_password + ", user_sip=" + this.user_sip + ", register_yet=" + this.register_yet + ", gestire_pwd=" + this.gestire_pwd + "]";
    }
}

