package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.fragment.ShopOneFragment;
import com.xinspace.csevent.shop.modle.ClassBean;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.ui.fragment.ShopFristFragment;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Android on 2017/9/30.
 */

public class ShideShopActivity extends BaseActivity {

    private TabLayout tab_shop;
    private LinearLayout lin_shop_content;
    private int currentIndex=0;//当前的fragment下标
    private int selectedIndex = currentIndex;
    private Fragment[] fragmentArray;
    private List<ClassBean> allBean = new ArrayList<>();
    private ImageView iv_main_cart;
    private SDPreference preference;
    private String userId;
    private TextView tv_shopcart_size;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    if (msg.obj != null){
                        allBean.addAll((Collection<? extends ClassBean>) msg.obj);
                        initView();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.activity_shide_shop);
        preference = SDPreference.getInstance();
        userId = preference.getContent("userId");
        initData();
    }

    /**
     * 获取分类数据
     */
    private void initData() {
        GetActivityListBiz.getGoodsClass(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if(result == null && result.equals("")){
                    return;
                }

                LogUtil.i("商品分类数据" + result);
                Gson gson = new Gson();
                List<ClassBean> beanList = new ArrayList<ClassBean>();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        ClassBean bean = gson.fromJson(dataObject.toString() , ClassBean.class);
                        beanList.add(bean);
                    }
                    handler.obtainMessage(100 , beanList).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void initView() {

        iv_main_cart = (ImageView) findViewById(R.id.iv_main_cart);
        iv_main_cart.setOnClickListener(clickListener);

        tv_shopcart_size = (TextView) findViewById(R.id.tv_shopcart_size);
        if (!userId.equals("0")){
            tv_shopcart_size.setText(preference.getContent("cartSize"));
        }else{
            tv_shopcart_size.setText("0");
        }

        fragmentArray=new Fragment[allBean.size()];

        tab_shop = (TabLayout) findViewById(R.id.tab_shop);
        lin_shop_content = (LinearLayout) findViewById(R.id.lin_shop_content);

        tab_shop.addTab(tab_shop.newTab().setText("首   页"));

        for (int i = 1 ; i < allBean.size() ; i++){
            tab_shop.addTab(tab_shop.newTab().setText(allBean.get(i).getText()));
        }

        fragmentArray[0]=new ShopFristFragment();

        for (int i = 1 ; i < allBean.size() ; i++ ){
            fragmentArray[i]=new ShopOneFragment();
            Bundle bundle = new Bundle();
            bundle.putString("cate" , allBean.get(i).getCate());
            fragmentArray[i].setArguments(bundle);
        }

        //添加
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.lin_shop_content,fragmentArray[0]);
        //提交
        transaction.commit();

        // 设置tab文本的没有选中（第一个参数）和选中（第二个参数）的颜色
        tab_shop.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#ef5948"));
        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tab_shop);
        tab_shop.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedIndex = tab.getPosition();
                changePage();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //改变页面
    private void changePage(){
        //选中的不是当前的按钮
        if(selectedIndex != currentIndex){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment showFragment = fragmentArray[selectedIndex];
            if(!showFragment.isAdded()){
                //将选中的响应页面添加到页面
                transaction.add(R.id.lin_shop_content,showFragment);
            }
            //隐藏 和显示
            transaction.hide(fragmentArray[currentIndex]);
            transaction.show(showFragment);
            transaction.commit();
            currentIndex = selectedIndex;
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_main_cart:
                    userId = preference.getContent("userId");
                    if (userId.equals("0")){
                        Intent intent = new Intent(ShideShopActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if (!userId.equals("0")){
                        Intent intent = new Intent(ShideShopActivity.this , ShopCartAct2.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };
}
