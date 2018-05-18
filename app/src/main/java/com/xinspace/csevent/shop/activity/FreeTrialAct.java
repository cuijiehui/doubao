package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.fragment.AllTryFragment;
import com.xinspace.csevent.shop.fragment.ApplySkillsFragment;
import com.xinspace.csevent.shop.fragment.MyTrialFragment;
import com.xinspace.csevent.shop.fragment.TryFirstFragment;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * 免费试用基类
 *
 * Created by Android on 2017/6/14.
 */

public class FreeTrialAct extends BaseActivity{

    private LinearLayout ll_try_content;

    private LinearLayout ll_try_first;
    private LinearLayout ll_all_try;
    private LinearLayout ll_apply_skills;
    private LinearLayout ll_my_trial;

    private ImageView iv_try_first;
    private ImageView iv_all_try;
    private ImageView iv_apply_skills;
    private ImageView iv_my_trial;

    private TextView tv_try_first;
    private TextView tv_all_try;
    private TextView tv_apply_skills;
    private TextView tv_my_trial;

    private ImageView[] iv_ary = new ImageView[4];
    private Fragment[] fragmentArray = new Fragment[4];
    private TextView[] tv_ary = new TextView[4];

    private int currentIndex = 0;//当前的fragment下标
    private int selectedIndex = currentIndex;
    private FragmentTransaction transaction;

    private TryFirstFragment tryFristFragment;
    private AllTryFragment allTryFragment;
    private ApplySkillsFragment applySkillsFragment;
    private MyTrialFragment myTrialFragment;

    private TextView tv_trial_title;
    private LinearLayout ll_trial_back;
    private Intent intent;
    private String flag;
    private String from;
    private String clickPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(FreeTrialAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_free_trial);

        intent = getIntent();
        if (intent != null){
            flag = intent.getStringExtra("flag");
            clickPage = intent.getStringExtra("clickPage");
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        from = intent.getStringExtra("from");
        LogUtil.i("----------from---------------"+ from);
        if (from != null && !from.equals("")){
            if (from.equals("MyTrailAct")){
                selectedIndex = 0;
                changePage();
                changBackground();
            }
        }
    }

    private void initView() {

        ll_try_content = (LinearLayout) findViewById(R.id.ll_try_content);
        ll_try_first = (LinearLayout) findViewById(R.id.ll_try_first);
        ll_all_try = (LinearLayout) findViewById(R.id.ll_all_try);
        ll_apply_skills = (LinearLayout) findViewById(R.id.ll_apply_skills);
        ll_my_trial = (LinearLayout) findViewById(R.id.ll_my_trial);

        ll_try_first.setOnClickListener(clickListener);
        ll_all_try.setOnClickListener(clickListener);
        ll_apply_skills.setOnClickListener(clickListener);
        ll_my_trial.setOnClickListener(clickListener);

        fragmentArray[0] = new TryFirstFragment();
        fragmentArray[1] = new AllTryFragment();
        fragmentArray[2] = new ApplySkillsFragment();
        fragmentArray[3] = new MyTrialFragment();

        iv_try_first = (ImageView) findViewById(R.id.iv_try_first);
        iv_all_try = (ImageView) findViewById(R.id.iv_all_try);
        iv_apply_skills = (ImageView) findViewById(R.id.iv_apply_skills);
        iv_my_trial = (ImageView) findViewById(R.id.iv_my_trial);
        iv_ary[0] = iv_try_first;
        iv_ary[1] = iv_all_try;
        iv_ary[2] = iv_apply_skills;
        iv_ary[3] = iv_my_trial;

        tv_try_first = (TextView) findViewById(R.id.tv_try_first);
        tv_all_try = (TextView) findViewById(R.id.tv_all_try);
        tv_apply_skills = (TextView) findViewById(R.id.tv_apply_skills);
        tv_my_trial = (TextView) findViewById(R.id.tv_my_trial);
        tv_ary[0] = tv_try_first;
        tv_ary[1] = tv_all_try;
        tv_ary[2] = tv_apply_skills;
        tv_ary[3] = tv_my_trial;

        tv_trial_title = (TextView) findViewById(R.id.tv_trial_title);
        tv_trial_title.setText("免费试用");

        ll_trial_back = (LinearLayout) findViewById(R.id.ll_trial_back);
        ll_trial_back.setOnClickListener(clickListener);


        //添加
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ll_try_content, fragmentArray[0]);
        //提交
        transaction.commit();
        //第一次进入默认设置首页导航高亮显示
        iv_ary[0].setImageResource(R.drawable.icon_try_frist_press);
        tv_ary[0].setTextColor(getResources().getColor(R.color.app_bottom_color));

        if (flag != null && flag.equals("address")){
            selectedIndex = 3;
            changePage();
            changBackground();
            tv_trial_title.setText("我的试用");
        }

    }


    //改变页面
    private void changePage() {
        //选中的不是当前的按钮

        if (selectedIndex != currentIndex) {
            LogUtil.i("selectedIndex" + selectedIndex + "currentIndex" + currentIndex);

            transaction = getSupportFragmentManager().beginTransaction();
            Fragment showFragment = fragmentArray[selectedIndex];
            if (!showFragment.isAdded()) {
                //将选中的响应页面添加到页面
                transaction.add(R.id.ll_try_content, showFragment);
            }
            //隐藏 和显示
            transaction.hide(fragmentArray[currentIndex]);
            transaction.show(showFragment);
            //transaction.replace(R.id.ll_main_linearLayout,showFragment);
            transaction.commit();
            currentIndex = selectedIndex;
        }
    }

    private void goHomePage() {
        selectedIndex = 0;
        //改变页面
        changePage();
        //改变背景
        changBackground();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_trial_back:

                    FreeTrialAct.this.finish();

                    break;
                case R.id.ll_try_first:

                    goHomePage();
                    tv_trial_title.setText("免费试用");

                    break;
                case R.id.ll_all_try:

                    selectedIndex = 1;
                    changePage();
                    changBackground();
                    tv_trial_title.setText("全部试用");

                    break;
                case R.id.ll_apply_skills:

                    selectedIndex = 2;
                    changePage();
                    changBackground();
                    tv_trial_title.setText("申请技巧");

                    break;
                case R.id.ll_my_trial:

                    selectedIndex = 3;
                    changePage();
                    changBackground();
                    tv_trial_title.setText("我的试用");

                    break;
            }
        }
    };

    //改变背景
    private void changBackground() {

        if (selectedIndex == 0) {
            iv_ary[0].setImageResource(R.drawable.icon_try_frist_press);
            iv_ary[1].setImageResource(R.drawable.icon_try_all);
            iv_ary[2].setImageResource(R.drawable.icon_apply_skills);
            iv_ary[3].setImageResource(R.drawable.icon_my_trial);

            tv_ary[0].setTextColor(getResources().getColor(R.color.app_bottom_color));
            tv_ary[1].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[2].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[3].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
        }

        if (selectedIndex == 1) {
            iv_ary[0].setImageResource(R.drawable.icon_try_frist);
            iv_ary[1].setImageResource(R.drawable.icon_try_all_press);
            iv_ary[2].setImageResource(R.drawable.icon_apply_skills);
            iv_ary[3].setImageResource(R.drawable.icon_my_trial);

            tv_ary[0].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[1].setTextColor(getResources().getColor(R.color.app_bottom_color));
            tv_ary[2].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[3].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
        }

        if (selectedIndex == 2) {
            iv_ary[0].setImageResource(R.drawable.icon_try_frist);
            iv_ary[1].setImageResource(R.drawable.icon_try_all);
            iv_ary[2].setImageResource(R.drawable.icon_apply_skills_press);
            iv_ary[3].setImageResource(R.drawable.icon_my_trial);

            tv_ary[0].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[1].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[2].setTextColor(getResources().getColor(R.color.app_bottom_color));
            tv_ary[3].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
        }


        if (selectedIndex == 3) {
            iv_ary[0].setImageResource(R.drawable.icon_try_frist);
            iv_ary[1].setImageResource(R.drawable.icon_try_all);
            iv_ary[2].setImageResource(R.drawable.icon_apply_skills);
            iv_ary[3].setImageResource(R.drawable.icon_my_trial_press);

            tv_ary[0].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[1].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[2].setTextColor(getResources().getColor(R.color.app_trial_bottom_color));
            tv_ary[3].setTextColor(getResources().getColor(R.color.app_bottom_color));
        }
    }

    @Override
    protected void onDestroy() {
        iv_ary = null;
        tv_ary = null;
        fragmentArray = null;
        clickListener = null;
        intent = null;
        FreeTrialAct.this.finish();
        System.gc();
        this.setContentView(R.layout.empty_view);
        super.onDestroy();

    }
}
