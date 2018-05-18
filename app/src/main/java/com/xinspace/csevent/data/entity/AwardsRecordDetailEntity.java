package com.xinspace.csevent.data.entity;

/**
 * 中奖记录详情实体类
 */

public class AwardsRecordDetailEntity {
    private String id;
    private String uid;
    private String aid;
    private String pid;
    private String startdate;
    private String uaddress;
    private String uname;
    private String utel;
    private String start;
    private String pname;
    private String type;
    private String num;
    private String price;//奖品的价格
    private String image;
    private String user_confirm;
    private String ctype;//奖品类型 gold 金币 integral 积分  product 产品 除商品外都不可更改发货地址，积分和金币自动发货
    private String confirmation_time;
    private String confirm;

    private String dname;
    private String number;
    private String noactivity;
    private String receipt_time;
    private String wintime;

    public String getWintime() {
        return wintime;
    }

    public void setWintime(String wintime) {
        this.wintime = wintime;
    }

    public String getReceipt_time() {
        return receipt_time;
    }

    public void setReceipt_time(String receipt_time) {
        this.receipt_time = receipt_time;
    }

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getConfirmation_time() {
        return confirmation_time;
    }

    public void setConfirmation_time(String confirmation_time) {
        this.confirmation_time = confirmation_time;
    }

    public AwardsRecordDetailEntity(String id, String uid, String aid, String pid, String startdate, String uaddress, String uname, String utel, String start, String pname, String type, String num, String price, String image, String user_confirm, String ctype) {
        this.id = id;
        this.uid = uid;
        this.aid = aid;
        this.pid = pid;
        this.startdate = startdate;
        this.uaddress = uaddress;
        this.uname = uname;
        this.utel = utel;
        this.start = start;
        this.pname = pname;
        this.type = type;
        this.num = num;
        this.price = price;
        this.image = image;
        this.user_confirm = user_confirm;
        this.ctype = ctype;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_confirm() {
        return user_confirm;
    }

    public void setUser_confirm(String user_confirm) {
        this.user_confirm = user_confirm;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    @Override
    public String toString() {
        return "AwardsRecordDetailEntity{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", aid='" + aid + '\'' +
                ", pid='" + pid + '\'' +
                ", startdate='" + startdate + '\'' +
                ", uaddress='" + uaddress + '\'' +
                ", uname='" + uname + '\'' +
                ", utel='" + utel + '\'' +
                ", start='" + start + '\'' +
                ", pname='" + pname + '\'' +
                ", type='" + type + '\'' +
                ", num='" + num + '\'' +
                ", image='" + image + '\'' +
                ", user_confirm='" + user_confirm + '\'' +
                ", ctype='" + ctype + '\'' +
                '}';
    }
}
