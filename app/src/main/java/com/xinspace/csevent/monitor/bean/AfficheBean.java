package com.xinspace.csevent.monitor.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/5/25.
 */

public class AfficheBean implements Serializable{


    /**
     * title : 电路维修
     * content : 2017年4月9号晚上6点到7点断电
     * create_time : 2017-04-06 11:07:58
     * id : 3
     */

    private String title;
    private String content;
    private String create_time;
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
