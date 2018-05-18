package com.xinspace.csevent.shop.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.MyTrailAct;
import com.xinspace.csevent.shop.adapter.MyTrailAdapter;
import com.xinspace.csevent.shop.modle.BannerBean;
import com.xinspace.csevent.shop.modle.TrialBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * 我的试用
 * Created by Android on 2017/6/14.
 */

public class MyTrialFragment extends Fragment{

    private View view;
    private ViewPager vp_my_trial;
    private RelativeLayout rel_applying;
    private RelativeLayout rel_apply_suc;
    private RelativeLayout rel_apply_fail;

    private MyListView rv_my_try;
    private MyTrailAdapter myTrailAdapter;

    private TextView tv_applying;
    private TextView tv_applying_line;

    private TextView tv_apply_suc;
    private TextView tv_apply_suc_line;

    private TextView tv_apply_fail;
    private TextView tv_apply_fail_line;
    private SDPreference preference;
    private String openId;
    private List<TrialBean> allBeanList = new ArrayList<TrialBean>();

    private List<ImageView> adv_list;//广告fragment集合
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private Timer timer = new Timer();
    private boolean isFirst = true; //第一次设置indicator
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what) {
                    case 200:
                        if (msg.obj != null) {
                            allBeanList.addAll((Collection<? extends TrialBean>) msg.obj);
                            myTrailAdapter.setList(allBeanList);
                            myTrailAdapter.notifyDataSetChanged();
                        }
                        break;
                    case HANDLER_SCROLL_ADVERTISEMENT:
                        scrollAdv();
                        break;
                }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_try , null);
        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
        initView();
        initData("0");

        return view;
    }

    private void initView() {

        vp_my_trial = (ViewPager) view.findViewById(R.id.vp_my_trial);
        vp_my_trial.setFocusable(true);

        rel_applying = (RelativeLayout) view.findViewById(R.id.rel_applying);
        rel_apply_suc = (RelativeLayout) view.findViewById(R.id.rel_apply_suc);
        rel_apply_fail = (RelativeLayout) view.findViewById(R.id.rel_apply_fail);

        rel_applying.setOnClickListener(onClickListener);
        rel_apply_suc.setOnClickListener(onClickListener);
        rel_apply_fail.setOnClickListener(onClickListener);

        tv_applying = (TextView) view.findViewById(R.id.tv_applying);
        tv_apply_suc = (TextView) view.findViewById(R.id.tv_apply_suc);
        tv_apply_fail = (TextView) view.findViewById(R.id.tv_apply_fail);

        tv_applying_line = (TextView) view.findViewById(R.id.tv_applying_line);
        tv_apply_suc_line = (TextView) view.findViewById(R.id.tv_apply_suc_line);
        tv_apply_fail_line = (TextView) view.findViewById(R.id.tv_apply_fail_line);

        rv_my_try = (MyListView) view.findViewById(R.id.lv_my_try);
        myTrailAdapter = new MyTrailAdapter(getActivity());
        myTrailAdapter.setList(allBeanList);
        rv_my_try.setAdapter(myTrailAdapter);
        rv_my_try.setOnItemClickListener(onItemClickListener);
        rv_my_try.setFocusable(false);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity() , MyTrailAct.class);

            intent.putExtra("id" , allBeanList.get(position).getId());

            startActivity(intent);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_applying:  //申请试用中

                    clickOne();
                    initData("0");

                    break;
                case R.id.rel_apply_suc:  //申请成功

                    clickTwo();
                    initData("1");

                    break;
                case R.id.rel_apply_fail:  //申请失败

                    clickThree();
                    initData("-1");

                    break;
            }
        }
    };

    private void clickOne(){
        tv_applying.setTextColor(Color.parseColor("#ea5205"));
        tv_applying_line.setBackgroundColor(Color.parseColor("#ea5205"));

        tv_apply_suc.setTextColor(Color.parseColor("#4a4a4a"));
        tv_apply_suc_line.setBackgroundColor(Color.parseColor("#ffffff"));

        tv_apply_fail.setTextColor(Color.parseColor("#4a4a4a"));
        tv_apply_fail_line.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    private void clickTwo(){
        tv_applying.setTextColor(Color.parseColor("#4a4a4a"));
        tv_applying_line.setBackgroundColor(Color.parseColor("#ffffff"));

        tv_apply_suc.setTextColor(Color.parseColor("#ea5205"));
        tv_apply_suc_line.setBackgroundColor(Color.parseColor("#ea5205"));

        tv_apply_fail.setTextColor(Color.parseColor("#4a4a4a"));
        tv_apply_fail_line.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    private void clickThree(){
        tv_applying.setTextColor(Color.parseColor("#4a4a4a"));
        tv_applying_line.setBackgroundColor(Color.parseColor("#ffffff"));

        tv_apply_suc.setTextColor(Color.parseColor("#4a4a4a"));
        tv_apply_suc_line.setBackgroundColor(Color.parseColor("#ffffff"));

        tv_apply_fail.setTextColor(Color.parseColor("#ea5205"));
        tv_apply_fail_line.setBackgroundColor(Color.parseColor("#ea5205"));
    }


    private void initData(String success){

        if (allBeanList.size() != 0){
            allBeanList.clear();
        }

        GetDataBiz.myApplyListData(openId, success, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("我的试用" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                JSONObject obj = new JSONObject(result);
                JSONObject dataObject = obj.getJSONObject("data");

                List<BannerBean> bannerList = new ArrayList<BannerBean>();
                List<TrialBean> beanList = new ArrayList<TrialBean>();

                JSONArray listJsonarry = dataObject.getJSONArray("list");
                for (int i = 0; i < listJsonarry.length(); i++) {
                    JSONObject beanObject = listJsonarry.getJSONObject(i);
                    TrialBean trialBean = gson.fromJson(beanObject.toString(), TrialBean.class);
                    beanList.add(trialBean);
                }
                handler.obtainMessage(200 , beanList).sendToTarget();

                JSONArray bannerJsonArray = dataObject.getJSONArray("adv");
                for (int i = 0; i < bannerJsonArray.length(); i++) {
                    JSONObject bannerObject = bannerJsonArray.getJSONObject(i);
                    BannerBean bannerBean =  gson.fromJson(bannerObject.toString(), BannerBean.class);
                    bannerList.add(bannerBean);
                }

                adv_list = new ArrayList<ImageView>();
                for (int i = 0; i < bannerList.size(); i++) {
                    final BannerBean enty = (BannerBean) bannerList.get(i);
                    String url = enty.getImgurl();
                    // final String link = enty.getAdlink();
                    //显示广告图片
                    ImageView image = new ImageView(getActivity());
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
                    //设置监听器
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        if (enty.getType() != null) {
//                            //clickIntoAdvertisement(link, enty.getType());
//                        }
                        }
                    });
                    adv_list.add(image);
                }
                //设置适配器
                AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(adv_list);
                vp_my_trial.setAdapter(adapter);

                //设置指示器
                if (isFirst) {
                    isFirst = false;
                    // indicator.setViewPager(vp_adv);
                    //滚动广告
                    timer.schedule(timeTask, 5000, 5000);
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //设置广告页
    private void scrollAdv() {
        if (scroll_flag) {
            int currentIndex = vp_my_trial.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_my_trial.setCurrentItem(currentIndex, false);
        }
    }

}
