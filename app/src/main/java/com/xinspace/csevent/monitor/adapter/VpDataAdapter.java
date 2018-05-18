package com.xinspace.csevent.monitor.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.xinspace.csevent.monitor.bean.VideoAdBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * Created by Android on 2017/7/12.
 */

public class VpDataAdapter extends StaticPagerAdapter {

    private List<VideoAdBean> list;

    public void setList(List<VideoAdBean> list) {
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        ImagerLoaderUtil.displayImage(list.get(position).getUrl() , view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return null;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
