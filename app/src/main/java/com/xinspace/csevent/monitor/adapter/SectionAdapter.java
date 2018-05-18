package com.xinspace.csevent.monitor.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinspace.csevent.R;
import com.xinspace.csevent.monitor.bean.AppComBean;

import java.util.List;

/**
 * Created by Android on 2017/9/20.
 */

public class SectionAdapter extends BaseQuickAdapter<MySection, BaseViewHolder> {


    public SectionAdapter(@LayoutRes int layoutResId, @Nullable List<MySection> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        AppComBean dataBean = item.t;
        helper.setText(R.id.com_app_community_name, "社区名称：" + dataBean.getCommunity());
        helper.setText(R.id.com_app_unit_name, "单元名称：" + dataBean.getUnit());
        helper.setText(R.id.com_app_room_name, "门号名称：" + dataBean.getProperty());
        String status = dataBean.getStatus();
        if (status.equals("2")){
            helper.setImageResource(R.id.img_unit_add_status, R.drawable.unit_add_pass);
        }else{
            helper.setImageResource(R.id.img_unit_add_status, R.drawable.unit_add_check);
        }
}
}
