package com.xinspace.csevent.publish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.view.CircleImageView;
import com.xinspace.csevent.publish.model.PublishBean;
import com.xinspace.csevent.sweepstake.view.ScrollFootViewListener;
import com.xinspace.csevent.sweepstake.view.ScrollViewForBottom;
import com.xinspace.csevent.sweepstake.view.ScrollViewForTop;
import com.xinspace.csevent.sweepstake.view.ScrollViewListener;
import com.xinspace.csevent.sweepstake.view.VerticalPagerAdapter;
import com.xinspace.csevent.sweepstake.view.VerticalViewPager;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Android on 2016/10/20.
 */
public class PublishDetailAct extends BaseActivity implements ScrollViewListener, ScrollFootViewListener {

    private Intent intent;
    private PublishBean publishBean;

    private VerticalViewPager vp_publish_detail;
    private ScrollViewForTop topView;
    private ScrollViewForBottom footView;
    private LayoutInflater inflater;
    private LinearLayout ll_back;
    private int screenWidth;
    private ViewPager viewPager_publish_detail;
    private LinearLayout lin_content;
    private CircleIndicator indicator;//指示器

    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private Timer timer=new Timer();
    private boolean isFirst=true;//第一次设置indicator

    private List<String> imgUrlList; // 广告图地址

    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:

                    break;
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
            }
        }
    };

    private TextView tv_goods_title;

    private CircleImageView iv_win_user;
    private TextView tv_win_user_name;
    private TextView tv_win_user_num;
    private TextView tv_win_user_id;
    private TextView tv_win_noactivity;
    private TextView tv_win_rule;
    private TextView tv_win_match;
    private TextView tv_win_time;

    private TextView tv_go_crowd;

    private RelativeLayout rel_old_act ;
    private RelativeLayout rel_part_record;

    //设置广告页
    private void scrollAdv() {
        if(scroll_flag){
            int currentIndex = viewPager_publish_detail.getCurrentItem();
            if(currentIndex == imgUrlList.size() - 1){
                currentIndex=0;
            }else{
                currentIndex+=1;
            }
            viewPager_publish_detail.setCurrentItem(currentIndex,false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_publish_detail);
        initView();

    }

    private void initView() {

        intent = getIntent();
        if (intent != null){
            publishBean = (PublishBean) intent.getSerializableExtra("enty");
        }
        inflater = LayoutInflater.from(PublishDetailAct.this);
        vp_publish_detail = (VerticalViewPager) findViewById(R.id.vp_publish_detail);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        topView = (ScrollViewForTop) inflater.inflate(R.layout.fragment_pd_top, null);
        topView.setScrollViewListener(this);
        footView = (ScrollViewForBottom) inflater.inflate(R.layout.fragment_pd_bottom, null);
        footView.setScrollFootViewListener(this);

        initTopView();

        vp_publish_detail.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {

                } else {

                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp_publish_detail.setAdapter(new VerticalPagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = null;
                switch (position) {
                    case 0:
                        view = topView;
                        break;
                    case 1:
                        view = footView;
                        break;
                    default:
                        break;
                }
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

    }

    private void initTopView() {

        imgUrlList = new ArrayList<String>();
        imgUrlList.add(publishBean.getThumbnail());

        screenWidth = ScreenUtils.getScreenWidth(this);
        viewPager_publish_detail = (ViewPager) topView.findViewById(R.id.viewPager_publish_detail);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewPager_publish_detail.getLayoutParams();
        params.height = (int) (screenWidth * 1.0f);
        params.width = (int) (screenWidth * 1.0f);
        viewPager_publish_detail.setLayoutParams(params);
        indicator = (CircleIndicator) topView.findViewById(R.id.indicator);

        rel_old_act = (RelativeLayout) topView.findViewById(R.id.rel_old_act);
        rel_old_act.setOnClickListener(clickListener);

        rel_part_record = (RelativeLayout) topView.findViewById(R.id.rel_part_record);
        rel_part_record.setOnClickListener(clickListener);

        tv_goods_title = (TextView) topView.findViewById(R.id.tv_goods_title);

        iv_win_user = (CircleImageView) topView.findViewById(R.id.iv_win_user);
        tv_win_user_name = (TextView) topView.findViewById(R.id.tv_win_user_name);
        tv_win_user_num = (TextView) topView.findViewById(R.id.tv_win_user_num);
        tv_win_user_id = (TextView) topView.findViewById(R.id.tv_win_user_id);
        tv_win_noactivity = (TextView) topView.findViewById(R.id.tv_win_noactivity);
        tv_win_rule = (TextView) topView.findViewById(R.id.tv_win_rule);
        tv_win_rule.setOnClickListener(clickListener);

        tv_win_match = (TextView) topView.findViewById(R.id.tv_win_match);
        tv_win_time = (TextView) topView.findViewById(R.id.tv_win_time);

        tv_goods_title.setText(publishBean.getName());
        if (publishBean.getUser_thumbnail() != null && !publishBean.getUser_thumbnail().equals("")){
            ImagerLoaderUtil.displayImageWithLoadingIcon( publishBean.getUser_thumbnail() , iv_win_user , R.drawable.loading_icon);
        }else{
            iv_win_user.setImageResource(R.drawable.loading_icon);
        }

        tv_win_user_name.setText("获奖者： " + publishBean.getUsername());
        tv_win_user_num.setText("幸运号码： " + publishBean.getLottery_number());
        tv_win_user_id.setText("用户ID： " + publishBean.getUid());

        tv_win_noactivity.setText("期号： " + publishBean.getNoactivity());
        tv_win_match.setText("参与次数： " + publishBean.getNumber_participation());
        tv_win_time.setText("中奖时间： " + publishBean.getWintime());


        List<ImageView> list = new ArrayList<>();
        for (int i = 0; i < imgUrlList.size(); i++) {
            String url = imgUrlList.get(i);
            //显示广告图片
            final ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            ImagerLoaderUtil.displayImage(url, image);
            list.add(image);
        }
        AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(list);
        viewPager_publish_detail.setAdapter(adapter);
        if(isFirst){
            isFirst=false;
            indicator.setViewPager(viewPager_publish_detail);
            //滚动广告
            timer.schedule(timeTask,3000,3000);
        }

        tv_go_crowd = (TextView) footView.findViewById(R.id.tv_go_crowd);
        tv_go_crowd.setOnClickListener(clickListener);

        lin_content = (LinearLayout) footView.findViewById(R.id.lin_content);
        for (int i = 0; i < imgUrlList.size(); i++) {
            ImageView imageView = new ImageView(this);
            lin_content.addView(imageView);
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params1.height = (int) (screenWidth * 1.0f);
            params1.width = (int) (screenWidth * 1.0f);
            imageView.setLayoutParams(params1);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            ImagerLoaderUtil.displayImage(imgUrlList.get(i), imageView);
        }

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:
                    finish();
                    break;
                case R.id.tv_win_rule: //中奖规则
                    Intent intent = new Intent(PublishDetailAct.this , CrowdRuleAct.class);
                    intent.putExtra("publishBean" , publishBean);
                    startActivity(intent);
                    break;
                case R.id.tv_go_crowd: //前往抽奖

                    break;
                case R.id.rel_old_act: // 往期
                    Intent intent1 = new Intent(PublishDetailAct.this , CrowdActivitiesAct.class);
                    intent1.putExtra("cid" , publishBean.getPid());
                    startActivity(intent1);
                    break;
                case R.id.rel_win_share: // 晒单

                    break;
                case R.id.rel_part_record: // 抽奖记录
                    Intent intent2 = new Intent(PublishDetailAct.this , CrowdParticipaAct.class);
                    intent2.putExtra("aid" , publishBean.getActiveid());
                    startActivity(intent2);
                    break;
            }
        }
    };

    @Override
    public void onFootScrollChanged(ScrollViewForBottom scrollView, int x, int y, int oldx, int oldy) {

    }

    @Override
    public void onScrollChanged(ScrollViewForTop scrollView, int x, int y, int oldx, int oldy) {

    }
}
