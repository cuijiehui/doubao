package com.xinspace.csevent.monitor.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.PayMentRecordAdapter;
import com.xinspace.csevent.monitor.bean.PayRecordBean;
import com.xinspace.csevent.monitor.bean.TypeBean;
import com.xinspace.csevent.monitor.weiget.PayTypePop;
import com.xinspace.csevent.monitor.weiget.ReturnTypeListener;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 缴费记录
 *
 * Created by Android on 2017/5/26.
 */

public class PayMentRecordAct extends BaseActivity{

    private LinearLayout ll_record_back;
    private TextView tv_payment_cate;
    private LinearLayout lin_all,lin_has_pay,lin_pay_no;

    private TextView tv_all , tv_all_line;
    private TextView tv_has_pay , tv_has_pay_line;
    private TextView tv_pay_no , tv_pay_no_line;

    private String status = "";
    private String cate_id = "";

    private SDPreference preference;
    private String cUid;
    private String cToken;
    private int page;
    private List<PayRecordBean> allBeanList;

    private PayMentRecordAdapter adapter;
    private RecyclerView rv_pay_record;
    private PayTypePop payTypePop;
    private LinearLayout lin_payment_cate;
    private ImageView iv_indicate;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allBeanList.addAll((Collection<? extends PayRecordBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 400:

                    break;
            }
        }
    };
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_payment_record);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);

        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        preference = SDPreference.getInstance();
        cToken = preference.getContent("cToken");
        cUid = preference.getContent("cUid");

        allBeanList = new ArrayList<>();

        initView();
        clickAll();
    }

    private void initView() {

        page = 1;

        ll_record_back = (LinearLayout) findViewById(R.id.ll_record_back);
        ll_record_back.setOnClickListener(clickListener);

        lin_payment_cate = (LinearLayout) findViewById(R.id.lin_payment_cate);
        lin_payment_cate.setOnClickListener(clickListener);

        tv_payment_cate = (TextView) findViewById(R.id.tv_payment_cate);
        iv_indicate = (ImageView) findViewById(R.id.iv_indicate);
        iv_indicate.setImageResource(R.drawable.icon_group_right);

        lin_all = (LinearLayout) findViewById(R.id.lin_all);
        lin_has_pay = (LinearLayout) findViewById(R.id.lin_has_pay);
        lin_pay_no = (LinearLayout) findViewById(R.id.lin_pay_no);

        lin_all.setOnClickListener(clickListener);
        lin_has_pay.setOnClickListener(clickListener);
        lin_pay_no.setOnClickListener(clickListener);

        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_all_line = (TextView) findViewById(R.id.tv_all_line);

        tv_has_pay = (TextView) findViewById(R.id.tv_has_pay);
        tv_has_pay_line = (TextView) findViewById(R.id.tv_has_pay_line);

        tv_pay_no = (TextView) findViewById(R.id.tv_pay_no);
        tv_pay_no_line = (TextView) findViewById(R.id.tv_pay_no_line);

        rv_pay_record = (RecyclerView) findViewById(R.id.rv_pay_record);
        rv_pay_record.setLayoutManager(new LinearLayoutManager(PayMentRecordAct.this, LinearLayoutManager.VERTICAL, false));
        adapter = new PayMentRecordAdapter(PayMentRecordAct.this , allBeanList);
        rv_pay_record.setAdapter(adapter);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_record_back:

                    finish();

                    break;
                case R.id.lin_payment_cate:

                    showPop();

                    break;
                case R.id.lin_all: // 全部缴费记录

                    clickAll();

                    break;
                case R.id.lin_has_pay:

                    clickHasPay(); // 已缴费记录

                    break;
                case R.id.lin_pay_no:

                    clickNoPay();  // 未缴费记录

                    break;
            }
        }
    };

    private void clickAll(){
        status = "";
        tv_all.setTextColor(Color.parseColor("#ea5205"));
        tv_all_line.setBackgroundColor(Color.parseColor("#ea5205"));
        tv_has_pay.setTextColor(Color.parseColor("#4a4a4a"));
        tv_has_pay_line.setBackgroundColor(Color.parseColor("#eeeeee"));
        tv_pay_no.setTextColor(Color.parseColor("#4a4a4a"));
        tv_pay_no_line.setBackgroundColor(Color.parseColor("#eeeeee"));
        page = 1;
        allBeanList.clear();
        initData();
    }

    private void clickHasPay(){
        status = "2";
        tv_all.setTextColor(Color.parseColor("#4a4a4a"));
        tv_all_line.setBackgroundColor(Color.parseColor("#eeeeee"));
        tv_has_pay.setTextColor(Color.parseColor("#ea5205"));
        tv_has_pay_line.setBackgroundColor(Color.parseColor("#ea5205"));
        tv_pay_no.setTextColor(Color.parseColor("#4a4a4a"));
        tv_pay_no_line.setBackgroundColor(Color.parseColor("#eeeeee"));
        page = 1;
        allBeanList.clear();
        initData();
    }

    private void clickNoPay(){
        status = "1";

        tv_all.setTextColor(Color.parseColor("#4a4a4a"));
        tv_all_line.setBackgroundColor(Color.parseColor("#eeeeee"));

        tv_has_pay.setTextColor(Color.parseColor("#4a4a4a"));
        tv_has_pay_line.setBackgroundColor(Color.parseColor("#eeeeee"));

        tv_pay_no.setTextColor(Color.parseColor("#ea5205"));
        tv_pay_no_line.setBackgroundColor(Color.parseColor("#ea5205"));

        page = 1;
        allBeanList.clear();
        initData();
    }

    private void initData() {

        GetDataBiz.getPaymentRecordData(area, cUid, cToken, String.valueOf(page), status, cate_id, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("缴费记录的列表" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                List<PayRecordBean> list = new ArrayList<PayRecordBean>();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject dataJsonObject = jsonArray.getJSONObject(i);
                        PayRecordBean bean = gson.fromJson(dataJsonObject.toString() , PayRecordBean.class);
                        list.add(bean);
                    }
                    handler.obtainMessage(200 , list).sendToTarget();
                }else{
                    handler.obtainMessage(200 , list).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void showPop(){

        if (payTypePop == null) {
            payTypePop = new PayTypePop(PayMentRecordAct.this , returnTypeListener);
        }

        if (!payTypePop.isShowing()) {
            payTypePop.showAtLocation(tv_payment_cate, Gravity.BOTTOM, 0, 0);

//            iv_indicate.setImageResource(R.drawable.icon_group_right);
//
//            WindowManager manager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            //获取xoff
//            int xpos = manager.getDefaultDisplay().getWidth()/2 - payTypePop.getWidth()/2;
//            //xoff,yoff基于anchor的左下角进行偏移。
//            payTypePop.showAsDropDown(lin_payment_cate, xpos, 0);
            payTypePop.isShowing();
        } else {
            //iv_indicate.setImageResource(R.drawable.icon_group_down);
            payTypePop.dismiss();
        }

        payTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                payTypePop = null;
            }
        });
    }

    ReturnTypeListener returnTypeListener = new ReturnTypeListener() {
        @Override
        public void returnBean(TypeBean typeBean) {
            cate_id = typeBean.getId();
            clickAll();
            payTypePop.dismiss();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (payTypePop != null){
            payTypePop.dismiss();
        }else{
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        clickListener = null;
        payTypePop = null;
        returnTypeListener = null;
        System.gc();
        super.onDestroy();
    }

}
