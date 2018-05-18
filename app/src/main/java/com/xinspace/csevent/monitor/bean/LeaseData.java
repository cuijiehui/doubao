package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/7/18.
 */

public class LeaseData implements Serializable{


    /**
     * code : 200
     * message : 获取租赁信息列表
     * data : [{"id":"1","title":"宏正地产推荐上城国际精装中高层楼盘","pic":"http://shop.coresun.net/attachment/images/1/2017/07/GNeUt23BuTZp02ocB2p912b3uTJ91u.jpg","address":"天河北路寰城海航广场27楼","house_type":"1室","rental":"1200","acreage":"50","orientations":"南北向","decoration":"精装","charge_pay":"押一付三","mobile":"15071349024","contact":"吴女士"},{"id":"2","title":"海珠区新港中路精装中高层楼盘","pic":"http://shop.coresun.net/attachment/images/1/2017/06/U43u1tZJPjzgG9VpgU3t4ejP236pjv.jpg","address":"广东省广州市海珠区新港中路","house_type":"2室","rental":"1800","acreage":"45","orientations":"南北向","decoration":"精装","charge_pay":"押二付一","mobile":"18122326969","contact":"王先生"},{"id":"3","title":"3号线市桥站附近精装城中村","pic":"http://shop.coresun.net/attachment/images/1/2017/06/Pjj28cDFz8G8K8Mxh8wjC6VQYcdF91.jpg","address":"3号线市桥站D出口","house_type":"3室","rental":"2500","acreage":"96","orientations":"东西向","decoration":"精装","charge_pay":"押二付一","mobile":"18965423698","contact":"李先生"}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 宏正地产推荐上城国际精装中高层楼盘
         * pic : http://shop.coresun.net/attachment/images/1/2017/07/GNeUt23BuTZp02ocB2p912b3uTJ91u.jpg
         * address : 天河北路寰城海航广场27楼
         * house_type : 1室
         * rental : 1200
         * acreage : 50
         * orientations : 南北向
         * decoration : 精装
         * charge_pay : 押一付三
         * mobile : 15071349024
         * contact : 吴女士
         */

        private String id;
        private String title;
        private String pic;
        private String address;
        private String house_type;
        private String rental;
        private String acreage;
        private String orientations;
        private String decoration;
        private String charge_pay;
        private String mobile;
        private String contact;

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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHouse_type() {
            return house_type;
        }

        public void setHouse_type(String house_type) {
            this.house_type = house_type;
        }

        public String getRental() {
            return rental;
        }

        public void setRental(String rental) {
            this.rental = rental;
        }

        public String getAcreage() {
            return acreage;
        }

        public void setAcreage(String acreage) {
            this.acreage = acreage;
        }

        public String getOrientations() {
            return orientations;
        }

        public void setOrientations(String orientations) {
            this.orientations = orientations;
        }

        public String getDecoration() {
            return decoration;
        }

        public void setDecoration(String decoration) {
            this.decoration = decoration;
        }

        public String getCharge_pay() {
            return charge_pay;
        }

        public void setCharge_pay(String charge_pay) {
            this.charge_pay = charge_pay;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
    }
}
