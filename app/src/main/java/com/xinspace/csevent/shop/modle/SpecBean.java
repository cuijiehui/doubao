package com.xinspace.csevent.shop.modle;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/2.
 */

public class SpecBean implements Serializable{

    /**
     * id : 286
     * uniacid : 1
     * specid : 91
     * title : 滋润修复
     * thumb : http://shop.coresun.net/attachment/images/1/2017/04/Iw99NebeNC0eN2PPW0bUb2ZcB2lL4k.jpg
     * show : 1
     * displayorder : 0
     * valueId :
     * virtual : 0
     */

    private String id;
    private String uniacid;
    private String specid;
    private String title;
    private String thumb;
    private String show;
    private String displayorder;
    private String valueId;
    private String virtual;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUniacid() {
        return uniacid;
    }

    public void setUniacid(String uniacid) {
        this.uniacid = uniacid;
    }

    public String getSpecid() {
        return specid;
    }

    public void setSpecid(String specid) {
        this.specid = specid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(String displayorder) {
        this.displayorder = displayorder;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getVirtual() {
        return virtual;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }
}
