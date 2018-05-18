package com.xinspace.csevent.monitor.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinspace.csevent.monitor.adapter.IdentityAdapter;
import com.xinspace.csevent.monitor.weiget.ReturnPriceListener;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/7/19.
 */

public class LeaseRentPop extends PopupWindow{

    private Context context;
    private View view;
    private MyListView lv_lease_rent;
    private EditText ed_min;
    private EditText ed_max;
    private TextView tv_ok;
    private String minPrice;
    private String maxPrice;
    private List<String> list = new ArrayList<>();
    private IdentityAdapter adapter;
    private ReturnPriceListener listener;

    public LeaseRentPop(Context context , ReturnPriceListener listener) {
        this.context = context;
        this.listener = listener;
        view = LayoutInflater.from(context).inflate(R.layout.pop_lease_rent, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dpToPx(context , 146));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);

        initView();
        initData();
    }

    private void initData() {

        list.add("不限");
        list.add("0~1500");
        list.add("1500~2500");
        list.add("2500~3500");
        list.add("3500~4500");
        list.add("4500以上");

    }

    private void initView() {

        lv_lease_rent = (MyListView) view.findViewById(R.id.lv_lease_rent);
        ed_min = (EditText) view.findViewById(R.id.ed_min);
        ed_max = (EditText) view.findViewById(R.id.ed_max);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(onClickListener);

        adapter = new IdentityAdapter(context);
        adapter.setList(list);
        lv_lease_rent.setAdapter(adapter);

        lv_lease_rent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    ed_min.setText("");
                    ed_max.setText("");
                }

                if (position == 1){
                    ed_min.setText("0");
                    ed_max.setText("1500");
                }

                if (position == 2){
                    ed_min.setText("1500");
                    ed_max.setText("2500");
                }

                if (position == 3){
                    ed_min.setText("2500");
                    ed_max.setText("3500");

                }

                if (position == 4){
                    ed_min.setText("3500");
                    ed_max.setText("4500");
                }

                if (position == 5){
                    ed_min.setText("4500");
                    ed_max.setText("~~~");
                }

                adapter.setSelectItem(position);
                adapter.notifyDataSetChanged();

            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_ok:
                    minPrice = ed_min.getText().toString().trim();
                    maxPrice = ed_max.getText().toString().trim();

                    listener.returnPrice(minPrice , maxPrice);
                    LeaseRentPop.this.dismiss();
                    break;
            }
        }
    };


}
