package com.xinspace.csevent.data.entity;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/8/21.
 */

public class GroupParticipants implements Serializable{


    /**
     * lack : 1
     * goods : {"id":"9","category":"6","title":"【两面针】植物防蛀清新牙膏 清爽薄荷香型 170g 防蛀固齿 清新口气 减少牙菌斑 多重维护口腔健康","groupnum":"2","goodsnum":"1","groupsprice":"8.80","thumb":"images/1/2017/04/Q2fCwbSPu32C2F38WoMz32MuIm3WkZ.jpg","goodsid":"9","price":"15.60","singleprice":"9.90","sales":"185"}
     * group : [{"openid":"o0S_71YvlY6q9i5MaF2EmObw3rpE","nickname":"ZLC","avatar":"images/1/2017/07/T1JNBBDm3jFED318BZ8Nj3FH3SSvNL.jpg"}]
     * endtime : 11960
     * recommend : [{"title":"补差价专用-不发货","groupnum":"3","goodsnum":"2","groupsprice":"0.01","thumb":"images/1/2017/06/ssW9oSwwSe9Zo5cz7Yd59Wf975x65M.jpg","goodsid":"21","price":"0.03","singleprice":"0.02","sales":"0"},{"title":"【艾漱艾窛】口腔伴侣漱口水 14秒搞定 美丽从\u201c齿\u201d开始 令人自信的神奇漱口水 纯天然德国进口配方 CCTV广告合作伙伴","groupnum":"2","goodsnum":"1","groupsprice":"58.80","thumb":"images/1/2017/05/OAUF5af5qBIiAEOY52FV5AHX133AqZ.jpg","goodsid":"16","price":"98.00","singleprice":"68.60","sales":"39"},{"title":"【两面针】两面针 中药牙膏药劲香型 消痛 深效修复牙膏 解决牙龈牙周等顽固问题引起的长期反复牙痛","groupnum":"2","goodsnum":"1","groupsprice":"38.80","thumb":"images/1/2017/04/vDDoe55lTe2b1Wx8X2tel8bEL10XE2.jpg","goodsid":"10","price":"59.90","singleprice":"45.60","sales":"77"}]
     */

    private int lack;
    private GoodsBean goods;
    private int endtime;
    private List<GroupBean> group;
    private List<RecommendBean> recommend;

    public static GroupParticipants objectFromData(String str) {

        return new Gson().fromJson(str, GroupParticipants.class);
    }

    public int getLack() {
        return lack;
    }

    public void setLack(int lack) {
        this.lack = lack;
    }

    public GoodsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsBean goods) {
        this.goods = goods;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public List<GroupBean> getGroup() {
        return group;
    }

    public void setGroup(List<GroupBean> group) {
        this.group = group;
    }

    public List<RecommendBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<RecommendBean> recommend) {
        this.recommend = recommend;
    }

    public static class GoodsBean implements Serializable{
        /**
         * id : 9
         * category : 6
         * title : 【两面针】植物防蛀清新牙膏 清爽薄荷香型 170g 防蛀固齿 清新口气 减少牙菌斑 多重维护口腔健康
         * groupnum : 2
         * goodsnum : 1
         * groupsprice : 8.80
         * thumb : images/1/2017/04/Q2fCwbSPu32C2F38WoMz32MuIm3WkZ.jpg
         * goodsid : 9
         * price : 15.60
         * singleprice : 9.90
         * sales : 185
         */

        private String id;
        private String category;
        private String title;
        private String groupnum;
        private String goodsnum;
        private String groupsprice;
        private String thumb;
        private String goodsid;
        private String price;
        private String singleprice;
        private String sales;

        public static GoodsBean objectFromData(String str) {

            return new Gson().fromJson(str, GoodsBean.class);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSingleprice() {
            return singleprice;
        }

        public void setSingleprice(String singleprice) {
            this.singleprice = singleprice;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }
    }

    public static class GroupBean implements Serializable{
        /**
         * openid : o0S_71YvlY6q9i5MaF2EmObw3rpE
         * nickname : ZLC
         * avatar : images/1/2017/07/T1JNBBDm3jFED318BZ8Nj3FH3SSvNL.jpg
         */

        private String openid;
        private String nickname;
        private String avatar;

        public static GroupBean objectFromData(String str) {

            return new Gson().fromJson(str, GroupBean.class);
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class RecommendBean implements Serializable{
        /**
         * title : 补差价专用-不发货
         * groupnum : 3
         * goodsnum : 2
         * groupsprice : 0.01
         * thumb : images/1/2017/06/ssW9oSwwSe9Zo5cz7Yd59Wf975x65M.jpg
         * goodsid : 21
         * price : 0.03
         * singleprice : 0.02
         * sales : 0
         */

        private String title;
        private String groupnum;
        private String goodsnum;
        private String groupsprice;
        private String thumb;
        private String goodsid;
        private String price;
        private String singleprice;
        private String sales;

        public static RecommendBean objectFromData(String str) {

            return new Gson().fromJson(str, RecommendBean.class);
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSingleprice() {
            return singleprice;
        }

        public void setSingleprice(String singleprice) {
            this.singleprice = singleprice;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }
    }
}
