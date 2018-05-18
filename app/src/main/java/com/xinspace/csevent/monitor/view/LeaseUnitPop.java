package com.xinspace.csevent.monitor.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.monitor.adapter.IdentityAdapter;
import com.xinspace.csevent.monitor.weiget.IdentityResultListener;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/7/19.
 */

public class LeaseUnitPop extends PopupWindow{

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
    private RelativeLayout rel_bottom;
    private IdentityResultListener listener;
    private String type = "0";

    public LeaseUnitPop(Context context , IdentityResultListener listener) {
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
        list.add("一室");
        list.add("两室");
        list.add("三室");
        list.add("四室");
        list.add("四室以上");

    }

    private void initView() {

        rel_bottom = (RelativeLayout) view.findViewById(R.id.rel_bottom);
        rel_bottom.setVisibility(View.GONE);

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

                    type = "0";

                }

                if (position == 1){

                    type = "1";

                }

                if (position == 2){

                    type = "2";

                }

                if (position == 3){

                    type = "3";

                }

                if (position == 4){

                    type = "4";

                }

                if (position == 5){

                    type = "5";

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

                    listener.identityResult(type);
                    LeaseUnitPop.this.dismiss();

                    break;
            }
        }
    };


}
