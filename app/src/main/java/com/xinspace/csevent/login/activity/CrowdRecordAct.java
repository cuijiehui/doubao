package com.xinspace.csevent.login.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.fragment.CRAllFragment;
import com.xinspace.csevent.login.fragment.CRPublishFragment;
import com.xinspace.csevent.login.fragment.CRingFragment;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/10/14.
 *
 * 众筹抽奖记录
 */
public class CrowdRecordAct extends BaseActivity{

    private TabLayout tabLayout;
    private LinearLayout ll_back;
    private LinearLayout lin_content;
    private int currentIndex=0;//当前的fragment下标
    private int selectedIndex=currentIndex;
    private Fragment[] fragmentArray=new Fragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_crowdrecod);
        initView();

    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(onClickListener);
        lin_content = (LinearLayout) findViewById(R.id.lin_content);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("所有"));
        tabLayout.addTab(tabLayout.newTab().setText("进行中"));
        tabLayout.addTab(tabLayout.newTab().setText("已揭晓"));

        fragmentArray[0]=new CRAllFragment();
        fragmentArray[1]=new CRingFragment();
        fragmentArray[2]=new CRPublishFragment();

        //添加
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.lin_content,fragmentArray[0]);
        //提交
        transaction.commit();


        // 设置tab文本的没有选中（第一个参数）和选中（第二个参数）的颜色
        tabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#ef5948"));
        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedIndex = tab.getPosition();
                changePage();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    finish();
                    break;
            }
        }
    };


    //改变页面
    private void changePage(){
        //选中的不是当前的按钮
        if(selectedIndex != currentIndex){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment showFragment = fragmentArray[selectedIndex];
            if(!showFragment.isAdded()){
                //将选中的响应页面添加到页面
                transaction.add(R.id.lin_content,showFragment);
            }
            //隐藏 和显示
            transaction.remove(fragmentArray[currentIndex]);
            transaction.show(showFragment);
            transaction.commit();
            currentIndex=selectedIndex;
        }
    }

}
