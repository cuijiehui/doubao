package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.shop.fragment.AfterSaleFragment;
import com.xinspace.csevent.sweepstake.adapter.FragmentViewPagerAdapter;
import com.xinspace.csevent.sweepstake.fragment.BuyAllRecordFragment2;
import com.xinspace.csevent.sweepstake.fragment.BuyNoPayFragment2;
import com.xinspace.csevent.sweepstake.fragment.BuyNoRecevingFragment2;
import com.xinspace.csevent.sweepstake.fragment.BuyNoSendFragment2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单界面
 *
 * Created by Android on 2017/5/16.
 */

public class StoreOrderAct extends BaseActivity{

    private LinearLayout ll_store_order_back;
    private TabLayout tabLayout;
    private ViewPager vp_store_order;
    private SDPreference preference;
    private String openId;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private BuyAllRecordFragment2 allFragment;  // 全部
    private BuyNoPayFragment2 noPayFragment;  // 待付款
    private BuyNoSendFragment2 noSendFragment; //待发货
    private BuyNoRecevingFragment2 noRecevingFragment; // 待收货
    //private NoAppraiseFragment noAppraiseFragment;  // 待评价
    private AfterSaleFragment afterSaleFragment;     // 完成
    private String flag;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(StoreOrderAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_store_order);
        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
        intent = getIntent();
        if (intent != null){
            flag = intent.getStringExtra("flag");
            LogUtil.i("界面跳转flag" + flag);

        }
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("订单的activity");
    }

    private void initView() {
        ll_store_order_back = (LinearLayout) findViewById(R.id.ll_store_order_back);
        ll_store_order_back.setOnClickListener(onClickListener);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        vp_store_order = (ViewPager) findViewById(R.id.vp_store_order);

        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setText("待付款"));
        tabLayout.addTab(tabLayout.newTab().setText("待发货"));
        tabLayout.addTab(tabLayout.newTab().setText("待收货"));
        //tabLayout.addTab(tabLayout.newTab().setText("待评价"));
        tabLayout.addTab(tabLayout.newTab().setText("已完成"));

        allFragment = new BuyAllRecordFragment2(); // 全部
        fragmentList.add(allFragment);

        noPayFragment = new BuyNoPayFragment2();
        fragmentList.add(noPayFragment);

        noSendFragment = new BuyNoSendFragment2();
        fragmentList.add(noSendFragment);

        noRecevingFragment = new BuyNoRecevingFragment2();
        fragmentList.add(noRecevingFragment);

//        noAppraiseFragment = new NoAppraiseFragment();
//        fragmentList.add(noAppraiseFragment);

        afterSaleFragment = new AfterSaleFragment();
        fragmentList.add(afterSaleFragment);

        vp_store_order.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        // tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        vp_store_order.setOffscreenPageLimit(0);

        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        vp_store_order.addOnPageChangeListener(listener);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_store_order.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (flag !=null && flag.equals("0")){
            vp_store_order.setCurrentItem(0);
        }else if (flag !=null && flag.equals("1")){
            vp_store_order.setCurrentItem(1);
        }else if (flag !=null && flag.equals("2")){
            vp_store_order.setCurrentItem(2);
        }else if (flag !=null && flag.equals("3")){
            vp_store_order.setCurrentItem(3);
        }else if (flag !=null && flag.equals("4")){
            vp_store_order.setCurrentItem(4);
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_store_order_back:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        onClickListener = null;
        intent = null;
        fragmentList = null;
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
