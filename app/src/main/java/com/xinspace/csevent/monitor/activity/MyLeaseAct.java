package com.xinspace.csevent.monitor.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;


import com.xinspace.csevent.monitor.fragment.AllLeaseFragment;
import com.xinspace.csevent.monitor.fragment.LeaseFragment2;
import com.xinspace.csevent.monitor.fragment.LeaseFragment3;
import com.xinspace.csevent.monitor.fragment.LeaseFragment4;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.sweepstake.adapter.FragmentViewPagerAdapter;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/7/21.
 */

public class MyLeaseAct extends BaseActivity{

    private LinearLayout ll_my_lease_back;
    private ViewPager vp_my_lease;
    private SDPreference sdPreference;
    private TabLayout tablayout;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private AllLeaseFragment allFragment;
    private String uid;
    private String token;
    private LeaseFragment2 leaseFragment2;
    private LeaseFragment3 leaseFragment3;
    private LeaseFragment4 leaseFragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(MyLeaseAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_my_lease);
        sdPreference = SDPreference.getInstance();
        uid  = sdPreference.getContent("cUid");
        token = sdPreference.getContent("cToken");
        initView();
    }

    private void initView() {

        ll_my_lease_back = (LinearLayout) findViewById(R.id.ll_my_lease_back);
        ll_my_lease_back.setOnClickListener(onClickListener);
        vp_my_lease = (ViewPager) findViewById(R.id.vp_my_lease);
        tablayout = (TabLayout) findViewById(R.id.tablayout);

        tablayout.addTab(tablayout.newTab().setText("全部"));
        tablayout.addTab(tablayout.newTab().setText("已预约"));
        tablayout.addTab(tablayout.newTab().setText("已过期"));
        tablayout.addTab(tablayout.newTab().setText("已完成"));

        allFragment = new AllLeaseFragment();
        fragmentList.add(allFragment);

        leaseFragment2 = new LeaseFragment2();
        fragmentList.add(leaseFragment2);

        leaseFragment3 = new LeaseFragment3();
        fragmentList.add(leaseFragment3);

        leaseFragment4 = new LeaseFragment4();
        fragmentList.add(leaseFragment4);

        vp_my_lease.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        // tabLayout.setupWithViewPager(viewPager);
        tablayout.setTabMode(TabLayout.MODE_FIXED);

        vp_my_lease.setOffscreenPageLimit(0);

        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tablayout);
        vp_my_lease.addOnPageChangeListener(listener);

        allFragment.initData(0 , "0" , uid , token);  //0    ,   1

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_my_lease.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp_my_lease.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                LogUtil.i("---onPageScrolled----" + position + "positionOffset" + positionOffset
//                        + "positionOffsetPixels" + positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {

                //LogUtil.i("---onPageSelected----" + position);

                if (position == 0){
                    allFragment.initData(0 , "0" , uid , token);
                }

                if (position == 1){
                    leaseFragment2.initData(0 , "0" , uid , token);
                }

                if (position == 2){
                    leaseFragment3.initData(0 , "0" , uid , token);
                }

                if (position == 3){
                    leaseFragment4.initData(0 , "0" , uid , token);
                }
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
                case R.id.ll_my_lease_back:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        onClickListener = null;
        fragmentList = null;
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
