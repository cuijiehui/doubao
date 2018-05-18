package sdk_sample.sdk.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/6/26.
 */

public class LockRecordBean implements Serializable{

    private String token;
    private String uid;
    private String phone;
    private String equip_sn;
    private String type;
    private String dataDay;

    public String getDataDay() {
        return dataDay;
    }

    public void setDataDay(String dataDay) {
        this.dataDay = dataDay;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEquip_sn() {
        return equip_sn;
    }

    public void setEquip_sn(String equip_sn) {
        this.equip_sn = equip_sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
