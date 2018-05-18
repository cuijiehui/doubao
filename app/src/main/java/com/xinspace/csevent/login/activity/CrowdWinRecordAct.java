package com.xinspace.csevent.login.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.fragment.CWNoSendFragment;
import com.xinspace.csevent.login.fragment.CWNoRecevingFragment;
import com.xinspace.csevent.login.fragment.CWRllFragment;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/10/18.
 *
 * 众筹中奖纪录列表
 */
public class CrowdWinRecordAct extends BaseActivity{

    private LinearLayout ll_back;
    private LinearLayout lin_content;
    private TabLayout tablayout;

    private int currentIndex=0;//当前的fragment下标
    private int selectedIndex=currentIndex;

    private ImageView[] iv_ary=new ImageView[3];
    private Fragment[] fragmentArray=new Fragment[3];
    private TextView[] tv_ary=new TextView[3];

    private ImageView iv_all_win_record;
    private ImageView iv_win_no_send;
    private ImageView iv_win_no_receiving;

    private TextView tv_all_win_record;
    private TextView tv_win_no_send;
    private TextView tv_win_no_receiving;

    private RelativeLayout rel_all_win_record , rel_win_no_send, rel_win_no_receiving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_crowd_win_record);
        
        initView();
        
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(onClickListener);

        lin_content = (LinearLayout) findViewById(R.id.lin_content);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab());
        tablayout.addTab(tablayout.newTab());
        tablayout.addTab(tablayout.newTab());


        rel_all_win_record = (RelativeLayout) findViewById(R.id.rel_all_win_record);
        rel_win_no_send = (RelativeLayout) findViewById(R.id.rel_win_no_send);
        rel_win_no_receiving = (RelativeLayout) findViewById(R.id.rel_win_no_receiving);

        rel_all_win_record.setOnClickListener(onClickListener);
        rel_win_no_send.setOnClickListener(onClickListener);
        rel_win_no_receiving.setOnClickListener(onClickListener);

        iv_all_win_record = (ImageView) findViewById(R.id.iv_all_win_record);
        iv_win_no_send = (ImageView) findViewById(R.id.iv_win_no_send);
        iv_win_no_receiving = (ImageView) findViewById(R.id.iv_win_no_receiving);

        iv_ary[0] = iv_all_win_record;
        iv_ary[1] = iv_win_no_send;
        iv_ary[2] = iv_win_no_receiving;

        tv_all_win_record = (TextView) findViewById(R.id.tv_all_win_record);
        tv_win_no_send = (TextView) findViewById(R.id.tv_win_no_send);
        tv_win_no_receiving = (TextView) findViewById(R.id.tv_win_no_receiving);

        tv_ary[0] = tv_all_win_record;
        tv_ary[1] = tv_win_no_send;
        tv_ary[2] = tv_win_no_receiving;

        fragmentArray[0]=new CWRllFragment();
        fragmentArray[1]=new CWNoSendFragment();
        fragmentArray[2]=new CWNoRecevingFragment();

        //添加
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.lin_content,fragmentArray[0]);
        //提交
        transaction.commit();
        //第一次进入默认设置首页导航高亮显示
        iv_ary[0].setImageResource(R.drawable.icon_all_check);
        tv_ary[0].setTextColor(Color.parseColor("#ef5948"));

        // 设置tab文本的没有选中（第一个参数）和选中（第二个参数）的颜色
        tablayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#ef5948"));
        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tablayout);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedIndex = tab.getPosition();
                changePage();
                changBackground();
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
                case R.id.rel_all_win_record:
                    selectedIndex = 0;
                    changePage();
                    changBackground();
                    tablayout.setScrollPosition(0 , 0f , true);
                    break;
                case R.id.rel_win_no_send:
                    selectedIndex = 1;
                    changePage();
                    changBackground();
                    tablayout.setScrollPosition(1 , 0f , true);
                    break;
                case R.id.rel_win_no_receiving:
                    selectedIndex = 2;
                    changePage();
                    changBackground();
                    tablayout.setScrollPosition(2 , 0f , true);
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


    private void changBackground() {
        if(selectedIndex==0){
            iv_ary[0].setImageResource(R.drawable.icon_all_check);
            iv_ary[1].setImageResource(R.drawable.icon_nosend);
            iv_ary[2].setImageResource(R.drawable.icon_no_receving);

            tv_ary[0].setTextColor(Color.parseColor("#ef5948"));
            tv_ary[1].setTextColor(Color.parseColor("#666666"));
            tv_ary[2].setTextColor(Color.parseColor("#666666"));

        }
        if(selectedIndex==1){
            iv_ary[0].setImageResource(R.drawable.icon_all);
            iv_ary[1].setImageResource(R.drawable.icon_nosend_check);
            iv_ary[2].setImageResource(R.drawable.icon_no_receving);

            tv_ary[0].setTextColor(Color.parseColor("#666666"));
            tv_ary[1].setTextColor(Color.parseColor("#ef5948"));
            tv_ary[2].setTextColor(Color.parseColor("#666666"));

        }
        if(selectedIndex==2){
            iv_ary[0].setImageResource(R.drawable.icon_all);
            iv_ary[1].setImageResource(R.drawable.icon_nosend);
            iv_ary[2].setImageResource(R.drawable.icon_no_receving_check);

            tv_ary[0].setTextColor(Color.parseColor("#666666"));
            tv_ary[1].setTextColor(Color.parseColor("#666666"));
            tv_ary[2].setTextColor(Color.parseColor("#ef5948"));

        }
    }



}
