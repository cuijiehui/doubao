package com.xinspace.csevent.shop.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/6/8.
 */

public class GroupDetailBean implements Serializable{


    /**
     * id : 12
     * title : 【移民汇】广东德庆特产 紫淮山1.5kg 新鲜紫山药 又称紫人参 3斤装  蔬菜之王 地中之宝 营养物质更加丰富
     * groupnum : 2
     * goodsnum : 1
     * units : 件
     * groupsprice : 21.00
     * thumb : https://wx.szshide.shop/attachment/images/1/2017/05/LtRrMJMMAwbR0McwRC50EsQqsRjSSW.jpg
     * goodsid : 12
     * teamnum : 47
     * thumb_url : ["https://wx.szshide.shop/attachment/images/1/2017/05/RLm5e5226g6G6YtEQt26CV52SY5nVL.jpg","https://wx.szshide.shop/attachment/images/1/2017/05/p4lKn4vmDntk4CCA006033tN0lC062.jpg","https://wx.szshide.shop/attachment/images/1/2017/05/OwrAUm4AbMhQqUqYghlbW6Bqgx44Bg.jpg"]
     * price : 26.50
     * content : <p><img src="http://wx.szshide.shop/attachment/images/1/2017/05/Tn22hv5Yn5R2TE2eUYnvVNNtlOjQr3.jpg" width="100%" alt="images/1/2017/05/Tn22hv5Yn5R2TE2eUYnvVNNtlOjQr3.jpg"/></p>
     * sales : 61
     * des : 紫淮山含有皂甙、黏液质，有润滑，滋润的作用，故可益肺气，养肺阴，治疗肺虚痰嗽久咳之症。
     * fightnum : 48
     */

    private String id;
    private String title;
    private String groupnum;
    private String goodsnum;
    private String units;
    private String groupsprice;
    private String thumb;
    private String goodsid;
    private String teamnum;
    private String price;
    private String content;
    private String sales;
    private String des;
    private String fightnum;
    private String singleprice;

    public String getSingleprice() {
        return singleprice;
    }

    public void setSingleprice(String singleprice) {
        this.singleprice = singleprice;
    }

    private List<String> thumb_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroupnum() {
        return groupnum;
    }

    public void setGroupnum(String groupnum) {
        this.groupnum = groupnum;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getGroupsprice() {
        return groupsprice;
    }

    public void setGroupsprice(String groupsprice) {
        this.groupsprice = groupsprice;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getTeamnum() {
        return teamnum;
    }

    public void setTeamnum(String teamnum) {
        this.teamnum = teamnum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getFightnum() {
        return fightnum;
    }

    public void setFightnum(String fightnum) {
        this.fightnum = fightnum;
    }

    public List<String> getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(List<String> thumb_url) {
        this.thumb_url = thumb_url;
    }
}
