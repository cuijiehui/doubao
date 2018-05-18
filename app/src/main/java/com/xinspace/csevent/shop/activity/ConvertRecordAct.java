package com.xinspace.csevent.shop.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.shop.fragment.AllConvertFragment;
import com.xinspace.csevent.shop.fragment.ConvertFinishFragment;
import com.xinspace.csevent.shop.fragment.ConvertReceivingFragment;
import com.xinspace.csevent.shop.fragment.ConvertShippedFragment;
import com.xinspace.csevent.sweepstake.adapter.FragmentViewPagerAdapter;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的积分兑换
 *
 * Created by Android on 2017/6/19.
 */

public class ConvertRecordAct extends BaseActivity{

    private TabLayout tablayout;
    private ViewPager vp_my_convert;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private LinearLayout ll_my_convert_back;
    private AllConvertFragment allConvertFragment;
    private ConvertShippedFragment convertShippedFragment;
    private ConvertReceivingFragment convertReceivingFragment;
    private ConvertFinishFragment convertFinishFragment;

    private SDPreference sdPreference;
    private String openId;

//    private GroupSucFragment groupSucFragment;
//    private GroupFailFragment groupFailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(ConvertRecordAct.this , R.color.app_bottom_color);

        setContentView(R.layout.act_convert_record);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {

        sdPreference = SDPreference.getInstance();
        openId = sdPreference.getContent("openid");

        ll_my_convert_back = (LinearLayout) findViewById(R.id.ll_my_convert_back);
        ll_my_convert_back.setOnClickListener(onClickListener);

        vp_my_convert = (ViewPager) findViewById(R.id.vp_my_convert);
        tablayout = (TabLayout) findViewById(R.id.tablayout);

        tablayout.addTab(tablayout.newTab().setText("全部"));
        tablayout.addTab(tablayout.newTab().setText("待发货"));
        tablayout.addTab(tablayout.newTab().setText("待收货"));
        tablayout.addTab(tablayout.newTab().setText("已完成"));

        allConvertFragment = new AllConvertFragment();
        convertShippedFragment = new ConvertShippedFragment();
        convertReceivingFragment = new ConvertReceivingFragment();
        convertFinishFragment = new ConvertFinishFragment();

        fragmentList.add(allConvertFragment);
        fragmentList.add(convertShippedFragment);
        fragmentList.add(convertReceivingFragment);
        fragmentList.add(convertFinishFragment);

        vp_my_convert.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        // tabLayout.setupWithViewPager(viewPager);
        tablayout.setTabMode(TabLayout.MODE_FIXED);

        vp_my_convert.setOffscreenPageLimit(0);

        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tablayout);
        vp_my_convert.addOnPageChangeListener(listener);

        allConvertFragment.initData(0 , "0" , openId);  //0    ,   1

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_my_convert.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp_my_convert.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                LogUtil.i("---onPageScrolled----" + position + "positionOffset" + positionOffset
//                        + "positionOffsetPixels" + positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {

                //LogUtil.i("---onPageSelected----" + position);

                if (position == 0){
                    allConvertFragment.initData(0 , "0" , openId);
                }

                if (position == 1){
                    convertShippedFragment.initData(0 , "0");
                }

                if (position == 2){
                    convertReceivingFragment.initData(0 , "0");
                }

                if (position == 3){
                    convertFinishFragment.initData(0 , "0");
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
                case R.id.ll_my_convert_back:
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
