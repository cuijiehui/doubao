package com.xinspace.csevent.monitor.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinspace.csevent.monitor.adapter.LeaseMoreAdapter;
import com.xinspace.csevent.monitor.view.MyGridView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  租赁筛选条件更多
 *
 * Created by Android on 2017/7/19.
 */

public class LeaseMorePop extends PopupWindow{

    private Context context;
    private View view;
    private TextView tv_ok;
    private TextView tv_cancel;
    private List<String> wayList = new ArrayList<>();
    private List<String> payWayList = new ArrayList<>();
    private List<String> orientationList = new ArrayList<>();
    private String[] orientations = new String[]{"朝南" , "朝北" , "朝东" , "朝西" , "南北" , "东西" , "东南" , "西南" , "东北" , "西北"};

    private MyGridView gv_lease_way;
    private MyGridView gv_lease_way_money;
    private MyGridView gv_room_orientation;

    private LeaseMoreAdapter adapter1;
    private LeaseMoreAdapter adapter2;
    private LeaseMoreAdapter adapter3;
    private ReturnMoreListener moreListener;

    private String orientation;
    private String rent_type;
    private String charge_pay;

    public LeaseMorePop(Context context , ReturnMoreListener moreListener) {
        this.context = context;
        this.moreListener = moreListener;
        view = LayoutInflater.from(context).inflate(R.layout.pop_more_rent, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dpToPx(context , 146));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);

        initData();
        initView();
    }

    private void initData() {

        wayList.add("整租");
        wayList.add("合租");
        wayList.add("公寓");

        payWayList.add("压一付一");
        payWayList.add("压二付一");
        payWayList.add("押一付三");

        for (int i = 0 ; i < orientations.length ; i++ ){
            orientationList.add(orientations[i]);
        }
    }

    private void initView() {

        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(onClickListener);

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(onClickListener);

        gv_lease_way = (MyGridView) view.findViewById(R.id.gv_lease_way);
        gv_lease_way_money = (MyGridView) view.findViewById(R.id.gv_lease_way_money);
        gv_room_orientation = (MyGridView) view.findViewById(R.id.gv_room_orientation);

        adapter1 = new LeaseMoreAdapter(context);
        adapter1.setList(wayList);
        gv_lease_way.setAdapter(adapter1);
        gv_lease_way.setOnItemClickListener(onItemClickListener1);


        adapter2 = new LeaseMoreAdapter(context);
        adapter2.setList(payWayList);
        gv_lease_way_money.setAdapter(adapter2);
        gv_lease_way_money.setOnItemClickListener(onItemClickListener2);

        adapter3 = new LeaseMoreAdapter(context);
        adapter3.setList(orientationList);
        gv_room_orientation.setAdapter(adapter3);
        gv_room_orientation.setOnItemClickListener(onItemClickListener3);
    }


    AdapterView.OnItemClickListener onItemClickListener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            rent_type = wayList.get(position);

            adapter1.setPos(position);
            adapter1.notifyDataSetChanged();

        }
    };


    AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            charge_pay = payWayList.get(position);
            adapter2.setPos(position);
            adapter2.notifyDataSetChanged();
        }
    };


    AdapterView.OnItemClickListener onItemClickListener3= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            orientation = orientationList.get(position);
            adapter3.setPos(position);
            adapter3.notifyDataSetChanged();

        }
    };





    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_ok:

                    moreListener.returnPrice(orientation , rent_type , charge_pay);
                    LeaseMorePop.this.dismiss();

                    break;
                case R.id.tv_cancel:

                    adapter1.setPos(-1);
                    adapter2.setPos(-1);
                    adapter3.setPos(-1);

                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    adapter3.notifyDataSetChanged();

                    LeaseMorePop.this.dismiss();

                    break;
            }
        }
    };


}
