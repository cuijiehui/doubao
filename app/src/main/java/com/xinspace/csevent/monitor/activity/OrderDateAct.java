package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xinspace.csevent.monitor.adapter.DateAdapter;
import com.xinspace.csevent.monitor.view.MonthDateView;
import com.xinspace.csevent.monitor.view.MyGridView;
import com.xinspace.csevent.monitor.view.WeekDayView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/7/20.
 */

public class OrderDateAct extends BaseActivity{

    private TextView tv_left;
    private TextView tv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;
    private WeekDayView weekDayView;
    private TimePicker time_picker;
    private LinearLayout lin_book_tel;
    private List<String> timeList = new ArrayList<>();

    List<Integer> list = new ArrayList<Integer>();
    private String clickDay;
    private LinearLayout ll_order_date_back;

    private String[] date = new String[]{"06:00" , "07:00" , "08:00" , "09:00" , "10:00" , "11:00" , "12:00" , "13:00" , "14:00"
            , "15:00" , "16:00" , "17:00" , "18:00" , "19:00" , "20:00" , "21:00" , "22:00" , "23:00" , "24:00"};
    private MyGridView gv_time;
    private DateAdapter adapter;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(OrderDateAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_order_date);

        initData();
        initView();
    }

    private void initData() {
        for (int i = 0 ; i < date.length ; i++){
            timeList.add(date[i]);
        }
    }

    private void initView() {

        ll_order_date_back = (LinearLayout) findViewById(R.id.ll_order_date_back);
        ll_order_date_back.setOnClickListener(onClickListener);

        lin_book_tel = (LinearLayout) findViewById(R.id.lin_book_tel);
        lin_book_tel.setOnClickListener(onClickListener);

        clickDay = TimeHelper.getDate(String.valueOf(System.currentTimeMillis()));
        time_picker = (TimePicker) findViewById(R.id.time_picker);

        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setText("<");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText(">");

        weekDayView = (WeekDayView) findViewById(R.id.weekDayView);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);

        weekDayView.setmTopLineColor(Color.parseColor("#ffffff"));
        weekDayView.setmBottomLineColor(Color.parseColor("#ffffff"));
        weekDayView.setmWeedayColor(Color.parseColor("#ffffff"));
        weekDayView.setmWeekendColor(Color.parseColor("#ffffff"));

        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week  =(TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);

        tv_left.setFocusable(true);

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
            }
        });

        gv_time = (MyGridView) findViewById(R.id.gv_time);
        adapter = new DateAdapter(OrderDateAct.this);
        adapter.setList(timeList);
        gv_time.setAdapter(adapter);
        gv_time.setOnItemClickListener(itemClickListener);
    }


    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            adapter.setPos(position);
            adapter.notifyDataSetChanged();
            time = timeList.get(position);

        }
    };


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_order_date_back:
                    OrderDateAct.this.finish();
                    break;
                case R.id.tv_left:
                    monthDateView.onLeftClick();
                    //ToastUtil.makeToast("上月日期不可选");

                    break;
                case R.id.tv_right:
                    monthDateView.onRightClick();
                    break;
                case R.id.tv_today:
                    monthDateView.setTodayToView();
                    break;
                case R.id.lin_book_tel:  //提交

//                    if (time_picker.getMinute() < 10){
//                        time = time_picker.getHour() + ":0" + time_picker.getMinute();
//                    }else{
//                        time = time_picker.getHour() + ":" + time_picker.getMinute();
//                    }

                    Intent data = new Intent();
                    data.putExtra("clickDay", clickDay);
                    data.putExtra("time" , time);
                    setResult(1000, data);
                    finish();
                    break;
            }
        }
    };


}
