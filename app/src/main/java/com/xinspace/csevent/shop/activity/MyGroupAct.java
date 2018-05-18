package com.xinspace.csevent.shop.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.fragment.GroupFailFragment;
import com.xinspace.csevent.shop.fragment.GroupSucFragment;
import com.xinspace.csevent.shop.fragment.GroupingFragment;
import com.xinspace.csevent.sweepstake.adapter.FragmentViewPagerAdapter;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的拼团
 *
 * Created by Android on 2017/6/12.
 */

public class MyGroupAct extends BaseActivity{

    private TabLayout tablayout;
    private ViewPager vp_my_group;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private LinearLayout ll_my_group_back;
    private GroupingFragment groupingFragment;
    private GroupSucFragment groupSucFragment;
    private GroupFailFragment groupFailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(MyGroupAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_my_group);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        ll_my_group_back = (LinearLayout) findViewById(R.id.ll_my_group_back);
        ll_my_group_back.setOnClickListener(onClickListener);

        vp_my_group = (ViewPager) findViewById(R.id.vp_my_group);
        tablayout = (TabLayout) findViewById(R.id.tablayout);

        tablayout.addTab(tablayout.newTab().setText("拼团中"));
        tablayout.addTab(tablayout.newTab().setText("拼团成功"));
        tablayout.addTab(tablayout.newTab().setText("拼团失败"));

        groupingFragment = new GroupingFragment();
        groupSucFragment = new GroupSucFragment();
        groupFailFragment = new GroupFailFragment();

        fragmentList.add(groupingFragment);
        fragmentList.add(groupSucFragment);
        fragmentList.add(groupFailFragment);

        vp_my_group.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        // tabLayout.setupWithViewPager(viewPager);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        vp_my_group.setOffscreenPageLimit(0);

        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tablayout);
        vp_my_group.addOnPageChangeListener(listener);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_my_group.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /**
         *
         *
         */
        vp_my_group.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {      //滑动选择界面

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_my_group_back:
                    MyGroupAct.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        onClickListener = null;
        fragmentList = null;
        ImagerLoaderUtil.clearImageMemory();
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
