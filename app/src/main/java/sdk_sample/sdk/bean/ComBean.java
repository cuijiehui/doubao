package sdk_sample.sdk.bean;

import com.xinspace.csevent.monitor.bean.AppComBean;

import java.util.List;

/**
 * Created by Android on 2017/6/23.
 */

public class ComBean {

    private String comTitle;
    private String layer;
    private List<LockBean> list;

    private List<AppComBean> beanList;

    public List<AppComBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<AppComBean> beanList) {
        this.beanList = beanList;
    }

    public String getComTitle() {
        return comTitle;
    }

    public void setComTitle(String comTitle) {
        this.comTitle = comTitle;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public List<LockBean> getList() {
        return list;
    }

    public void setList(List<LockBean> list) {
        this.list = list;
    }
}
