package com.xinspace.csevent.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * 第一次使用的时候弹出的新手教程
 */
public class FirstUseGuideActivity extends Activity {
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private List<ImageView> list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use_guide);

        setView();
        setListener();
    }
    //设置监听器
    private void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                //最后一个指导页面时候,呈现一个进入按钮
                if(position==list.size()-1){

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //初始化
    private void setView() {
        viewPager= (ViewPager) findViewById(R.id.vp_first_use_viewpager);
        indicator= (CircleIndicator) findViewById(R.id.ci_first_use_indicator);

        for (int i=0;i<3;i++){
            ImageView imageView=new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.drawable.shengzhong_logo);

            list.add(imageView);
        }

        AdvViewpagerAdapter adapter=new AdvViewpagerAdapter(list);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

    }
}
