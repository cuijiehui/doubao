package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.SeckillTimeBean;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;

/**
 * Created by Android on 2017/6/7.
 */

public class SeckillTimeAdapter extends BaseAdapter {

    private Context context;
    private List<SeckillTimeBean> timesList;
    private LayoutInflater inflater;
    private int clickPosition = 0;

    public SeckillTimeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    public void setTimesList(List<SeckillTimeBean> timesList) {
        this.timesList = timesList;
    }

    @Override
    public int getCount() {
        return timesList.size();
    }

    @Override
    public Object getItem(int position) {
        return timesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectItem(int position) {
        LogUtil.i("------------clickPosition-----------------");
        this.clickPosition = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_seckill_time, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SeckillTimeBean bean = timesList.get(position);
        holder.tv_time.setText(bean.getTime() + ":00");

        String status = bean.getStatus();
        if (status.equals("0")) {
            holder.tv_state.setText("抢购中");
        } else if (status.equals("1")) {
            holder.tv_state.setText("未开抢");
        }

        LogUtil.i("clickPostion" + clickPosition + "position" + position);
        holder.tv_time.setTextColor(Color.parseColor("#4a4a4a"));
        holder.tv_state.setTextColor(Color.parseColor("#4a4a4a"));
        if (position == clickPosition) {
            LogUtil.i("----------------position" + position + "clickPosition" + clickPosition);
            holder.tv_time.setTextColor(Color.parseColor("#ea5205"));
            holder.tv_state.setTextColor(Color.parseColor("#ea5205"));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_time;
        TextView tv_state;
    }
}
