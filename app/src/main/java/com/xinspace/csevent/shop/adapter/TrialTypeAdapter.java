package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.SeckillTimeBean;
import com.xinspace.csevent.shop.modle.TrialTypeBean;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;

/**
 * Created by Android on 2017/6/7.
 */

public class TrialTypeAdapter extends BaseAdapter {

    private Context context;
    private List<TrialTypeBean> list;
    private LayoutInflater inflater;
    private int clickPosition = 0;

    public TrialTypeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<TrialTypeBean> list) {
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

    public void setSelectItem(int position) {
        this.clickPosition = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_trial_type, null);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TrialTypeBean bean = list.get(position);

        holder.tv_type.setText(bean.getName());
        holder.tv_type.setTextColor(Color.parseColor("#4a4a4a"));

        if (position == clickPosition) {
            holder.tv_type.setTextColor(Color.parseColor("#ea5205"));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_type;
    }
}
