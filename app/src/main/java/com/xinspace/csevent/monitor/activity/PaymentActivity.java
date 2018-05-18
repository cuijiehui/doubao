package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.PaymentAdapter;
import com.xinspace.csevent.monitor.bean.PayTypeBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
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
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 社区缴费界面
 *
 * Created by Android on 2017/3/17.
 */

public class PaymentActivity extends BaseActivity{

    private LinearLayout ll_payment_back;

//    private RelativeLayout rel_pay_water;     //水费
//    private RelativeLayout rel_energy_charge; //电费
//    private RelativeLayout rel_fuel_gas;      //燃气费
//    private RelativeLayout rel_parking_charge;//停车费
//    private RelativeLayout rel_property_fee;  //物业费

    private ExpandableListView expandable_lv;
    private String cUid;
    private String token;
    private String com_id;
    private SDPreference preference;
    private List<String> typeList;
    private List<List<PayTypeBean>> itemList;
    private TextView tv_payment_record;

    private PaymentAdapter paymentAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    LogUtil.i("---------------------------------");
                    paymentAdapter = new PaymentAdapter(typeList , itemList , PaymentActivity.this);
                    expandable_lv.setAdapter(paymentAdapter);
                    break;
            }
        }
    };
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_payment2);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        preference = SDPreference.getInstance();
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");

        cUid = preference.getContent("cUid");
        token = preference.getContent("cToken");
        com_id = preference.getContent("com_id");

        itemList = new ArrayList<>();
        typeList = new ArrayList<>();

        initData();
        initView();
    }


    private void initView() {
        ll_payment_back = (LinearLayout) findViewById(R.id.ll_payment_back);
        ll_payment_back.setOnClickListener(onClickListener);

        expandable_lv = (ExpandableListView) findViewById(R.id.expandable_lv);
        expandable_lv.setGroupIndicator(null);

        tv_payment_record = (TextView) findViewById(R.id.tv_payment_record);
        tv_payment_record.setOnClickListener(onClickListener);

        expandable_lv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (itemList.get(groupPosition).size() == 0){
                    ToastUtil.makeToast("无未缴费");
                }
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_payment_back:
                    PaymentActivity.this.finish();
                    break;

                case R.id.tv_payment_record:
                    Intent intent = new Intent(PaymentActivity.this , PayMentRecordAct.class);
                    startActivity(intent);
                    break;
            }
        }
    };


    private void initData() {

        GetDataBiz.getPaymentData(area, cUid, token, com_id, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("代缴费的result" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("code").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject typeJsonObject = jsonArray.getJSONObject(i);
                        typeList.add(typeJsonObject.getString("name"));
                        JSONArray dataJsonArray = typeJsonObject.getJSONArray("data");
                        List<PayTypeBean> list = new ArrayList<PayTypeBean>();
                        for (int j = 0 ; j < dataJsonArray.length() ; j++){
                            JSONObject dataJsonobject = dataJsonArray.getJSONObject(j);
                            PayTypeBean bean = gson.fromJson(dataJsonobject.toString() , PayTypeBean.class);
                            list.add(bean);
                        }
                        itemList.add(list);
                    }
                    handler.obtainMessage(200).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        onClickListener = null;
        typeList = null;
        itemList = null;
        System.gc();
        super.onDestroy();
    }


}


