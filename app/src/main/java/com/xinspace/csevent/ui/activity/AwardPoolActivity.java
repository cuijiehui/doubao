package com.xinspace.csevent.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.AttendActivityBiz;
import com.xinspace.csevent.data.biz.GetActDetailInfoBiz;
import com.xinspace.csevent.customview.CustomAwardDialog;
import com.xinspace.csevent.customview.CustomAwardFailDialog;
import com.xinspace.csevent.customview.CustomPoolAwardDialog;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.PoolWinningEnty;
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.data.entity.WinningEnty;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 抽奖池抽奖页面
 */
public class AwardPoolActivity extends BaseActivity {
    private ActivityListEntity enty;
    private Dialog dialog;
    private LinearLayout ll_back;
    private TextView title;//活动名称
    private TextView timeLeft;//活动剩余时间
    private TextView awardLeft;//剩余奖品
    private TextView awardCount;//中奖人数
    private TextView awardList;//奖品列表
    private TextView awardTime;//活动时间
    private TextView awardRule;//活动规则
    private TextView consume;//消耗积分数

    private ScrollView scrollView;
    private FrameLayout notStart;//未开始抽奖状态
    private FrameLayout started;//开始抽奖
    private ImageView animation;
    private ImageView iv_started;//抽奖按钮
    private ImageView iv_not_start;//未抽奖按钮


    private String resultStr = null;//抽奖结果
    private int count = 0;//计数
    private static final int PICUTURE_COUNT = 44;//动画帧数

    private static final int HANDLER_CHANGE_PICTURE = 100;
    private static final int HANDLER_SHOW_AWARD_RESULT = 101;
    private boolean noResult=false;//没有结果返回

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==HANDLER_CHANGE_PICTURE){
                animation.setImageResource(picAry[count]);
            }else if(msg.what==HANDLER_SHOW_AWARD_RESULT){
                try {
                    iv_started.setClickable(true);
                    handleAward(resultStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_pool);

        getData();
        setViews();
        setListener();
        getActivityInfo();
    }
    //scrollView滚动
    private void haveMoveToBottomForText() {
        View childView = scrollView.getChildAt(0);
        //为了用户滚动到接近底部时,即显示按钮,在高度后加10
        if (childView != null && childView.getMeasuredHeight() <= (scrollView.getScrollY() + scrollView.getHeight()) + 30) {
            LogUtil.i("已经滚动到底部了");
            if (started.getVisibility() == View.VISIBLE) {
                return;
            }
            showAttendAwardButton();
        }
    }
    //显示抽奖按钮
    private void showAttendAwardButton() {
        //如果抽奖按钮已经显示了,则return
        if (started.getVisibility() == View.VISIBLE) {
            return;
        }
        if (notStart.getVisibility() == View.GONE) {
            notStart.setVisibility(View.VISIBLE);
        }
    }
    //获取活动详细信息
    private void getActivityInfo() {
        //创建一个loading的提示框
        dialog = DialogUtil.createLoadingDialog(AwardPoolActivity.this, "");
        dialog.show();
        //dialog.setCanceledOnTouchOutside(true);
        GetActDetailInfoBiz.getDetail(enty, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                //活动详细信息部分
                dialog.cancel();//有数据返回的时候让dialog消失
                showActivityInfo(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示活动详细信息
    private void showActivityInfo(String result) {
//        LogUtil.i("抽奖池抽奖:" + result);
//        ActivityDetailEntity actEnty = ActivityDetailParser.parser(result);
//        //活动时间和中奖人数
//        ActivityInfoEntity activity = actEnty.getActivity();
//        long surplus_time = activity.getSurplus_time();
//        TimeLeftEntity timeEnty = new TimeLeftEntity((int) surplus_time);
//        updateTime(timeEnty);
//
//        int winners = activity.getWinners();
//        awardCount.setText(String.valueOf(winners));
//
//        int award_left = activity.getSurplus_prize();
//        awardLeft.setText(String.valueOf(award_left));
//
//        //消耗积分
//        float cons = activity.getConsume();
//        consume.setText(String.valueOf((int) cons));
//
//        //活动时间
//        String start_time = activity.getStarttime().substring(0, 11);
//        String end_time = activity.getEndtime().substring(0, 11);
//        String time = start_time + " - " + end_time;
//        awardTime.setText(time);
//
//        //活动规则
//        String remark = activity.getRemark();
//        awardRule.setText(remark);
//
//        //显示奖品列表,活动名称
//        List<AwardInfoEntity> actList = actEnty.getActivity_list();
//        StringBuilder actName = new StringBuilder();
//        for (int i = 0; i < actList.size(); i++) {
//            AwardInfoEntity enty = actList.get(i);
//            String name = enty.getName();
//            actName.append(name);
//            if (!(i == actList.size() - 1)) {
//                actName.append(",");
//            }
//        }
//        title.setText(actName.toString());
//        awardList.setText(actName.toString());

    }
    //更新剩余时间
    private void updateTime(TimeLeftEntity timeEnty) {
        String day = timeEnty.getDays();
        String hours = timeEnty.getHoursWithDay();
        String mins = timeEnty.getMinWithDay();
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        if (mins.length() == 1) {
            mins = "0" + mins;
        }
        LogUtil.i("day:" + day);
        LogUtil.i("hour:" + hours);
        LogUtil.i("min:" + mins);
        timeLeft.setText(day + "天" + hours + "小时" + mins + "分");
    }
    //设置监听器
    private void setListener() {
        //开始抽奖
        iv_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CoresunApp.USER_ID == null) {
                    ToastUtil.makeToast("请先登录");
                    startActivity(new Intent(AwardPoolActivity.this, LoginActivity.class));
                    return;
                }
                //scrollview滚动到最上面
                scrollView.scrollTo(0, 0);
                attendActivity();
            }
        });
        //未开始抽奖按钮
        iv_not_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notStart.setVisibility(View.GONE);
                started.setVisibility(View.VISIBLE);
                //布局高度
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();//屏幕高度
                LogUtil.i("高度:" + height + "/宽度:" + wm.getDefaultDisplay().getWidth());

                TranslateAnimation animation = new TranslateAnimation(0f, 0f, height, 0f);//参数是指调用的控件的参数,并非屏幕的参数
                animation.setDuration(500);
                started.setAnimation(animation);
                animation.startNow();
            }
        });
        //返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //scrollview
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showAttendAwardButton();
                }
                return false;
            }
        });
    }
    //开始抽奖
    private void attendActivity() {
        //活动已经结束
        int state = enty.getState();
        if (state == 1) {
            ToastUtil.makeToast("该活动已经结束");
            return;
        } else if (state == 2) {
            //未开始
            ToastUtil.makeToast("活动未开始");
            return;
        } else if (state == 4) {
            ToastUtil.makeToast("该活动的奖品已经发完");
            return;
        }

        //使抽奖按钮不可用
        iv_started.setClickable(false);

        //执行抽奖的动画
        startAwardAnimation();
        //抽奖之前,将成员resultStr结果置空
        resultStr = null;

        AttendActivityBiz.attendByPool(this, enty, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                resultStr = result;
                //当动画结束后没有结果返回时,则等待,当结果返回时,通知显示结果
                if(noResult){
                    iv_started.setClickable(true);
                    handleAward(result);
                    noResult=false;
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //抽奖动画
    private void startAwardAnimation() {
        //开始动画前滚动到顶部
        scrollView.scrollTo(0,0);

        //每隔一小段时间切换一张图片
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtil.i("i="+count);
                if (count == PICUTURE_COUNT - 1) {
                    timer.cancel();
                    count = 0;
                    //动画结束后再处理结果
                    handler.sendEmptyMessage(HANDLER_SHOW_AWARD_RESULT);
                } else {
                    handler.sendEmptyMessage(HANDLER_CHANGE_PICTURE);
                    count++;
                }
            }
        }, 0, 50);
    }
    //处理抽奖数据
    private void handleAward(String result) throws JSONException {
        //如果抽奖结果还没有返回,则等待
        if (null == result) {
            ToastUtil.makeToast("等待结果中...");
            noResult=true;
            return;
        }
        JSONObject obj = new JSONObject(result);
        String res = obj.getString("result");
        if (res.equals("200")) {
            String is_win = obj.getString("is_win");
            if (is_win.equals("1")) {
                //中奖
                String winning = obj.getString("winning");
                Object awardEnty = JsonPaser.parserObj(winning, PoolWinningEnty.class);
                PoolWinningEnty entyWin = (PoolWinningEnty) awardEnty;
                String name = entyWin.getName();
                LogUtil.i("获得的奖品:" + name);

                final WinningEnty winningEnty=new WinningEnty(entyWin.getFid(),entyWin.getName(),entyWin.getCname(),entyWin.getPid(),entyWin.getRegistration_id(),entyWin.getDelivery_order_id(),entyWin.getIs_registration());

                //获得奖品的类型
                String type = ((PoolWinningEnty) awardEnty).getType();
                //弹出获奖对话框
                if(type.equals("product")){
                    //获得的是奖品则跳转到领取奖品页面
                    final CustomAwardDialog dialog=CustomAwardDialog.getInstance(AwardPoolActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    dialog.setMessage(name);
                    dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =new Intent(AwardPoolActivity.this,GetAwardActivity.class);
                            ArrayList list=new ArrayList();
                            list.add(winningEnty);
                            intent.putExtra("data",list);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }else{
                    //获得的是积分则自动领取积分
                    CustomPoolAwardDialog awardDialog = CustomPoolAwardDialog.getInstance(AwardPoolActivity.this);
                    awardDialog.setCanceledOnTouchOutside(false);
                    awardDialog.show();
                    awardDialog.setText(name);
                }
            } else if (is_win.equals("0")) {
                //未中奖
                CustomAwardFailDialog dialog = CustomAwardFailDialog.getInstance(AwardPoolActivity.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        } else if (res.equals("101")) {

        } else if (res.equals("205")) {
            //活动已经过期
            ToastUtil.makeToast("活动已过期");
        }
    }
    //初始化组件
    private void setViews() {
        ll_back = (LinearLayout) findViewById(R.id.ll_award_pool_back);

        timeLeft = (TextView) findViewById(R.id.tv_award_pool_time_left);
        awardLeft = (TextView) findViewById(R.id.tv_award_pool_award_left);
        awardCount = (TextView) findViewById(R.id.tv_award_pool_award_count);
        awardList = (TextView) findViewById(R.id.tv_award_pool_award_list);
        title = (TextView) findViewById(R.id.tv_award_pool_act_title);
        awardRule = (TextView) findViewById(R.id.tv_award_pool_rule);
        awardTime = (TextView) findViewById(R.id.tv_award_pool_time);
        consume = (TextView) findViewById(R.id.tv_award_pool_consume);

        scrollView = (ScrollView) findViewById(R.id.sv_award_pool_scrollView);

        notStart = (FrameLayout) findViewById(R.id.fl_award_pool_not_start);
        started = (FrameLayout) findViewById(R.id.fl_award_pool_started);

        //animation = (ImageView) findViewById(R.id.iv_award_pool_animation);
        iv_started= (ImageView) findViewById(R.id.iv_award_pool_started);
        iv_not_start= (ImageView) findViewById(R.id.iv_award_pool_not_start);
    }
    //获取上一个页面传递过来的数据
    private void getData() {
        Intent intent = getIntent();
        enty = (ActivityListEntity) intent.getSerializableExtra("data");
    }
    //动画图片
    private int[] picAry = new int[]{
//            R.drawable.pond_0001,
//            R.drawable.pond_0002,
//            R.drawable.pond_0003,
//            R.drawable.pond_0004,
//            R.drawable.pond_0005,
//            R.drawable.pond_0006,
//            R.drawable.pond_0007,
//            R.drawable.pond_0008,
//            R.drawable.pond_0009,
//            R.drawable.pond_0010,
//            R.drawable.pond_0011,
//            R.drawable.pond_0012,
//            R.drawable.pond_0013,
//            R.drawable.pond_0014,
//            R.drawable.pond_0015,
//            R.drawable.pond_0016,
//            R.drawable.pond_0017,
//            R.drawable.pond_0018,
//            R.drawable.pond_0019,
//            R.drawable.pond_0020,
//            R.drawable.pond_0021,
//            R.drawable.pond_0022,
//            R.drawable.pond_0023,
//            R.drawable.pond_0024,
//            R.drawable.pond_0025,
//            R.drawable.pond_0026,
//            R.drawable.pond_0027,
//            R.drawable.pond_0028,
//            R.drawable.pond_0029,
//            R.drawable.pond_0030,
//            R.drawable.pond_0031,
//            R.drawable.pond_0032,
//            R.drawable.pond_0033,
//            R.drawable.pond_0034,
//            R.drawable.pond_0035,
//            R.drawable.pond_0036,
//            R.drawable.pond_0037,
//            R.drawable.pond_0038,
//            R.drawable.pond_0039,
//            R.drawable.pond_0040,
//            R.drawable.pond_0041,
//            R.drawable.pond_0042,
//            R.drawable.pond_0043,
//            R.drawable.pond_0044,
    };
}
