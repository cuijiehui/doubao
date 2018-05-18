package com.xinspace.csevent.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.ui.fragment.AwardPoolFragment;
import com.xinspace.csevent.ui.fragment.GeneralAwardFragment;

/***
 * 抽奖类型页面
 */
public class AwardStyleActivity extends BaseActivity {
    private LinearLayout ll_back;
    private LinearLayout ll_bar1;
    private LinearLayout ll_bar2;

    private TextView tv_title1;
    private TextView tv_title2;

    private LinearLayout ll_bg1;
    private LinearLayout ll_bg2;

    private Fragment[] fragmentArray=new Fragment[2];//fragment数组
    private TextView[] textviewArray=new TextView[2];
    private LinearLayout[] linearArray=new LinearLayout[2];

    private int currentIndex=0;//当前的fragment下标
    private int selectedIndex=currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_style);
        setView();
        setListener();
    }
    //设置监听
    private void setListener() {
        //返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //普通抽奖
        ll_bar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = 0;
                changePage();
                changeBackground();
            }
        });
        //抽奖池
        ll_bar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = 1;
                changePage();
                changeBackground();
            }
        });
    }
    //初始化组件
    private void setView() {
        ll_back= (LinearLayout) findViewById(R.id.ll_award_style_back);

        ll_bar1= (LinearLayout) findViewById(R.id.ll_award_style_general);
        ll_bar2= (LinearLayout) findViewById(R.id.ll_award_style_pool);

        tv_title1= (TextView) findViewById(R.id.tv_award_style_title1);
        tv_title2= (TextView) findViewById(R.id.tv_award_style_title2);

        ll_bg1= (LinearLayout) findViewById(R.id.ll_award_style_bg1);
        ll_bg2= (LinearLayout) findViewById(R.id.ll_award_style_bg2);

        //标题放入数组中
        textviewArray[0]=tv_title1;
        textviewArray[1]=tv_title2;

        //下划线背景放入数组中
        linearArray[0]=ll_bg1;
        linearArray[1]=ll_bg2;

        //添加fragment
        fragmentArray[0]=new GeneralAwardFragment();
        fragmentArray[1]=new AwardPoolFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ll_duobao_record_container,fragmentArray[0]);
        //提交
        transaction.commit();
        tv_title1.setTextColor(getResources().getColor(R.color.color_app_base_color));
    }
    //改变背景
    private void changeBackground() {
        if(currentIndex==0){
            //改变下划线背景
            for (LinearLayout ll:linearArray) {
                ll.setBackgroundColor(getResources().getColor(R.color.color_divider_background));
            }
            ll_bg1.setBackgroundColor(getResources().getColor(R.color.color_app_base_color));
            //改变文字背景
            for (TextView tv:textviewArray) {
                tv.setTextColor(getResources().getColor(R.color.color_font_gray));
            }
            tv_title1.setTextColor(getResources().getColor(R.color.color_app_base_color));
        }
        if(currentIndex==1){
            //改变下划线背景
            for (LinearLayout ll:linearArray) {
                ll.setBackgroundColor(getResources().getColor(R.color.color_divider_background));
            }
            ll_bg2.setBackgroundColor(getResources().getColor(R.color.color_app_base_color));
            //改变文字背景
            for (TextView tv:textviewArray) {
                tv.setTextColor(getResources().getColor(R.color.color_font_gray));
            }
            tv_title2.setTextColor(getResources().getColor(R.color.color_app_base_color));
        }
    }
    //改变页面
    private void changePage(){
        //选中的不是当前的按钮
        if(selectedIndex!=currentIndex){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment showFragment = fragmentArray[selectedIndex];
            if(!showFragment.isAdded()){
                //将选中的响应页面添加到页面
                transaction.add(R.id.ll_duobao_record_container,showFragment);
            }
            //隐藏 和显示
            transaction.hide(fragmentArray[currentIndex]);
            transaction.show(showFragment);
            transaction.commit();
            currentIndex=selectedIndex;
        }
    }
}
