package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.entity.GroupParticipants;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.login.activity.ExChangeAct;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.ParticipantsAdapter;
import com.xinspace.csevent.shop.adapter.RecommendAdapter;
import com.xinspace.csevent.shop.modle.GroupDetailBean;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpellGroupActivity extends BaseActivity{

    /**控件初始化*/
    @BindView(R.id.tv_common_title) //标题
    TextView tvCommonTitle;
    @BindView(R.id.ll_common_icon_back) //返回键
    LinearLayout llCommonIconBack;
    @BindView(R.id.iv_group_logo)  //团购物品的图片
    ImageView ivGroupLogo;
    @BindView(R.id.spell_group_commodity_name)  //团购的详细描述
    TextView spellGroupCommodityName;
    @BindView(R.id.spell_group_price)   //团购的价格
    TextView spellGroupPrice;
    @BindView(R.id.spell_group_person_count)    //团购的人数
    TextView spellGroupPersonCount;
    @BindView(R.id.spell_group_singlebuy_price)     //单买的价格
    TextView spellGroupSinglebuyPrice;
    @BindView(R.id.spell_group_original_price)      //原价购买
    TextView spellGroupOriginalPrice;
    @BindView(R.id.spell_group_participants_container)  //参与团购者
    RecyclerView rvParticipants;
    @BindView(R.id.spell_group_notice)        //团购人数已满
    TextView spellGroupNotice;
    @BindView(R.id.spell_group_endtime)
    RelativeLayout endTimeContainer;
    @BindView(R.id.spell_group_tv_endtime)
    TextView tvEndTime;
    @BindView(R.id.spell_group_join)        //一键团购
    Button spellGroupJoin;
    @BindView(R.id.spell_group_participants)   //团购相关的容器
    LinearLayout spellGroupParticipants;
    @BindView(R.id.lin_group)   //震颤拼团
    LinearLayout linGroup;
    @BindView(R.id.lin_seckill) //限时秒杀
    LinearLayout linSeckill;
    @BindView(R.id.lin_exChange) //积分兑换
    LinearLayout linExChange;
    @BindView(R.id.lin_jiu)     //9块9包邮
    LinearLayout linJiu;
    @BindView(R.id.spell_group_find_more)   //查看更多
    RelativeLayout spellGroupFindMore;
    @BindView(R.id.spell_group_anthor)  //其他商品
    RecyclerView spellGroupAnthor;

    /**此处开始为变量*/
    private GroupDetailBean bean;   //团购详情实体类
    private String type;        //团购类型
    private String teamId;      //团购ID
    private SDPreference prefs;
    private String openId;
    private String commodityUrl;
    private List<Params> params;
    GroupParticipants participants;

    private Gson gson;
    private ParticipantsAdapter adapter;
    private Timer timer;
    private int endTime;
    private RecommendAdapter recommendAdapter;
    private String jornGroupUrl;    //一键参团或开团的URL

    @OnClick({R.id.ll_common_icon_back,R.id.spell_group_join,R.id.lin_group,R.id.lin_seckill,R.id.lin_exChange,R.id.lin_jiu,R.id.spell_group_find_more})
    void clickEvent(View view){
        switch (view.getId()){
            case R.id.ll_common_icon_back://返回
                finish();
                break;
            case R.id.spell_group_join:
                joinGroup();
                break;
            case R.id.lin_group:
                Intent intent2 = new Intent(this, GroupGoodsAct.class);
                startActivity(intent2);
                break;
            case R.id.lin_seckill:
                Intent intent1 = new Intent(this, SeckillAct.class);
                startActivity(intent1);
                break;
            case R.id.lin_exChange:
                String userId = SDPreference.getInstance().getContent("userId");
                if (!userId.equals("0")) {
                    Intent intent4 = new Intent(this, ExChangeAct.class);
                    startActivity(intent4);
                } else {
                    Intent intent4 = new Intent(this, LoginActivity.class);
                    startActivity(intent4);
                }
                break;
            case R.id.lin_jiu:
                Intent intent = new Intent(this, JiuGoodsAct.class);
                startActivity(intent);
                break;
            case R.id.spell_group_find_more:
                ToastUtil.makeToast("业务拓展中！");
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_group);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.app_bottom_color);
        ButterKnife.bind(this);
        tvCommonTitle.setText("拼团");
        prefs = SDPreference.getInstance();
        gson = new Gson();
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        rvParticipants.setLayoutManager(layoutManager);
        adapter = new ParticipantsAdapter(this);
        rvParticipants.setAdapter(adapter);
    }

    private void loadData() {
        try{
            bean = (GroupDetailBean) getIntent().getSerializableExtra("bean");
            type = getIntent().getStringExtra("type");
            teamId = getIntent().getStringExtra("teamid");
            openId = prefs.getContent("openid");
            commodityUrl = AppConfig.GROUP_PERSON_DETAIL;
            params = new ArrayList<>();
            params.add(new Params("teamid", teamId));
            params.add(new Params("openId", openId));
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            httpUtil.sendPost(commodityUrl, params);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *  数据回调的监听
     */
    private HttpRequestListener listener = new HttpRequestListener() {
        @Override
        public void onHttpRequestFinish(String result) throws JSONException {
            participants = parseData(result);
            setViewByData(participants);
        }

        @Override
        public void onHttpRequestError(String error) {
            ToastUtil.makeToast("加载数据出错啦，请检查网络哦，亲！");
        }
    };

    private void setViewByData(GroupParticipants participants) {
        //数据
        String groupNum = participants.getGoods().getGroupnum();
        String title = participants.getGoods().getTitle();
        String groupsPrice = participants.getGoods().getGroupsprice();
        String imgCommodity = participants.getGoods().getThumb();
        String singlePrice = participants.getGoods().getSingleprice();
        String originalPrice = participants.getGoods().getPrice();
        String salesCount = participants.getGoods().getSales();
        /**显示数据*/
        //1.头部
        //TextView富文本
        SpannableStringBuilder ssb = new SpannableStringBuilder(groupNum + "人拼团 " + title);
        ForegroundColorSpan soreSpan = new ForegroundColorSpan(Color.parseColor("#EA5202"));
        ssb.setSpan(soreSpan, 0, groupNum.length()+3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spellGroupCommodityName.setText(ssb);

        spellGroupPrice.setText("¥" + groupsPrice);
        spellGroupSinglebuyPrice.setText("单买价 ¥" + singlePrice);
        spellGroupOriginalPrice.setText("原价" + originalPrice);
        spellGroupOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        spellGroupPersonCount.setText("已有" + salesCount + "人参与");

        Glide.with(this)
                .load(imgCommodity)
                .placeholder(R.drawable.loading_icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivGroupLogo);

        //2.中部(参团人数)
        int iGroupNum = Integer.valueOf(groupNum);
        int iGoodsNum = Integer.valueOf(participants.getGoods().getGoodsnum());
        //如果参团人数已满
        if (iGroupNum==iGoodsNum){
            endTimeContainer.setVisibility(View.GONE);
            spellGroupNotice.setText("此团人数已满");
            spellGroupJoin.setText("一键开团");
        }
        else{
            spellGroupNotice.setText("仅差" + (iGroupNum-iGoodsNum) + "人，即可成团");
            spellGroupJoin.setText("立即参团");
            endTime = participants.getEndtime();
            startTask();//倒计时
        }
        adapter.setData(participants.getGroup());
        adapter.setTotalParticipants(Integer.valueOf(participants.getGoods().getGroupnum()));

        //3.其他产品
        recommendAdapter = new RecommendAdapter(R.layout.item_recommend, participants.getRecommend());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        spellGroupAnthor.setLayoutManager(layoutManager);
        spellGroupAnthor.setNestedScrollingEnabled(false);
        spellGroupAnthor.setAdapter(recommendAdapter);
    }

    /**
     * 解析数据 Gson
     * @param result
     * @return
     * @throws JSONException
     */
    private GroupParticipants parseData(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        String code = jsonObject.getString("code");
        String data = jsonObject.getString("data");
        if (code.equals("200")){
            return gson.fromJson(data, GroupParticipants.class);
        }
        return null;
    }

    //更新时间 timerTask
    private void startTask(){
        timer = new Timer();
        TimerTask timeTask1 = new TimerTask() {
            @Override
            public void run() {
                //主线程中更新UI
                endTime -= 1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTime();
                    }
                });
            }
        };
        timer.schedule(timeTask1, 0, 1000);
    }

    private void updateTime(){
        TimeLeftEntity timeEnty = new TimeLeftEntity(endTime);
        String day = timeEnty.getDays();
        String hours = timeEnty.getHoursWithDay();
        String mins = timeEnty.getMinWithDay();
        String sec = timeEnty.getSecondsWithDay();

        if (hours.length() == 1 ){
            hours = "0" + hours;
        }

        if (mins.length() == 1 ){
            mins = "0" + mins;
        }

        if (sec.length() == 1 ){
            sec = "0" + sec;
        }

        String stime = "剩下 " + hours + ":" + mins + ":" + sec;
        SpannableStringBuilder ssb = new SpannableStringBuilder(stime);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        ssb.setSpan(fcs, 3, stime.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvEndTime.setText(ssb);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null) timer.cancel();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void joinGroup() {
        Intent intent = new Intent(this, BuyGroupAct.class);
        intent.putExtra("type", type);
        intent.putExtra("teamid", teamId);
        intent.putExtra("bean", bean);
        startActivity(intent);
    }
}
