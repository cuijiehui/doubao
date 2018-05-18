package com.xinspace.csevent.data.entity;


import java.io.Serializable;

/**
 * 夺宝记录实体
 */
public class DuobaoRecordEntity implements Serializable{
    private String num;//活动参与次数
    private String startdate;///创建时间
    private String pnum;//奖品总量
    private String name;//活动名称
    private String image;//活动图片
    private String iswin;//是否中奖
    private String snum;//发放数量
    private String aid;//活动ID
    private String type;//抽奖类型
    private String integral;//使用积分
    private String noactivity;
    private String match;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    public DuobaoRecordEntity() {
    }

    public DuobaoRecordEntity(String num, String startdate, String pnum, String name, String image, String iswin, String snum, String aid, String type, String integral) {
        this.num = num;
        this.startdate = startdate;
        this.pnum = pnum;
        this.name = name;
        this.image = image;
        this.iswin = iswin;
        this.snum = snum;
        this.aid = aid;
        this.type = type;
        this.integral = integral;
    }

    public String getIswin() {
        return iswin;
    }

    public void setIswin(String iswin) {
        this.iswin = iswin;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSnum() {
        return snum;
    }

    public void setSnum(String snum) {
        this.snum = snum;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    @Override
    public String toString() {
        return "DuobaoRecordEntity{" +
                "num='" + num + '\'' +
                ", startdate='" + startdate + '\'' +
                ", pnum='" + pnum + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", snum='" + snum + '\'' +
                ", aid='" + aid + '\'' +
                ", type='" + type + '\'' +
                ", integral='" + integral + '\'' +
                '}';
    }
}
