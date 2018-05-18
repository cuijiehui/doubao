package com.xinspace.csevent.monitor.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.LeaseAdapter;
import com.xinspace.csevent.monitor.bean.LeaseRoomBean;
import com.xinspace.csevent.monitor.bean.LeaseScreenBean;
import com.xinspace.csevent.monitor.view.ChooseAreaPop;
import com.xinspace.csevent.monitor.view.LeaseRentPop;
import com.xinspace.csevent.monitor.view.LeaseUnitPop;
import com.xinspace.csevent.monitor.weiget.IdentityResultListener;
import com.xinspace.csevent.monitor.weiget.LeaseMorePop;
import com.xinspace.csevent.monitor.weiget.ReturnMoreListener;
import com.xinspace.csevent.monitor.weiget.ReturnPriceListener;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.ui.widget.citypicker.activity.CityPickerActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 租赁界面
 *
 * Created by Android on 2017/3/17.
 */

public class LeaseActivity extends BaseActivity{

    private LinearLayout ll_lease_back;
    private LinearLayout ll_choose_address;
    private TextView tv_address;
    private EditText et_lease_search;
    private TextView tv_search;
    private LinearLayout lin_choose_area;  // 区域
    private LinearLayout lin_lease_rent;   // 租金
    private LinearLayout lin_lease_unit;   // 户型
    private LinearLayout lin_lease_more;   // 更多
    private RecyclerView rc_lease;
    private SDPreference preference;
    private String cuid;
    private String cToken;
    private List<LeaseRoomBean> allList = new ArrayList<LeaseRoomBean>();
    private LeaseAdapter leaseAdapter ;
    private boolean isMore;
    private int page;
    private ChooseAreaPop chooseAreaPop;
    private LeaseRentPop leaseRentPop;
    private LeaseUnitPop leaseUnitPop;
    private LeaseMorePop leaseMorePop;
    private static final int REQUEST_CODE_PICK_CITY = 233;
    private String chooseCity = "广州";
    private LeaseScreenBean leaseScreenBean;

    private TextView tv_choose_area;
    private TextView tv_lease_rent;
    private TextView tv_lease_unit;
    private TextView tv_lease_more;

    private boolean isClickArea = false;
    private boolean isClickRent = false;
    private boolean isClickUnit = false;
    private boolean isClickMore = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends LeaseRoomBean>) msg.obj);
                        leaseAdapter.notifyDataSetChanged();
                    }
                    break;
                case 400:

                    ToastUtil.makeToast("没有更多数据");

                    break;
            }
        }
    };
    private String area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lease);

        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        preference = SDPreference.getInstance();
        cuid = preference.getContent("cUid");
        cToken = preference.getContent("cToken");

        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        initView();
        leaseScreenBean = new LeaseScreenBean();
        leaseScreenBean.setCity(chooseCity);

        page = 1;
        initData(page , leaseScreenBean);
    }

    private void initView() {
        ll_lease_back = (LinearLayout) findViewById(R.id.ll_lease_back);
        ll_lease_back.setOnClickListener(onClickListener);

        ll_choose_address = (LinearLayout) findViewById(R.id.ll_choose_address);
        ll_choose_address.setOnClickListener(onClickListener);

        tv_address = (TextView) findViewById(R.id.tv_address);
        et_lease_search = (EditText) findViewById(R.id.et_lease_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        lin_choose_area = (LinearLayout) findViewById(R.id.lin_choose_area);
        lin_lease_rent = (LinearLayout) findViewById(R.id.lin_lease_rent);
        lin_lease_unit = (LinearLayout) findViewById(R.id.lin_lease_unit);
        lin_lease_more = (LinearLayout) findViewById(R.id.lin_lease_more);

        lin_choose_area.setOnClickListener(onClickListener);
        lin_lease_rent.setOnClickListener(onClickListener);
        lin_lease_unit.setOnClickListener(onClickListener);
        lin_lease_more.setOnClickListener(onClickListener);

        tv_choose_area = (TextView) findViewById(R.id.tv_choose_area);
        tv_lease_rent = (TextView) findViewById(R.id.tv_lease_rent);
        tv_lease_unit = (TextView) findViewById(R.id.tv_lease_unit);
        tv_lease_more = (TextView) findViewById(R.id.tv_lease_more);

        rc_lease = (RecyclerView) findViewById(R.id.rc_lease);
        leaseAdapter = new LeaseAdapter(allList , LeaseActivity.this);
        rc_lease.setLayoutManager(new LinearLayoutManager(LeaseActivity.this, LinearLayoutManager.VERTICAL, false));
        rc_lease.setAdapter(leaseAdapter);

        rc_lease.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                if (leaseAdapter.getItemCount() > 3 && lastCompletelyVisibleItemPosition == leaseAdapter.getItemCount() - 1 && isMore) {
                    LogUtil.i("加载下一页-----------------");
                    page++;
                    initData(page , leaseScreenBean);
                    isMore = false;
                }
            }
        });
    }

    private void initData(int page , LeaseScreenBean bean) {

        GetDataBiz.GET_LEASE_ROOM_DATA(area , cuid, cToken, page , bean ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (TextUtils.isEmpty(result)){
                    return;
                }

                Gson gson = new Gson();
                //LeaseData data = gson.fromJson(result , LeaseData.class);

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 200){
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    List<LeaseRoomBean> list = new ArrayList<LeaseRoomBean>();
                    for (int i = 0 ; i < dataArray.length() ; i++){
                        JSONObject jsonObject1 = dataArray.getJSONObject(i);
                        LeaseRoomBean bean = gson.fromJson(jsonObject1.toString() , LeaseRoomBean.class);
                        list.add(bean);
                    }

                    if (list.size() != 0){
                        handler.obtainMessage(200 , list).sendToTarget();
                        isMore = true;
                    }else{
                        isMore = false;
                        handler.obtainMessage(400).sendToTarget();
                    }
                }else{

                }

            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_lease_back:
                    LeaseActivity.this.finish();
                    break;
                case R.id.ll_choose_address:

                    Intent intent = new Intent(LeaseActivity.this , CityPickerActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent , REQUEST_CODE_PICK_CITY);

                    break;
                case R.id.lin_choose_area:
                    showPop();

                    if (isClickArea){
                        tv_choose_area.setTextColor(Color.parseColor("#666666"));
                        isClickArea = false;
                    }else{
                        tv_choose_area.setTextColor(Color.parseColor("#ea5205"));
                        isClickArea = true;
                    }
                    tv_lease_rent.setTextColor(Color.parseColor("#666666"));
                    tv_lease_unit.setTextColor(Color.parseColor("#666666"));
                    tv_lease_more.setTextColor(Color.parseColor("#666666"));

                    //isClickArea = false;
                    isClickRent = false;
                    isClickUnit = false;
                    isClickMore = false;


                   if (leaseRentPop != null &&leaseRentPop.isShowing()){
                       leaseRentPop.dismiss();
                   }

                    if (leaseUnitPop != null &&leaseUnitPop.isShowing()){
                        leaseUnitPop.dismiss();
                    }

                    if (leaseMorePop != null &&leaseMorePop.isShowing()){
                        leaseMorePop.dismiss();
                    }

                    break;
                case R.id.lin_lease_rent:
                    showRentPop();

                    if (isClickRent){
                        tv_lease_rent.setTextColor(Color.parseColor("#666666"));
                        isClickRent = false;
                    }else{
                        tv_lease_rent.setTextColor(Color.parseColor("#ea5205"));
                        isClickRent = true;
                    }

                    tv_choose_area.setTextColor(Color.parseColor("#666666"));
                    tv_lease_unit.setTextColor(Color.parseColor("#666666"));
                    tv_lease_more.setTextColor(Color.parseColor("#666666"));


                    isClickArea = false;
                    //isClickRent = false;
                    isClickUnit = false;
                    isClickMore = false;


                    if (chooseAreaPop != null && chooseAreaPop.isShowing()){
                        chooseAreaPop.dismiss();
                    }

                    if (leaseUnitPop != null && leaseUnitPop.isShowing()){
                        leaseUnitPop.dismiss();
                    }

                    if (leaseMorePop != null && leaseMorePop.isShowing()){
                        leaseMorePop.dismiss();
                    }

                    break;
                case R.id.lin_lease_unit:

                    showUnitPop();

                    if (isClickUnit){
                        tv_lease_unit.setTextColor(Color.parseColor("#666666"));
                        isClickUnit = false;
                    }else{
                        tv_lease_unit.setTextColor(Color.parseColor("#ea5205"));
                        isClickUnit = true;
                    }

                    tv_choose_area.setTextColor(Color.parseColor("#666666"));
                    tv_lease_rent.setTextColor(Color.parseColor("#666666"));
                    //tv_lease_unit.setTextColor(Color.parseColor("#ea5205"));
                    tv_lease_more.setTextColor(Color.parseColor("#666666"));

                    isClickArea = false;
                    isClickRent = false;
                    //isClickUnit = false;
                    isClickMore = false;


                    if (chooseAreaPop != null && chooseAreaPop.isShowing()){
                        chooseAreaPop.dismiss();
                    }

                    if (leaseRentPop != null && leaseRentPop.isShowing()){
                        leaseRentPop.dismiss();
                    }

                    if (leaseMorePop != null && leaseMorePop.isShowing()){
                        leaseMorePop.dismiss();
                    }


                    break;
                case R.id.lin_lease_more:

                    showMorePop();

                    if (isClickMore){
                        tv_lease_more.setTextColor(Color.parseColor("#666666"));
                        isClickMore = false;
                    }else{
                        tv_lease_more.setTextColor(Color.parseColor("#ea5205"));
                        isClickMore = true;
                    }

                    tv_choose_area.setTextColor(Color.parseColor("#666666"));
                    tv_lease_rent.setTextColor(Color.parseColor("#666666"));
                    tv_lease_unit.setTextColor(Color.parseColor("#666666"));
                    //tv_lease_more.setTextColor(Color.parseColor("#ea5205"));

                    isClickArea = false;
                    isClickRent = false;
                    isClickUnit = false;
                    //isClickMore = false;

                    if (chooseAreaPop != null && chooseAreaPop.isShowing()){
                        chooseAreaPop.dismiss();
                    }

                    if (leaseRentPop != null && leaseRentPop.isShowing()){
                        leaseRentPop.dismiss();
                    }

                    if (leaseRentPop != null && leaseRentPop.isShowing()){
                        leaseUnitPop.dismiss();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                chooseCity = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                tv_address.setText(chooseCity);
            }
        }
    }

    private void showPop() {
        if (chooseAreaPop == null) {
            chooseAreaPop = new ChooseAreaPop(LeaseActivity.this, chooseCity , listener);
            chooseAreaPop.setOnDismissListener(dismissListener);
        }

        if (!chooseAreaPop.isShowing()) {
            chooseAreaPop.showAtLocation(lin_choose_area, Gravity.BOTTOM, 0, 0);
            chooseAreaPop.isShowing();
        } else {
            chooseAreaPop.dismiss();
        }
    }

    IdentityResultListener listener = new IdentityResultListener() {
        @Override
        public void identityResult(String identity) {
            allList.clear();
            leaseAdapter.notifyDataSetChanged();

            leaseScreenBean.setArea(identity);
            page = 1;
            initData(page , leaseScreenBean);
        }
    };

    private void showRentPop(){

        if (leaseRentPop == null) {
            leaseRentPop = new LeaseRentPop(LeaseActivity.this , returnPriceListener);
            leaseRentPop.setOnDismissListener(dismissListener);
        }

        if (!leaseRentPop.isShowing()) {
            leaseRentPop.showAtLocation(lin_choose_area, Gravity.BOTTOM, 0, 0);
            leaseRentPop.isShowing();
        } else {
            leaseRentPop.dismiss();
        }
    }

    ReturnPriceListener returnPriceListener = new ReturnPriceListener() {
        @Override
        public void returnPrice(String minP, String maxP) {
            allList.clear();
            leaseAdapter.notifyDataSetChanged();

            leaseScreenBean.setMinprice(minP);
            leaseScreenBean.setMaxprice(maxP);
            page = 1;
            initData(page , leaseScreenBean);
        }
    };

    private void showUnitPop(){

        if (leaseUnitPop == null) {
            leaseUnitPop = new LeaseUnitPop(LeaseActivity.this , listener2);
            leaseUnitPop.setOnDismissListener(dismissListener);
        }

        if (!leaseUnitPop.isShowing()) {
            leaseUnitPop.showAtLocation(lin_choose_area, Gravity.BOTTOM, 0, 0);
            leaseUnitPop.isShowing();
        } else {
            leaseUnitPop.dismiss();
        }
    }


    IdentityResultListener listener2 = new IdentityResultListener() {
        @Override
        public void identityResult(String identity) {
            allList.clear();
            leaseAdapter.notifyDataSetChanged();

            leaseScreenBean.setHouse_type(identity);
            page = 1;
            initData(page , leaseScreenBean);
        }
    };



    private void showMorePop(){
        if (leaseMorePop == null) {
            leaseMorePop = new LeaseMorePop(LeaseActivity.this , moreListener);
            leaseMorePop.setOnDismissListener(dismissListener);
        }

        if (!leaseMorePop.isShowing()) {
            leaseMorePop.showAtLocation(lin_choose_area, Gravity.BOTTOM, 0, 0);
            leaseMorePop.isShowing();
        } else {
            leaseMorePop.dismiss();
        }
    }


    ReturnMoreListener moreListener = new ReturnMoreListener() {
        @Override
        public void returnPrice(String orientations, String rent_type, String charge_pay) {

            allList.clear();
            leaseAdapter.notifyDataSetChanged();

            leaseScreenBean.setOrientations(orientations);
            leaseScreenBean.setCharge_pay(charge_pay);
            leaseScreenBean.setRent_type(rent_type);
            page = 1;
            initData(page , leaseScreenBean);

        }
    } ;


    PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            LogUtil.i("---------------------------------------------");
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClickListener = null;
        chooseAreaPop = null;
    }
}


