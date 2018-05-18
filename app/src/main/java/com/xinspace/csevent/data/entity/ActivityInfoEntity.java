package com.xinspace.csevent.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 抽奖活动的活动信息
 */
public class ActivityInfoEntity implements Serializable{
    private String endtime;
    private String winners;
    private String starttime;
    private String activity_id;
    private long surplus_time;//剩余时间
    private String remark;//描述
    private String image;
    private int type;//活动类型
    private String consume;//消耗金币
    private String createtime;//创建时间
    private String name;//活动名称
    private int special;//是否为特殊抽奖
    private double longitude;
    private double latitude;
    private String surplus_prize;//剩余奖品数量
    private int is_top;//是否热门
    private String html_is;//活动规则是否页面 0否 1 是
    private String html_href;//静态页面链接
    private String noactivity;
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNoactivity() {
        return noactivity;
    }

    public void setNoactivity(String noactivity) {
        this.noactivity = noactivity;
    }

    private String imgUrl;
    private List<String> urlList;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public ActivityInfoEntity() {
    }

    public ActivityInfoEntity(String endtime, String winners, String starttime, String activity_id, long surplus_time, String remark, String image, int type, String consume, String createtime, String name, int special, double longitude, double latitude, String surplus_prize, int is_top, String html_is, String html_href) {
        this.endtime = endtime;
        this.winners = winners;
        this.starttime = starttime;
        this.activity_id = activity_id;
        this.surplus_time = surplus_time;
        this.remark = remark;
        this.image = image;
        this.type = type;
        this.consume = consume;
        this.createtime = createtime;
        this.name = name;
        this.special = special;
        this.longitude = longitude;
        this.latitude = latitude;
        this.surplus_prize = surplus_prize;
        this.is_top = is_top;
        this.html_is = html_is;
        this.html_href = html_href;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getWinners() {
        return winners;
    }

    public void setWinners(String winners) {
        this.winners = winners;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public long getSurplus_time() {
        return surplus_time;
    }

    public void setSurplus_time(long surplus_time) {
        this.surplus_time = surplus_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getSurplus_prize() {
        return surplus_prize;
    }

    public void setSurplus_prize(String surplus_prize) {
        this.surplus_prize = surplus_prize;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public String getHtml_is() {
        return html_is;
    }

    public void setHtml_is(String html_is) {
        this.html_is = html_is;
    }

    public String getHtml_href() {
        return html_href;
    }

    public void setHtml_href(String html_href) {
        this.html_href = html_href;
    }

    @Override
    public String toString() {
        return "ActivityInfoEntity{" +
                "endtime='" + endtime + '\'' +
                ", winners=" + winners +
                ", starttime='" + starttime + '\'' +
                ", activity_id=" + activity_id +
                ", surplus_time=" + surplus_time +
                ", remark='" + remark + '\'' +
                ", image='" + image + '\'' +
                ", type=" + type +
                ", consume=" + consume +
                ", createtime='" + createtime + '\'' +
                ", name='" + name + '\'' +
                ", special=" + special +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", surplus_prize=" + surplus_prize +
                ", is_top=" + is_top +
                ", html_is=" + html_is +
                ", html_href='" + html_href + '\'' +
                '}';
    }
}
