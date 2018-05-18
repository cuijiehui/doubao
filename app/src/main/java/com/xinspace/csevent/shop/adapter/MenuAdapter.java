package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.ActivityClassEntity;
import com.xinspace.csevent.shop.activity.AwardsTypeActivity;

import java.util.List;

/**
 * Created by lizhihong on 2015/11/26.
 * 此类为菜单适配器
 */
public class MenuAdapter extends BaseAdapter{

    Context context;
    private List<Object> list;
    LayoutInflater inflater;

    public MenuAdapter(Context context,List<Object> list) {
        this.context = context;
        this.list = list;

        this.inflater=LayoutInflater.from(context);
    }
    //更新数据
    public void updateData(List<Object> list){
        this.list=list;
        notifyDataSetChanged();
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
        view = inflater.inflate(R.layout.item_menu,null);
        TextView tvTxt= (TextView) view.findViewById(R.id.item_menu_txt);
        tvTxt.setText(((ActivityClassEntity)list.get(position)).getName());
        int pos = ((AwardsTypeActivity)context).currentItem;
        if(position == pos){
            view.setBackgroundResource(R.drawable.menu_line_icon);
            tvTxt.setTextColor(Color.parseColor("#ea8010"));
        }
        return view;
    }
}
