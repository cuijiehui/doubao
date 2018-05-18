package com.xinspace.csevent.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.adapter.GuidePagerAdpter;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.MainActivity;

/**
 * Created by Android on 2016/9/19.
 *
 * app 新功能引导页
 *
 */
public class GuideAct extends Activity{

    private ViewPager pager;
    private String[] imgSrcs = new String[] { "guide_image1.png", "guide_image2.png", "guide_image3.png",};
    private GuidePagerAdpter adpter;
    private Button skipBt;
    private View dot1;
    private View dot2;
    private View dot3;

    private TextView intorduceText;
    private Button btExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_guide);
        guideViewInit();

    }

    private void guideViewInit() {

        pager = (ViewPager) findViewById(R.id.act_guide_pager);
        pager.setOnPageChangeListener(changeListener);
        adpter = new GuidePagerAdpter(imgSrcs, this);
        pager.setAdapter(adpter);

        skipBt = (Button) findViewById(R.id.bt_skip);
        btExperience = (Button) findViewById(R.id.bt_experience);

        skipBt.setOnClickListener(clickListener);
        btExperience.setOnClickListener(clickListener);

        intorduceText = (TextView) findViewById(R.id.text_introduce);

        dot1 = (View) findViewById(R.id.v_dot0);
        dot2 = (View) findViewById(R.id.v_dot1);
        dot3 = (View) findViewById(R.id.v_dot2);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_skip:
                    gotoHome();
                    break;
                default:
                    gotoHome();
                    break;
            }
        }
    };

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            dot1.setBackgroundResource(R.drawable.guide_dot_normal);
            dot2.setBackgroundResource(R.drawable.guide_dot_normal);
            dot3.setBackgroundResource(R.drawable.guide_dot_normal);
            switch (arg0) {
                case 0:
                    dot1.setBackgroundResource(R.drawable.guide_dot_select);
                    skipBt.setVisibility(View.VISIBLE);
                    intorduceText.setVisibility(View.GONE);
                    btExperience.setVisibility(View.GONE);
                    break;
                case 1:
                    dot2.setBackgroundResource(R.drawable.guide_dot_select);
                    skipBt.setVisibility(View.VISIBLE);
                    intorduceText.setVisibility(View.GONE);
                    btExperience.setVisibility(View.GONE);
                    break;
                case 2:
                    dot3.setBackgroundResource(R.drawable.guide_dot_select);
                    skipBt.setVisibility(View.GONE);
                    intorduceText.setVisibility(View.GONE);
                    btExperience.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    protected void gotoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        clickListener = null;
        changeListener = null;
        imgSrcs = null;
        super.onDestroy();
    }

}
