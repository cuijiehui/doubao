package com.xinspace.csevent.sweepstake.modle;

import com.xinspace.csevent.data.biz.CommunityUnit;

/**
 * Created by Android on 2016/12/7.
 */
public class FirstEvent {

    private String eventMsg;

    private Object object;

    public String getEventMsg() {
        return eventMsg;
    }

    public CommunityUnit getCommunityUnit(){
        return (CommunityUnit) object;
    }

    public FirstEvent(String event) {
        eventMsg = event;
    }

    public FirstEvent (String event, Object object){
        this.eventMsg = event;
        this.object = object;
    }

}
