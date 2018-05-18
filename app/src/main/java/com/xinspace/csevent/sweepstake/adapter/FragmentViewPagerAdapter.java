package com.xinspace.csevent.sweepstake.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        // fragments.get(arg0).setUserVisibleHint(true);
        return fragments.get(arg0);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // fragments.get(position).setUserVisibleHint(false);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {

        return super.instantiateItem(arg0, arg1);
    }


}
