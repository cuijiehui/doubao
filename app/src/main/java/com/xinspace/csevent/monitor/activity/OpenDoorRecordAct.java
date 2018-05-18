package com.xinspace.csevent.monitor.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.OpenRecordAdapter;
import com.xinspace.csevent.monitor.bean.OpenLockBean;
import com.xinspace.csevent.monitor.view.MonthDateView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.TimeHelper;
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
import java.util.Collection;
import java.util.List;

import sdk_sample.sdk.bean.LockRecordBean;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * Created by Android on 2017/3/18.
 *
 * 选择日期查询开门记录
 */

public class OpenDoorRecordAct extends BaseActivity{

    private LinearLayout ll_open_record;
    private SDPreference preference;
    private String cUid;
    private String cToken;
    private List<OpenLockBean> allList = new ArrayList<>();
    private RecyclerView rv_open_record;
    private OpenRecordAdapter adapter;

    private TextView tv_left;
    private TextView tv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;
    List<Integer> list = new ArrayList<Integer>();
    private String clickDay;
    private String nowDay;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends OpenLockBean>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 400:
                    ToastUtil.makeToast("暂无开门记录");
                    break;
            }
        }
    };
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_open_record);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        preference = SDPreference.getInstance();
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");

        cUid = preference.getContent("cUid");
        cToken = preference.getContent("cToken");
        nowDay = TimeHelper.getDate(String.valueOf(System.currentTimeMillis()));

        initView();
        initData(nowDay);
    }

    private void initData(String date) {

        LockRecordBean bean = new LockRecordBean();
        bean.setToken(cToken);
        bean.setUid(cUid);
        bean.setDataDay(date);

        GetDataBiz.GET_LOCK_RECORD(area, bean, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("开门记录的返回值" + result);
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                Gson gson = new Gson();

                if (jsonObject.getInt("code") == 200){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    List<OpenLockBean> list = new ArrayList<OpenLockBean>();
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject beanObject = jsonArray.getJSONObject(i);
                        OpenLockBean bean = gson.fromJson(beanObject.toString() , OpenLockBean.class);
                        list.add(bean);
                    }
                    handler.obtainMessage(200 , list).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void initView() {

        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setText("<");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText(">");

        ll_open_record = (LinearLayout) findViewById(R.id.ll_open_record);
        ll_open_record.setOnClickListener(onClickListener);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week  =(TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);

        tv_left.setOnClickListener(onClickListener);
        tv_right.setOnClickListener(onClickListener);
        tv_today.setOnClickListener(onClickListener);

        monthDateView.setTextView(tv_date,tv_week);
        monthDateView.setDaysHasThingList(list);
        monthDateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
                //Toast.makeText(getApplication(), "点击了：" +monthDateView.getmSelYear() + "-"+ monthDateView.getmSelMonth() +"-"+monthDateView.getmSelDay(), Toast.LENGTH_SHORT).show();


                if (monthDateView.getmSelMonth() < 10 ){
                    if (monthDateView.getmSelDay() < 10 ){
                        clickDay = monthDateView.getmSelYear() + "-0"+ monthDateView.getmSelMonth() +"-0"+monthDateView.getmSelDay();
                    }else{
                        clickDay = monthDateView.getmSelYear() + "-0"+ monthDateView.getmSelMonth() +"-"+monthDateView.getmSelDay();
                    }
                }else{
                    if (monthDateView.getmSelDay() < 10 ){
                        clickDay = monthDateView.getmSelYear() + "-"+ monthDateView.getmSelMonth() +"-0"+monthDateView.getmSelDay();
                    }else{
                        clickDay = monthDateView.getmSelYear() + "-"+ monthDateView.getmSelMonth() +"-"+monthDateView.getmSelDay();
                    }
                }

               // clickDay = monthDateView.getmSelYear() + "-"+ monthDateView.getmSelMonth() +"-"+monthDateView.getmSelDay();
                if (allList.size() != 0){
                    allList.clear();
                    adapter.notifyDataSetChanged();
                }
                initData(clickDay);
            }
        });

        rv_open_record = (RecyclerView) findViewById(R.id.rv_open_record);
        adapter = new OpenRecordAdapter(OpenDoorRecordAct.this , allList);
        rv_open_record.setLayoutManager(new LinearLayoutManager(OpenDoorRecordAct.this, LinearLayoutManager.VERTICAL, false));
        rv_open_record.setAdapter(adapter);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_open_record:
                    OpenDoorRecordAct.this.finish();
                    break;
                case R.id.tv_left:
                    monthDateView.onLeftClick();
                    break;
                case R.id.tv_right:
                    monthDateView.onRightClick();
                    break;
                case R.id.tv_today:
                    monthDateView.setTodayToView();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        this.setContentView(R.layout.empty_view);
        onClickListener = null;
        System.gc();
        super.onDestroy();
    }
}
