package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.data.entity.AwardInfoEntity;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * 抽奖页面点击图片后放大图片的页面
 */
public class ZoomInImageActivity extends BaseActivity {
    private ViewPager viewpager;
    private CircleIndicator indicator;
    private List<AwardInfoEntity> entyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_in_image);
        setView();
        getData();
        setAdapter();
    }
    //设置显示奖品图片的Adapter
    private void setAdapter() {
        List<ImageView> list=new ArrayList<>();
        for (int i=0;i<entyList.size();i++){
            AwardInfoEntity enty = entyList.get(i);
            String url=enty.getList_image();
            //显示广告图片
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            ImagerLoaderUtil.displayImage(url, image);
            list.add(image);

            //设置点击图片后关闭activity
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        AdvViewpagerAdapter adapter=new AdvViewpagerAdapter(list);

        viewpager.setAdapter(adapter);
        //设置下标指示器
        indicator.setViewPager(viewpager);
    }
    //获取上个activity传递过来的数据
    private void getData() {
        Intent intent=getIntent();
        entyList = (List<AwardInfoEntity>) intent.getSerializableExtra("data");
    }
    //初始化组件
    private void setView() {
        indicator= (CircleIndicator) findViewById(R.id.zoom_in_indicator);
        viewpager= (ViewPager) findViewById(R.id.zoom_in_viewpager);
    }
}
