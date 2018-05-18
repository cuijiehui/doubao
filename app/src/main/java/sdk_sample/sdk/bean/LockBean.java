package sdk_sample.sdk.bean;

/**
 * Created by Android on 2017/3/23.
 */


import android.os.Parcel;
import android.os.Parcelable;



public class LockBean implements Parcelable {
    private static final long serialVersionUID = 1212512535L;
    private String lock_name;
    private String domain_sn;
    private String sip_number;
    private String lock_parent_name;
    private String username;
    private String layer;
    private int section;

    private int type; //数据显示的类别 1:Grid布局数据  2:List布局数据　3:类别布局数据

    protected LockBean(Parcel in) {
        lock_name = in.readString();
        domain_sn = in.readString();
        sip_number = in.readString();
        lock_parent_name = in.readString();
        username = in.readString();
        layer = in.readString();
        section = in.readInt();
        type = in.readInt();
    }

    public static final Creator<LockBean> CREATOR = new Creator<LockBean>() {
        @Override
        public LockBean createFromParcel(Parcel in) {
            return new LockBean(in);
        }

        @Override
        public LockBean[] newArray(int size) {
            return new LockBean[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getLock_name() {
        return this.lock_name;
    }

    public void setLock_name(String lock_name) {
        this.lock_name = lock_name;
    }

    public String getDomain_sn() {
        return this.domain_sn;
    }

    public void setDomain_sn(String domain_sn) {
        this.domain_sn = domain_sn;
    }

    public String getSip_number() {
        return this.sip_number;
    }

    public void setSip_number(String sip_number) {
        this.sip_number = sip_number;
    }

    public String getLock_parent_name() {
        return this.lock_parent_name;
    }

    public void setLock_parent_name(String lock_parent_name) {
        this.lock_parent_name = lock_parent_name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LockBean() {
    }

    public LockBean(String lock_name, String domain_sn, String sip_number, String lock_parent_name, String username) {
        this.lock_name = lock_name;
        this.domain_sn = domain_sn;
        this.sip_number = sip_number;
        this.lock_parent_name = lock_parent_name;
        this.username = username;
    }

    public static long getSerialversionuid() {
        return 1212512535L;
    }

    @Override
    public String toString() {
        return "LockBean{" +
                "lock_name='" + lock_name + '\'' +
                ", domain_sn='" + domain_sn + '\'' +
                ", sip_number='" + sip_number + '\'' +
                ", lock_parent_name='" + lock_parent_name + '\'' +
                ", username='" + username + '\'' +
                ", layer='" + layer + '\'' +
                ", section=" + section +
                ", type=" + type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lock_name);
        dest.writeString(domain_sn);
        dest.writeString(sip_number);
        dest.writeString(lock_parent_name);
        dest.writeString(username);
        dest.writeString(layer);
        dest.writeInt(section);
        dest.writeInt(type);
    }
}

