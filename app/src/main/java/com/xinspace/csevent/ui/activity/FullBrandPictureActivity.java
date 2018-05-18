package com.xinspace.csevent.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.data.biz.GetAdvertisementIconBiz;
import com.xinspace.csevent.data.entity.BrandEntity;
import com.xinspace.csevent.data.entity.BrandScrollPicEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import me.relex.circleindicator.CircleIndicator;

public class FullBrandPictureActivity extends Activity {
    private CircleIndicator indicator;
    private ViewPager viewPager;
    private BrandEntity enty;
    private LinearLayout ll_back;
    private List<Object> pic_list;
    private TextView brandName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_brand_picture);

        setView();
        setListener();
        getData();
        getBrandPicture();
    }
    //获取上个页面的数据
    private void getData() {
        Intent intent=getIntent();
        enty= (BrandEntity) intent.getSerializableExtra("data");

        brandName.setText(enty.getName());
    }
    //获取品牌轮播图
    private void getBrandPicture() {
        GetAdvertisementIconBiz.getScrollBrandPic(this, enty.getId(), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("滚动图:"+result);
                showScrollPic(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示轮播图
    private void showScrollPic(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            String res = obj.getString("result");

            if(res.equals("200")){
                JSONArray ary = obj.getJSONArray("data");
                pic_list=JsonPaser.parserAry(ary.toString(), BrandScrollPicEntity.class);
                List<ImageView> iv_list=new ArrayList<>();
                for (int i=0;i<pic_list.size();i++){
                    BrandScrollPicEntity pic_enty = (BrandScrollPicEntity) pic_list.get(i);
                    String url=pic_enty.getImg_url();
                    //显示广告图片
                    ImageView image = new ImageView(this);
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    ImagerLoaderUtil.displayImage(url,image);

                    iv_list.add(image);
                }
                AdvViewpagerAdapter adapter=new AdvViewpagerAdapter(iv_list);
                viewPager.setAdapter(adapter);
                indicator.setViewPager(viewPager);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //监听器
    private void setListener() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //初始化
    private void setView() {
        viewPager= (ViewPager) findViewById(R.id.vp_brand_viewPager);
        indicator= (CircleIndicator) findViewById(R.id.ci_brand_indicator);
        ll_back= (LinearLayout) findViewById(R.id.ll_brand_back);
        brandName= (TextView) findViewById(R.id.tv_fullpage_brand_name);
    }
}
