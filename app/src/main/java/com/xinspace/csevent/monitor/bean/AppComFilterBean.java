package com.xinspace.csevent.monitor.bean;

/**
 * Created by Android on 2017/9/20.
 */

public class AppComFilterBean {

    private String commuintyId;
    private String commuintyName;
    private ContentBean bean;

    public String getCommuintyId() {
        return commuintyId;
    }

    public void setCommuintyId(String commuintyId) {
        this.commuintyId = commuintyId;
    }

    public String getCommuintyName() {
        return commuintyName;
    }

    public void setCommuintyName(String commuintyName) {
        this.commuintyName = commuintyName;
    }

    public ContentBean getBean() {
        return bean;
    }

    public void setBean(ContentBean bean) {
        this.bean = bean;
    }

    public static class ContentBean{
        private String unit;
        private String status;
        private String property;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

}
