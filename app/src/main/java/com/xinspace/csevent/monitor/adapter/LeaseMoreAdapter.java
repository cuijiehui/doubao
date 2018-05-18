package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;

/**
 * 租赁更多适配器
 *
 * Created by Android on 2017/6/28.
 */

public class LeaseMoreAdapter extends BaseAdapter{

    private List<String> list;
    private Context context;
    LayoutInflater inflater;
    private int pos = -1;

    public void setPos(int pos) {
        this.pos = pos;
    }

    public LeaseMoreAdapter(Context context) {
        this.context = context;
        this.inflater= LayoutInflater.from(context);
    }

    public void setList(List<String> list) {

        LogUtil.i("---------顶部筛选list-------------" + list.size());
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.item_pop_screen,null);

//        final FilterBean filterBean = list.get(position);
//        final boolean isSelect = filterBean.isSelect();

        TextView tvTxt= (TextView) view.findViewById(R.id.tv_screen);
        tvTxt.setText(list.get(position));

//        if (isSelect){
//            tvTxt.setTextColor(Color.parseColor("#ffffff"));
//            tvTxt.setBackgroundResource(R.drawable.tv_screen_press_shape);
//        }else{
//            tvTxt.setTextColor(Color.parseColor("#333333"));
//            tvTxt.setBackgroundResource(R.drawable.tv_screen_shape);
//        }
//
//        tvTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                filterBean.setSelect(!isSelect);
//                notifyDataSetChanged();
//            }
//        });

        if(position == pos){
            tvTxt.setTextColor(Color.parseColor("#ffffff"));
            tvTxt.setBackgroundResource(R.drawable.tv_screen_press_shape);
        }else{
            tvTxt.setTextColor(Color.parseColor("#333333"));
            tvTxt.setBackgroundResource(R.drawable.tv_screen_shape);
        }

        return view;
    }
}
