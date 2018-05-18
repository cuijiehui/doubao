package com.xinspace.csevent.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class AdvViewpagerAdapter extends PagerAdapter{
    private List<ImageView> list;

    public AdvViewpagerAdapter(List<ImageView> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewPager viewpager = (ViewPager) container;
        ImageView view = list.get(position);
        viewpager.addView(view);
        return view;
    }
    public void updateData(List<ImageView> list){
        this.list=list;
        this.notifyDataSetChanged();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewpager = (ViewPager) container;
        viewpager.removeView(list.get(position));
    }
}
