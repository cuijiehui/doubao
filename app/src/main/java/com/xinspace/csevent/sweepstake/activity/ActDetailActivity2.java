package com.xinspace.csevent.sweepstake.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.biz.AttendActivityBiz;
import com.xinspace.csevent.data.biz.GetActDetailInfoBiz;
import com.xinspace.csevent.customview.CustomAwardOpeningDialog;
import com.xinspace.csevent.data.entity.ActivityDetailEntity;
import com.xinspace.csevent.data.entity.ActivityInfoEntity;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.ActivityDetailParser;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.sweepstake.view.ScrollFootViewListener;
import com.xinspace.csevent.sweepstake.view.ScrollViewForBottom;
import com.xinspace.csevent.sweepstake.view.ScrollViewForTop;
import com.xinspace.csevent.sweepstake.view.ScrollViewListener;
import com.xinspace.csevent.sweepstake.view.VerticalPagerAdapter;
import com.xinspace.csevent.sweepstake.view.VerticalViewPager;
import com.xinspace.csevent.sweepstake.view.VerticalViewPager.OnPageChangeListener;
import com.xinspace.csevent.sweepstake.weiget.AwardFailDialog;
import com.xinspace.csevent.sweepstake.weiget.AwardSucDialog;
import com.xinspace.csevent.sweepstake.weiget.RemindDialog;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.AwardsRecordActivity;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.onekeyshare.OnekeyShare;
import me.relex.circleindicator.CircleIndicator;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;


/**
 * 普通抽奖页面
 */
public class ActDetailActivity2 extends BaseActivity implements ScrollViewListener , ScrollFootViewListener {

    private VerticalViewPager verticalViewPager;
    private ScrollViewForTop topView;
    private ScrollViewForBottom footView;
    private LayoutInflater inflater;
    private boolean isFirst = true;//第一次设置indicator
    private int screenWidth;

    private ActivityListEntity enty;
    private ViewPager vp_award_img;

    private TextView tv_time_left;//时间剩余

    private TextView tv_remaining_day;   //剩余天数
    private TextView tv_remaining_hour1; // 小时第一位
    private TextView tv_remaining_hour2; // 小时第二位
    private TextView tv_remaining_min1; // 分钟第一位
    private TextView tv_remaining_min2; // 分钟第二位
    private TextView tv_remaining_sec1; // 秒第一位
    private TextView tv_remaining_sec2; // 秒第二位
    private TextView tv_act_issue;
    private LinearLayout lin_content;


    private TextView tv_remaining_prize;//奖品剩余数量
    private TextView award_count;//中奖人数
    private TextView tv_act_title;
    private TextView tv_remark;//活动描述
    private TextView during_time;//活动时间
    private TextView tv_add;//加抽奖次数
    private TextView tv_reduce;
    private TextView tv_consume;
    private EditText et_count;//显示抽奖次数的输入框
    private ImageView iv_start;//开始抽奖
    private CircleIndicator indicator;//指示器

    private TextView tv_consume_content;


    //    private ScrollView scrollView_text;
//    private ScrollView scrollView_html;
    private WebView webView;
    private String actName = "";//活动的标题
    private LinearLayout ll_back;
    private FrameLayout state_not_start;//未开始状态
    private ImageView iv_not_start;
    private FrameLayout state_start;//点击开始抽奖

    private int count = 1;//默认抽奖次数
    private int consume;//单次抽奖需要消耗的积分
    private int totalConsume;//总共消耗积分

    private ImageView iv_share;
    private List<Object> awardList;//中奖的中奖信息实体集合
    private Dialog dialog;
    private long time_left;//剩余时间的秒数

    private int[] count_ary = new int[]{1, 3, 6, 10};//抽奖次数,四个档次
    private int flag = 0;
    private String aid;//活动id
    private CustomAwardOpeningDialog openingDialog;
    private int defaultProgress = 25;//抽奖对话框的默认进度
    private String imgUrl;
    private boolean isStartState = false;

    private int surplus_prize;
    private String cid;  // 活动ID
    private String pid;  // 奖品ID

    private AwardSucDialog awardSucDialog;
    private AwardFailDialog awardFailDialog;
    private RemindDialog remindDialog;

    TextView tv_progress;

    private String deliveryid;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int progress = msg.arg1;
                    tv_progress.setText(progress + "%");
                    break;
            }
        }
    };

    //更新时间 timerTask
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            if (time_left > 60) {
                time_left -= 1;
                final TimeLeftEntity timeEnty = new TimeLeftEntity((int) time_left);
                //主线程中更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTime(timeEnty);
                    }
                });
            }
        }
    };


    private Timer timer;
    private String is_win;
    private JSONObject obj;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail2);
        //获取该活动的数据
        getData();
        initView();

        setListener();
        //发送请求获取详细信息
        getActivityInfo();
    }

    private void initView() {

        state_not_start = (FrameLayout) findViewById(R.id.fl_state_not_start);
        iv_not_start = (ImageView) findViewById(R.id.iv_act_detail_not_start);

        int width = ScreenUtils.getScreenWidth(ActDetailActivity2.this) - ScreenUtils.dpToPx(ActDetailActivity2.this , 90);
        //int height = ScreenUtils.dpToPx(ActDetailActivity2.this , 30);
        int height = ScreenUtils.getScreenHeight(ActDetailActivity2.this) - ScreenUtils.dpToPx(ActDetailActivity2.this , 90);
        //animate(state_not_start).x(width).y(height);
        animate(state_not_start).x(30).y(height).setDuration(1).start();

        state_not_start.setVisibility(View.VISIBLE);

        verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalViewPager);
        inflater = LayoutInflater.from(this);
        topView = (ScrollViewForTop) inflater.inflate(R.layout.detali_top, null);
        topView.setScrollViewListener(this);
        footView = (ScrollViewForBottom) inflater.inflate(R.layout.detail_bottom, null);
        footView.setScrollFootViewListener(this);
        lin_content = (LinearLayout) footView.findViewById(R.id.lin_content);

        initTopView();
        verticalViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(1.0f);
                    } else if (state_start.getVisibility() == View.VISIBLE && isStartState) {
                        closeStartLayout();
                    }
                } else {
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(1.0f);
                    } else if (state_start.getVisibility() == View.VISIBLE && isStartState) {
                        closeStartLayout();
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        verticalViewPager.setAdapter(new VerticalPagerAdapter() {

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

    //处理中奖信息
    private void handleAward(String result) throws Exception {
        obj = new JSONObject(result);
        String res = obj.getString("result");
        String msg = obj.getString("msg");
        //中奖奖品名称的拼接
        sb = new StringBuilder();
        if (res.equals("200")) {//正确
            //弹出抽奖进度对话框
            if (obj.has("deliveryid")) {
                deliveryid = obj.getString("deliveryid");
            }
            showAwardOpeningDialog();
        } else if (res.equals("101")) {//金币不足
//            CustomCoinNotEnoughDialog dialog=CustomCoinNotEnoughDialog.getInstance(this);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
            showAwardNumDialog();
            return;
        } else if (res.equals("207")) { //当前期号中过奖了,不可以再抽取
            ToastUtil.makeToast(msg);
            return;
        } else if (res.equals("105")) {  //奖品已经发完
            ToastUtil.makeToast("奖品已发完");
            return;
        }
    }

    //在可以抽奖的情况下显示抽奖结果
    private void showMyAwardResult() {
        //关闭进度窗口
        if (null != openingDialog) {
            openingDialog.dismiss();
        }
        try {
            is_win = obj.getString("is_win");

            Log.i("www", "is_win" + is_win);

            if ("1".equals(is_win)) {//中奖
//                JSONArray winAry = obj.getJSONArray("winning");
//                awardList=JsonPaser.parserAry(winAry.toString(),WinningEnty.class);
//                for (int i=0;i<awardList.size();i++) {
//                    WinningEnty awardEnty = (WinningEnty) awardList.get(i);
//                    String cname=awardEnty.getCname();
//                    sb.append(cname+" ");
//                }
                showAwardSucDialog();

            } else {//未中奖
//                CustomAwardFailDialog dialog_fail= CustomAwardFailDialog.getInstance(this);
//                dialog_fail.show();
//                dialog_fail.setCanceledOnTouchOutside(false);
                showAwardFailDialog();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断中奖集合,如果不为空,则说明有中奖,空则说明没有中奖
//        if(awardList.size()>0){
//            final CustomAwardDialog dialog=CustomAwardDialog.getInstance(this);
//            dialog.show();
//            dialog.setMessage("获得\""+sb.toString()+"\"奖品");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    Intent intent = new Intent(ActDetailActivity2.this, GetAwardActivity.class);
//                    intent.putExtra("data",(ArrayList)awardList);
//                    startActivity(intent);
//                }
//            });
//        }
    }

    //抽奖成功
    private void showAwardSucDialog() {
        awardSucDialog = new AwardSucDialog(this);
        awardSucDialog.setCanceledOnTouchOutside(false);
        TextView textView1 = (TextView) awardSucDialog.getTextView1();
        TextView textView2 = (TextView) awardSucDialog.getTextView2();
        ImageView imageView = (ImageView) awardSucDialog.getImageView();
        Button but_yes = (Button) awardSucDialog.getButton2();
        but_yes.setText("领取");
        //确定
        awardSucDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getUserAddresses();
                awardSucDialog.dismiss();
            }
        });

        //取消
        awardSucDialog.setOnNegativeListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                awardSucDialog.dismiss();
            }
        });
        awardSucDialog.show();
    }


    private void showRemindDialog() {
        remindDialog = new RemindDialog(this);
        remindDialog.setCanceledOnTouchOutside(false);
        //确定
        remindDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                remindDialog.dismiss();
            }
        });
        remindDialog.show();
    }

    //获取用户地址
    public void getUserAddresses() {
        AddressManagerBiz.getAddressList( "" ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "查询地址的" + result);
                handleAddAddressResult(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    //处理用户地址返回的结果
    private void handleAddAddressResult(String result) {
        List<Object> list = JsonPaser2.parserAry(result, GetAddressEntity.class, "addresslist");
        //显示地址
        if (list.size() != 0 && list.get(0) instanceof GetAddressEntity) {
            List<GetAddressEntity> addresssList = new ArrayList<>();
            for (Object obj : list) {
                addresssList.add((GetAddressEntity) obj);
            }
//            addresses = addresssList;
//            LogUtil.i("收货地址:" + addresses.toString());
//            // ToastUtil.makeToast(addresses.toString());
//            updateListView(addresses);
            Intent intent = new Intent(ActDetailActivity2.this, PrizeDetailAct.class);
            intent.putExtra("flag", "detail");
            intent.putExtra("id", pid);
            intent.putExtra("deliveryid", deliveryid);
            intent.putExtra("match" , "");
            startActivity(intent);
        } else {//如果地址集合没东西,中奖列表界面
            Intent intent = new Intent(ActDetailActivity2.this, AwardsRecordActivity.class);
            intent.putExtra("address", "0");
            startActivity(intent);
        }
    }


    //抽奖失败
    private void showAwardFailDialog() {
        awardFailDialog = new AwardFailDialog(this);
        awardFailDialog.setCanceledOnTouchOutside(false);
        TextView textView1 = (TextView) awardFailDialog.getTextView1();
        TextView textView2 = (TextView) awardFailDialog.getTextView2();
        ImageView imageView = (ImageView) awardFailDialog.getImageView();
        textView1.setText("很遗憾没中奖...");
        textView2.setText("为什么受伤的总是我...");
        imageView.setImageResource(R.drawable.icon_award_fail);
        //确定
        awardFailDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                awardFailDialog.dismiss();
            }
        });
        awardFailDialog.show();
    }

    //抽奖积分不足
    private void showAwardNumDialog() {
        awardSucDialog = new AwardSucDialog(this);
        awardSucDialog.setCanceledOnTouchOutside(false);
        TextView textView1 = (TextView) awardSucDialog.getTextView1();
        TextView textView2 = (TextView) awardSucDialog.getTextView2();
        ImageView imageView = (ImageView) awardSucDialog.getImageView();
        textView1.setText("积分不足...");
        textView2.setText("友谊的小船说翻就翻...");
        Button but_yes = (Button) awardSucDialog.getButton2();
        but_yes.setText("充值");
        imageView.setImageResource(R.drawable.icon_award_num);
        //充值
        awardSucDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                awardSucDialog.dismiss();
            }
        });
        //取消
        awardSucDialog.setOnNegativeListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                awardSucDialog.dismiss();
            }
        });
        awardSucDialog.show();
    }

    //显示活动的信息
    private void showActivityInfo(String result) throws ParseException {
        LogUtil.i("活动详情:" + result);
        //显示奖品轮播图
        ActivityDetailEntity actEnty = ActivityDetailParser.parser(result);
        imgUrl = actEnty.getImgUrl();
        showTextDetail(actEnty);
        ActivityInfoEntity activity = actEnty.getActivity();
        aid = activity.getActivity_id();
    }

    //显示文本加图片类型的活动规则
    private void showTextDetail(ActivityDetailEntity actEnty) {
        //scrollView_text.setVisibility(View.VISIBLE);
        ActivityInfoEntity actDetail = actEnty.getActivity();
        tv_act_title.setText(actDetail.getName());


        cid = actDetail.getPid();
        pid = actDetail.getActivity_id();
        Log.i("www", "cid" + cid + "pid" + pid);


        //显示活动时间
        String start_time = actDetail.getStarttime().substring(0, 11);
        String end_time = actDetail.getEndtime().substring(0, 11);
        String time = start_time + " - " + end_time;
        during_time.setText(time);

        tv_act_issue.setText("期号：" + actDetail.getNoactivity());

        //显示描述
        tv_remark.setText(actDetail.getRemark());

        //单次抽奖消耗的积分数
        consume = Integer.valueOf(actDetail.getConsume());
        totalConsume = consume;
        tv_consume_content.setText("消耗：" + consume + "积分，" + consume + "积分/次");
        et_count.setText(String.valueOf(totalConsume));

//        tv_consume.setText(String.valueOf(consume));
//        LogUtil.i("本活动单次抽奖消耗的积分:"+consume);

        //中奖数和剩余奖数
        surplus_prize = Integer.valueOf(actDetail.getSurplus_prize());
        award_count.setText(String.valueOf(actDetail.getWinners()));
        tv_remaining_prize.setText(String.valueOf(actDetail.getSurplus_prize()));

        //显示剩余时间
        time_left = actDetail.getSurplus_time();
        TimeLeftEntity timeEnty = new TimeLeftEntity((int) time_left);
        updateTime(timeEnty);

        final List<String> imgUrlList = actEnty.getThumbnailList();
        for (int i = 0; i < imgUrlList.size(); i++) {
            ImageView imageView = new ImageView(this);
            lin_content.addView(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.height = (int) (screenWidth * 1.0f);
            params.width = (int) (screenWidth * 1.0f);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            ImagerLoaderUtil.displayImage(imgUrlList.get(i), imageView);
        }

        timer = new Timer();
        //timer.schedule(timeTask , 0 , 1000 * 60);
        timer.schedule(timeTask, 0, 1000);


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
        vp_award_img.setAdapter(adapter);

        if (isFirst) {
            isFirst = false;
            indicator.setViewPager(vp_award_img);
            //滚动广告
            timer.schedule(timeTask, 5000, 5000);
        }

        //显示活动剩余时间,剩余奖品和中奖人数
        //showAwardInfo(actDetail);

    }

    //更新剩余时间
    private void updateTime(TimeLeftEntity timeEnty) {

        //Log.i("www" , "活动剩余秒:" + time_left);
        String day = timeEnty.getDays();
        String hours = timeEnty.getHoursWithDay();
        String mins = timeEnty.getMinWithDay();
        String sec = timeEnty.getSecondsWithDay();

        //Log.i("www" , "day" + day + "hours" + hours+ "mins" + mins + "sec" + sec);

        if (day.length() == 1) {
            day = "0" + day;
            tv_remaining_day.setText(day + "天");
        } else {
            tv_remaining_day.setText(day + "天");
        }

        if (hours.length() == 1) {
            tv_remaining_hour1.setText("0");
            tv_remaining_hour2.setText(hours);
        } else {
            tv_remaining_hour1.setText(hours.substring(0, 1));
            tv_remaining_hour2.setText(hours.substring(1, 2));
        }
        if (mins.length() == 1) {
            tv_remaining_min1.setText("0");
            tv_remaining_min2.setText(mins);
        } else {
            tv_remaining_min1.setText(mins.substring(0, 1));
            tv_remaining_min2.setText(mins.substring(1, 2));
        }

        if (sec.length() == 1) {
            tv_remaining_sec1.setText("0");
            tv_remaining_sec2.setText(sec);
        } else {
            tv_remaining_sec1.setText(sec.substring(0, 1));
            tv_remaining_sec2.setText(sec.substring(1, 2));
        }

//        LogUtil.i("day:" + day);
//        LogUtil.i("hour:" + hours);
//        LogUtil.i("min:" + mins);
        //tv_time_left.setText(day + "天" + hours + "小时" + mins + "分");
    }

    //获取活动的详细信息
    private void getActivityInfo() {
        //创建一个loading的提示框
        dialog = DialogUtil.createLoadingDialog(ActDetailActivity2.this, "");
        dialog.show();
        //dialog.setCanceledOnTouchOutside(true);
        GetActDetailInfoBiz.getDetail(enty, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    Log.i("www", "活动详情" + result);
                    //活动详细信息部分
                    dialog.cancel();//有数据返回的时候让dialog消失
                    showActivityInfo(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //获取首页传递过来的数据
    private void getData() {
        Intent intent = getIntent();
        enty = (ActivityListEntity) intent.getSerializableExtra("data");
        if (enty.getState() == 4) {
            showRemindDialog();
        }

    }


    //设置监听器
    private void setListener() {
        //滚动网页
        vp_award_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("www", "www---vp_award_img---up----------");
                    //showAttendAwardButton();
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(1.0f);
                    } else if (state_start.getVisibility() == View.VISIBLE && isStartState) {
                        closeStartLayout();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("www", "www---vp_award_img--down----------");
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(0.5f);
                    } else if (state_start.getVisibility() == View.VISIBLE) {

                    }
                }
                return false;
            }
        });


        vp_award_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("www", "www---vp_award_img---up----------");
                    //showAttendAwardButton();
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(1.0f);
                    } else if (state_start.getVisibility() == View.VISIBLE) {
                        closeStartLayout();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("www", "www---vp_award_img--down----------");
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(0.5f);
                    } else if (state_start.getVisibility() == View.VISIBLE) {

                    }
                }
                return false;
            }
        });


        topView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("www", "www---topView---up----------");
                    //showAttendAwardButton();
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(1.0f);
                    } else if (state_start.getVisibility() == View.VISIBLE && isStartState) {
                        closeStartLayout();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("www", "www--topView---down----------");
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(0.5f);
                    } else if (state_start.getVisibility() == View.VISIBLE) {

                    }
                }
                return false;
            }
        });

        footView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("www", "www---footView---up----------");
                    //showAttendAwardButton();
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(1.0f);
                    } else if (state_start.getVisibility() == View.VISIBLE && isStartState) {
                        closeStartLayout();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("www", "www---footView--down----------");
                    if (state_not_start.getVisibility() == View.VISIBLE) {
                        state_not_start.setAlpha(0.5f);
                    } else if (state_start.getVisibility() == View.VISIBLE) {

                    }
                }
                return false;
            }
        });


        //滚动文本
//        scrollView_text.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction()==MotionEvent.ACTION_UP){
//                    showAttendAwardButton();
//                }
//                return false;
//            }
//        });

        //分享
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        //增加抽奖次数
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("www", "consume=" + consume);
                totalConsume += consume;
                et_count.setText(String.valueOf(totalConsume));

            }
        });
        //减少抽奖次数
        tv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalConsume != 0 && totalConsume > 0) {
                    totalConsume -= consume;
                }
                Log.i("www", "consume=" + consume);
                et_count.setText(String.valueOf(totalConsume));
            }
        });
        //后退
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //开始抽奖
        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CoresunApp.USER_ID == null) {
                    ToastUtil.makeToast("请先登录");
                    startActivity(new Intent(ActDetailActivity2.this, LoginActivity.class));
                    return;
                }
                attendActivity();
                closeStartLayout();
            }
        });

        //将抽奖按钮以动画的形式弹出

        iv_not_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("www", "点击点击点击点击了点击");
                isStartState = true;
                state_start.setVisibility(View.VISIBLE);
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();//屏幕高度
                LogUtil.i("高度:" + height + "/宽度:" + wm.getDefaultDisplay().getWidth());
                TranslateAnimation animation = new TranslateAnimation(0f, 0f, height, 0f);//参数是指调用的控件的参数,并非屏幕的参数
                animation.setDuration(500);
                state_start.setAnimation(animation);
                animation.startNow();


                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
                alphaAnimation.setDuration(300);
                state_not_start.setAnimation(alphaAnimation);
                state_not_start.setVisibility(View.GONE);
                iv_not_start.setClickable(false);

            }
        });


//        iv_not_start.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                state_start.setVisibility(View.VISIBLE);
//                //布局高度
//                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//                int height = wm.getDefaultDisplay().getHeight();//屏幕高度
//                LogUtil.i("高度:"+height+"/宽度:"+wm.getDefaultDisplay().getWidth());
//
//                TranslateAnimation animation = new TranslateAnimation(0f,0f,height,0f);//参数是指调用的控件的参数,并非屏幕的参数
//                animation.setDuration(500);
//                AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f, 0f);
//                alphaAnimation.setDuration(500);
//                state_not_start.setAnimation(alphaAnimation);
//                state_not_start.setVisibility(View.GONE);
//                state_start.setAnimation(animation);
//                animation.startNow();
//
//                return false;
//            }
//        });
    }


    private void closeStartLayout() {
        isStartState = false;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();//屏幕高度
        LogUtil.i("高度:" + height + "/宽度:" + wm.getDefaultDisplay().getWidth());
        TranslateAnimation animation = new TranslateAnimation(0f, 0f, 0f, height);//参数是指调用的控件的参数,并非屏幕的参数
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(500);
        state_start.setAnimation(animation);
        animation.startNow();
        state_start.setVisibility(View.GONE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(300);
        state_not_start.setAnimation(alphaAnimation);
        state_not_start.setVisibility(View.VISIBLE);
        iv_not_start.setClickable(true);
    }


    //点击抽奖
    private void attendActivity() {


        Log.i("www", "点击抽奖后的surplus_prize" + surplus_prize);
        if (surplus_prize == 0) {
            ToastUtil.makeToast("该活动的奖品已经发完");
            return;
        }

//        //活动已经结束
//        int state = enty.getState();
//        if(state==1){
//            ToastUtil.makeToast("该活动已经结束");
//            return;
//        }else if(state==2){
//            //未开始
//            ToastUtil.makeToast("活动未开始");
//            return;
//        }else if(state==4){
//            ToastUtil.makeToast("该活动的奖品已经发完");
//            return;
//        }

        //调用抽奖接口
        //getAwardResult();
        checkStockGet();
        //设置抽奖按钮不可用
        //iv_start.setClickable(false);
    }


    /**
     * 第一次拉取库存
     */
    private void checkStockGet() {
        //第一次拉取库存
        AttendActivityBiz.checkStockGet(this, cid, pid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    Log.i("www", "第一次拉取库存result" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("YES")) {
                            checkStockSet();
                        } else {
                            ToastUtil.makeToast("该活动的奖品已经发完");
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    /**
     * 第二次拉取库存
     */
    private void checkStockSet() {
        //第一次拉取库存
        String uid = CoresunApp.USER_ID;
        AttendActivityBiz.checkStockSet(this, cid, pid, uid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    Log.i("www", "第二次拉取库存result" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("yes")) {
                            getAwardResult();
                        } else {
                            ToastUtil.makeToast("该活动的奖品已经发完");
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    //获取抽奖结果
    private void getAwardResult() {
        //参与抽奖接口
        AttendActivityBiz.attendByGeneral(this, pid, CoresunApp.USER_ID, totalConsume, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    Log.i("www", "抽奖result" + result);
                    if (result == null && result.equals("")){
                        return;
                    }

                    //等待抽奖进度完成之后再显示结果
                    handleAward(result);
                    //设置抽奖按钮可用
                    iv_start.setClickable(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //显示抽奖进度对话框
    private void showAwardOpeningDialog() {
        openingDialog = CustomAwardOpeningDialog.getInstance(this);
        openingDialog.setCanceledOnTouchOutside(false);
        openingDialog.show();

        tv_progress = openingDialog.getTextView();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (defaultProgress > 100) {
                    timer.cancel();
                    defaultProgress = 0;
                    //获取抽奖结果
//                    getAwardResult();
                    runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            showMyAwardResult();
                        }
                    });
                    return;
                }
                openingDialog.setProgress(defaultProgress);
                handler.obtainMessage(1, defaultProgress, defaultProgress).sendToTarget();
                defaultProgress += 1;
            }
        }, 0, 20);
    }

    //初始化组件
    private void initTopView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_award_detail_back);

        screenWidth = ScreenUtils.getScreenWidth(this);
        vp_award_img = (ViewPager) topView.findViewById(R.id.vp_act_detail_viewPager);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vp_award_img.getLayoutParams();
        params.height = (int) (screenWidth * 1.0f);
        params.width = (int) (screenWidth * 1.0f);
        vp_award_img.setLayoutParams(params);
//        scrollView_text = (ScrollView) findViewById(R.id.scrollView_text);
//        scrollView_html = (ScrollView) findViewById(R.id.scrollView_html);

        webView = (WebView) footView.findViewById(R.id.wv_act_detail_webview);
        indicator = (CircleIndicator) topView.findViewById(R.id.indicator);
        tv_act_title = (TextView) topView.findViewById(R.id.tv_detail_act_title);
        tv_remaining_day = (TextView) topView.findViewById(R.id.tv_remaining_day);
        tv_remaining_hour1 = (TextView) topView.findViewById(R.id.tv_remaining_hour1);
        tv_remaining_hour2 = (TextView) topView.findViewById(R.id.tv_remaining_hour2);
        tv_remaining_min1 = (TextView) topView.findViewById(R.id.tv_remaining_min1);
        tv_remaining_min2 = (TextView) topView.findViewById(R.id.tv_remaining_min2);
        tv_remaining_sec1 = (TextView) topView.findViewById(R.id.tv_remaining_sec1);
        tv_remaining_sec2 = (TextView) topView.findViewById(R.id.tv_remaining_sec2);
        tv_remaining_prize = (TextView) topView.findViewById(R.id.tv_remaining_prize);
        award_count = (TextView) topView.findViewById(R.id.tv_person);
        tv_remark = (TextView) topView.findViewById(R.id.tv_detail_act_indouct);
        during_time = (TextView) topView.findViewById(R.id.tv_act_during_time);
        tv_act_issue = (TextView) topView.findViewById(R.id.tv_act_issue);


        //tv_time_left= (TextView) findViewById(R.id.tv_detail_time_left);
        tv_add = (TextView) findViewById(R.id.tv_detail_add);
        tv_reduce = (TextView) findViewById(R.id.tv_detail_reduce);
        tv_consume = (TextView) findViewById(R.id.tv_act_detail_consume);
        iv_start = (ImageView) findViewById(R.id.iv_detail_start_award);
        et_count = (EditText) findViewById(R.id.et_detail_count);
        iv_share = (ImageView) findViewById(R.id.iv_act_detail_share);

        state_start = (FrameLayout) findViewById(R.id.fl_state_start);
        tv_consume_content = (TextView) findViewById(R.id.tv_consume_content);
    }


    //显示分享
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题：微信、QQ（新浪微博不需要标题）
        oks.setTitle("晟中活动 " + actName);  //最多30个字符

        // text是分享文本：所有平台都需要这个字段
        oks.setText(actName + "  http://www.coresun.net");  //最多40个字符

        // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
        //oks.setImagePath(Environment.getExternalStorageDirectory() + "/ic_launcher.png");//确保SDcard下面存在此张图片

        //网络图片的url：所有平台(需要设置此内容微信分享中才能点击链接)
        oks.setImageUrl("https://app.coresun.net/shide/assets/admin/layout/img/ic_launcher.png");//网络图片rul

        // url：仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://app.coresun.net/shide/wx_user/index_active?aid=" + aid);   //网友点进链接后，可以看到分享的详情

        // Url：仅在QQ空间使用
        oks.setTitleUrl("http://www.coresun.net");  //网友点进链接后，可以看到分享的详情

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //将倒计时取消
        if (null != timer) {
            timer.cancel();
        }
        timer = null;
        timeTask = null;
    }



    //监听顶部Scroll 滚动
    @Override
    public void onScrollChanged(ScrollViewForTop scrollView, int x, int y, int oldx, int oldy) {
        //Log.i("www", "顶部x" + x + "y" + y + "oldx" + oldx + "oldy" + oldy);
        if (y != oldy){

        }
    }



    //监听部Scroll 滚动
    @Override
    public void onFootScrollChanged(ScrollViewForBottom scrollView, int x, int y, int oldx, int oldy) {
        //Log.i("www", "底部x" + x + "y" + y + "oldx" + oldx + "oldy" + oldy);
        if (y != oldy){

        }
    }

}
