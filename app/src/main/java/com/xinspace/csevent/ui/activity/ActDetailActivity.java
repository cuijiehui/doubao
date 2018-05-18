package com.xinspace.csevent.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.GetActDetailInfoBiz;
import com.xinspace.csevent.customview.CustomAwardDialog;
import com.xinspace.csevent.customview.CustomAwardFailDialog;
import com.xinspace.csevent.customview.CustomAwardOpeningDialog;
import com.xinspace.csevent.data.entity.ActivityDetailEntity;
import com.xinspace.csevent.data.entity.ActivityInfoEntity;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.data.entity.WinningEnty;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.ActivityDetailParser;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 普通抽奖页面
 */
public class ActDetailActivity extends BaseActivity{

    private ActivityListEntity enty;
    private ViewPager vp_award_img;
    private TextView tv_time_left;//时间剩余
    private TextView award_left;//奖品剩余数量
    private TextView award_count;//中奖人数
    private TextView tv_act_title;
    private TextView tv_remark;//活动描述
    private TextView during_time;//活动时间
    private TextView tv_add;//加抽奖次数
    private TextView tv_reduce;
    private TextView tv_consume;
    private EditText et_count;//显示抽奖次数的输入框
    private ImageView iv_start;//开始抽奖

    private ScrollView scrollView_text;
    private ScrollView scrollView_html;
    private WebView webView;

    private String actName="";//活动的标题
    private LinearLayout ll_back;
    private FrameLayout state_not_start;//未开始状态
    private ImageView iv_not_start;
    private FrameLayout state_start;//点击开始抽奖

    private int count=1;//默认抽奖次数
    private int consume;//单次抽奖需要消耗的积分
    private int totalConsume;//总共消耗积分

    private ImageView iv_share;
    private List<Object> awardList;//中奖的中奖信息实体集合
    private Dialog dialog;
    private long time_left;//剩余时间的秒数

    private int[] count_ary=new int[]{1,3,6,10};//抽奖次数,四个档次
    private int flag=0;
    private int aid;//活动id
    private CustomAwardOpeningDialog openingDialog;
    private int defaultProgress=25;//抽奖对话框的默认进度
    //更新时间
    private TimerTask timeTask=new TimerTask() {
        @Override
        public void run() {
            if(time_left>60){
                time_left-=60;
                final TimeLeftEntity timeEnty=new TimeLeftEntity((int)time_left);
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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);
        //获取该活动的数据
        getData();
        setViews();
        setListener();
        //发送请求获取详细信息
        getActivityInfo();
    }

    //处理中奖信息
    private void handleAward(String result)throws Exception{
        obj=new JSONObject(result);
        String res=obj.getString("result");

        //中奖奖品名称的拼接
        sb=new StringBuilder();
        if(res.equals("200")){//正确
            //弹出抽奖进度对话框
            showAwardOpeningDialog();

        }else if(res.equals("101")){//金币不足

            return;
        }else if(res.equals("102")){//已经中过奖了,不可以再抽取
            ToastUtil.makeToast("您已中过奖,不可再抽取");
            return;
        }else if(res.equals("105")){//奖品已经发完
            ToastUtil.makeToast("奖品已发完");
            return;
        }
    }
    //在可以抽奖的情况下显示抽奖结果
    private void showMyAwardResult(){
        //关闭进度窗口
        if(null!=openingDialog){
            openingDialog.dismiss();
        }

        try{
            is_win=obj.getString("is_win");
            if ("1".equals(is_win)){//中奖
                JSONArray winAry = obj.getJSONArray("winning");
                awardList=JsonPaser.parserAry(winAry.toString(),WinningEnty.class);
                for (int i=0;i<awardList.size();i++) {
                    WinningEnty awardEnty = (WinningEnty) awardList.get(i);
                    String cname=awardEnty.getCname();
                    sb.append(cname+" ");
                }
            }else{//未中奖
                CustomAwardFailDialog dialog_fail= CustomAwardFailDialog.getInstance(this);
                dialog_fail.show();
                dialog_fail.setCanceledOnTouchOutside(false);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //判断中奖集合,如果不为空,则说明有中奖,空则说明没有中奖
        if(awardList.size()>0){
            final CustomAwardDialog dialog=CustomAwardDialog.getInstance(this);
            dialog.show();
            dialog.setMessage("获得\""+sb.toString()+"\"奖品");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(ActDetailActivity.this, GetAwardActivity.class);
                    intent.putExtra("data",(ArrayList)awardList);
                    startActivity(intent);
                }
            });
        }
    }

    //显示活动的信息
    private void showActivityInfo(String result) throws ParseException {
        LogUtil.i("活动详情:" + result);
        //显示奖品轮播图
        ActivityDetailEntity actEnty = ActivityDetailParser.parser(result);
        ActivityInfoEntity activity = actEnty.getActivity();
//        aid=activity.getActivity_id();
//        int is_html = activity.getHtml_is();
//        if(is_html==1){
//            //规则为一个静态页面
//            showHtmlDetail(actEnty);
//        }else {
//            //规则为文本类型加图片
//            showTextDetail(actEnty);
//        }
    }
    //显示静态页面的活动规则
    private void showHtmlDetail(ActivityDetailEntity actEnty) {
        scrollView_html.setVisibility(View.VISIBLE);
        ActivityInfoEntity activity = actEnty.getActivity();
        String html_url=activity.getHtml_href();
        //显示中奖人数,剩余奖品,剩余时间
        showAwardInfo(activity);

        WebSettings settings = webView.getSettings();
        //设置可以使用javascript
        settings.setJavaScriptEnabled(true);

        //加载页面
        webView.loadUrl(html_url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    //显示文本加图片类型的活动规则
    private void showTextDetail(ActivityDetailEntity actEnty) {
//        scrollView_text.setVisibility(View.VISIBLE);
//        final List<AwardInfoEntity> act_list = actEnty.getActivity_list();
//        List<ImageView> list=new ArrayList<>();
//        for (int i=0;i<act_list.size();i++){
//            AwardInfoEntity awdEnty = act_list.get(i);
//            String cname=awdEnty.getCname();
//            String url=awdEnty.getList_image();
//            //显示广告图片
//            final ImageView image = new ImageView(this);
//            image.setScaleType(ImageView.ScaleType.FIT_XY);
//            ImagerLoaderUtil.displayImage(url, image);
//            list.add(image);
//
//            //设置广告监听
//            image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //传递所有的奖品图片到下一个页面
//                    Intent intent=new Intent(ActDetailActivity.this,ZoomInImageActivity.class);
//                    intent.putExtra("data",(ArrayList<AwardInfoEntity>)act_list);
//                    startActivity(intent);
//                }
//            });
//
//            //添加活动标题
//            actName+=cname;
//            if(!(i==act_list.size()-1)){
//                actName+=",";
//            }
//        }
//        AdvViewpagerAdapter adapter=new AdvViewpagerAdapter(list);
//        vp_award_img.setAdapter(adapter);
//        tv_act_title.setText(actName);
//        ActivityInfoEntity actDetail = actEnty.getActivity();
//        //显示活动剩余时间,剩余奖品和中奖人数
//        showAwardInfo(actDetail);
//
//        //显示活动时间
//        String start_time=actDetail.getStarttime().substring(0,11);
//        String end_time=actDetail.getEndtime().substring(0,11);
//        String time=start_time+" - "+end_time;
//        during_time.setText(time);
//
//        //显示描述
//        tv_remark.setText(actDetail.getRemark());
    }
    //显示活动剩余时间,剩余奖品和中奖人数
    private void showAwardInfo(ActivityInfoEntity actDetail) {
        //单次抽奖消耗的积分数
        consume=Integer.valueOf(actDetail.getConsume());
        tv_consume.setText(String.valueOf(consume));
        LogUtil.i("本活动单次抽奖消耗的积分:"+consume);

        //中奖数和剩余奖数
        award_count.setText(String.valueOf(actDetail.getWinners()));
        award_left.setText(String.valueOf(actDetail.getSurplus_prize()));

        //显示剩余时间
        time_left = actDetail.getSurplus_time();
        TimeLeftEntity timeEnty=new TimeLeftEntity((int)time_left);
        updateTime(timeEnty);
        //启动倒计时的线程,每分钟更新一次
//        time_thread.start();

        timer=new Timer();
        timer.schedule(timeTask,0,1000*60);
    }
    //更新剩余时间
    private void updateTime(TimeLeftEntity timeEnty) {
        LogUtil.i("活动剩余秒:" + time_left);
        String day=timeEnty.getDays();
        String hours=timeEnty.getHoursWithDay();
        String mins=timeEnty.getMinWithDay();
        if(day.length()==1){
            day="0"+day;
        }
        if(hours.length()==1){
            hours="0"+hours;
        }
        if(mins.length()==1){
            mins="0"+mins;
        }
        LogUtil.i("day:" + day);
        LogUtil.i("hour:" + hours);
        LogUtil.i("min:" + mins);
        tv_time_left.setText(day + "天" + hours + "小时" + mins + "分");
    }
    //获取活动的详细信息
    private void getActivityInfo() {
        //创建一个loading的提示框
        dialog = DialogUtil.createLoadingDialog(ActDetailActivity.this,"");
        dialog.show();
        //dialog.setCanceledOnTouchOutside(true);
        GetActDetailInfoBiz.getDetail(enty, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
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
        enty = (ActivityListEntity)intent.getSerializableExtra("data");
    }
    //设置监听器
    private void setListener() {
        //滚动网页
        scrollView_html.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    showAttendAwardButton();
                }
                return false;
            }
        });
        //滚动文本
        scrollView_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    showAttendAwardButton();
                }
                return false;
            }
        });
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
                if(flag<3){
                    flag+=1;
                }else{
                    flag=3;
                }
                count=count_ary[flag];
                LogUtil.i("count="+count);
                et_count.setText(String.valueOf(count));
                //显示消耗积分
                showConsume();
            }
        });
        //减少抽奖次数
        tv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag>0){
                    flag-=1;
                }else{
                    flag=0;
                }
                count=count_ary[flag];
                LogUtil.i("count="+count);
                et_count.setText(String.valueOf(count));
                //显示消耗积分
                showConsume();
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
                if(CoresunApp.USER_ID==null){
                    ToastUtil.makeToast("请先登录");
                    startActivity(new Intent(ActDetailActivity.this,LoginActivity.class));
                    return;
                }
                attendActivity();
            }
        });
        //将抽奖按钮以动画的形式弹出
        iv_not_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                state_start.setVisibility(View.VISIBLE);
//                //布局高度
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();//屏幕高度
                LogUtil.i("高度:"+height+"/宽度:"+wm.getDefaultDisplay().getWidth());

                TranslateAnimation animation=new TranslateAnimation(0f,0f,height,0f);//参数是指调用的控件的参数,并非屏幕的参数
                animation.setDuration(500);

                AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f, 0f);
                alphaAnimation.setDuration(500);

                state_not_start.setAnimation(alphaAnimation);
                state_not_start.setVisibility(View.GONE);
                state_start.setAnimation(animation);
                animation.startNow();

                return false;
            }
        });
    }
    //显示不同档次,消耗不同的积分
    private void showConsume() {
        if(flag==0){
            totalConsume=consume;
        }else if(flag==1){
            totalConsume=consume*3-consume/10*1;
        }else if(flag==2){
            totalConsume=consume*6-consume/10*5;
        }else if(flag==3){
            totalConsume=consume*10-consume/10*10;
        }
        tv_consume.setText(String.valueOf(totalConsume));
        LogUtil.i(flag+"档消耗积分:"+totalConsume);
    }
    //滚动到底部后显示抽奖按钮
    private void showAttendAwardButton() {
        //如果抽奖按钮已经显示了,则return
        if(state_start.getVisibility()==View.VISIBLE){
            return;
        }
        if(state_not_start.getVisibility()==View.GONE){
            state_not_start.setVisibility(View.VISIBLE);
        }
    }
    //点击抽奖
    private void attendActivity() {
        //活动已经结束
        int state=enty.getState();
        if(state==1){
            ToastUtil.makeToast("该活动已经结束");
            return;
        }else if(state==2){
            //未开始
            ToastUtil.makeToast("活动未开始");
            return;
        }else if(state==4){
            ToastUtil.makeToast("该活动的奖品已经发完");
            return;
        }

        //调用抽奖接口
        getAwardResult();
        //设置抽奖按钮不可用
        iv_start.setClickable(false);
    }
    //获取抽奖结果
    private void getAwardResult() {
        //参与抽奖接口
//        AttendActivityBiz.attendByGeneral(this, enty, String.valueOf(count), new HttpRequestListener() {
//            @Override
//            public void onHttpRequestFinish(String result) throws JSONException {
//                try {
//                    //等待抽奖进度完成之后再显示结果
//                    handleAward(result);
//
//                    //设置抽奖按钮可用
//                    iv_start.setClickable(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
    //显示抽奖进度对话框
    private void showAwardOpeningDialog() {
        openingDialog= CustomAwardOpeningDialog.getInstance(this);
        openingDialog.setCanceledOnTouchOutside(false);
        openingDialog.show();

        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(defaultProgress>100){
                    timer.cancel();
                    defaultProgress=0;
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
                defaultProgress+=25;
            }
        },0,500);
    }
    //初始化组件
    private void setViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_award_detail_back);
        vp_award_img= (ViewPager) findViewById(R.id.vp_act_detail_viewPager);
        scrollView_text = (ScrollView) findViewById(R.id.scrollView_text);
        scrollView_html = (ScrollView) findViewById(R.id.scrollView_html);

        webView= (WebView) findViewById(R.id.wv_act_detail_webview);

        tv_time_left= (TextView) findViewById(R.id.tv_detail_time_left);
        award_left= (TextView) findViewById(R.id.tv_my_award_left);
        award_count= (TextView) findViewById(R.id.tv_my_award_count);
        tv_act_title= (TextView) findViewById(R.id.tv_detail_act_title);
        tv_remark= (TextView) findViewById(R.id.tv_act_remark);
        during_time= (TextView) findViewById(R.id.tv_act_during_time);
        tv_add= (TextView) findViewById(R.id.tv_detail_add);
        tv_reduce= (TextView) findViewById(R.id.tv_detail_reduce);
        tv_consume= (TextView) findViewById(R.id.tv_act_detail_consume);

        iv_start= (ImageView) findViewById(R.id.iv_detail_start_award);
        et_count=(EditText)findViewById(R.id.et_detail_count);
        iv_share = (ImageView)findViewById(R.id.iv_act_detail_share);

        state_not_start= (FrameLayout) findViewById(R.id.fl_state_not_start);
        iv_not_start= (ImageView) findViewById(R.id.iv_act_detail_not_start);
        state_start= (FrameLayout) findViewById(R.id.fl_state_start);
    }
    //显示分享
    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题：微信、QQ（新浪微博不需要标题）
        oks.setTitle("晟中活动 "+actName);  //最多30个字符

        // text是分享文本：所有平台都需要这个字段
        oks.setText(actName+"  http://www.coresun.net");  //最多40个字符

        // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
        //oks.setImagePath(Environment.getExternalStorageDirectory() + "/ic_launcher.png");//确保SDcard下面存在此张图片

        //网络图片的url：所有平台(需要设置此内容微信分享中才能点击链接)
        oks.setImageUrl("https://app.coresun.net/shide/assets/admin/layout/img/ic_launcher.png");//网络图片rul

        // url：仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://app.coresun.net/shide/wx_user/index_active?aid="+aid);   //网友点进链接后，可以看到分享的详情

        // Url：仅在QQ空间使用
        oks.setTitleUrl("http://www.coresun.net");  //网友点进链接后，可以看到分享的详情

        // 启动分享GUI
        oks.show(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //将倒计时取消
        if(null!=timer){
            timer.cancel();
        }
        timer=null;
        timeTask=null;
    }
}
