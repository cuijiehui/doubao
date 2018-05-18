package com.xinspace.csevent.monitor.adapter;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.xinspace.csevent.monitor.bean.AppComBean;

/**
 * Created by Android on 2017/9/20.
 */

public class MySection extends SectionEntity<AppComBean>{

    private Boolean isMore;

    public MySection(boolean isHeader, String header, boolean isMore) {
        super(isHeader, header);
        this.isMore = isMore;
    }

    public MySection(AppComBean t){
        super(t);
    }

    public void setMore(Boolean more) {
        isMore = more;
    }

    public boolean isMore(){
        return isMore;
    }
}
