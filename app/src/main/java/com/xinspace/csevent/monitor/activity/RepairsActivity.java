package com.xinspace.csevent.monitor.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.monitor.fragment.RepairsFragment;
import com.xinspace.csevent.monitor.fragment.RepairsRecordFragment;
import com.xinspace.csevent.R;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * 报修界面
 * <p>
 * Created by Android on 2017/3/17.
 */

public class RepairsActivity extends BaseActivity {

    private LinearLayout ll_repairs_back;
    private TabLayout tab_repairs;
    private LinearLayout lin_repairs_content;
    private int currentIndex = 0;//当前的fragment下标
    private int selectedIndex = currentIndex;
    private Fragment[] fragmentArray = new Fragment[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_repairs);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);

        initView();
    }

    private void initView() {
        ll_repairs_back = (LinearLayout) findViewById(R.id.ll_repairs_back);
        ll_repairs_back.setOnClickListener(onClickListener);

        tab_repairs = (TabLayout) findViewById(R.id.tab_repairs);
        lin_repairs_content = (LinearLayout) findViewById(R.id.lin_repairs_content);

        tab_repairs.addTab(tab_repairs.newTab().setText("我要报修"));
        tab_repairs.addTab(tab_repairs.newTab().setText("报修记录"));

        fragmentArray[0]=new RepairsFragment();
        fragmentArray[1]=new RepairsRecordFragment();

        //添加
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.lin_repairs_content,fragmentArray[0]);
        //提交
        transaction.commit();

        // 设置tab文本的没有选中（第一个参数）和选中（第二个参数）的颜色
        tab_repairs.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#ef5948"));
        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tab_repairs);
        tab_repairs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
            switch (v.getId()) {
                case R.id.ll_repairs_back:
                    RepairsActivity.this.finish();
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
                transaction.add(R.id.lin_repairs_content,showFragment);
            }
            //隐藏 和显示
            transaction.remove(fragmentArray[currentIndex]);
            transaction.show(showFragment);
            transaction.commit();
            currentIndex=selectedIndex;
        }
    }

    @Override
    protected void onDestroy() {
        this.setContentView(R.layout.empty_view);
        fragmentArray = null;
        System.gc();
        super.onDestroy();
    }


}
