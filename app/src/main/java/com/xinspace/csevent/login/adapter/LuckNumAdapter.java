package com.xinspace.csevent.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;

import java.util.List;

/**
 * Created by Android on 2016/10/18.
 */
public class LuckNumAdapter extends BaseAdapter{

    private Context context;
    private List<String> luckNum;


    public LuckNumAdapter(Context context) {
        this.context = context;
    }

    public void setLuckNum(List<String> luckNum) {
        this.luckNum = luckNum;
    }

    @Override
    public int getCount() {
        return luckNum.size();
    }

    @Override
    public Object getItem(int position) {
        return luckNum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_luck_num, null);
            holder.tv_luck_num = (TextView) convertView.findViewById(R.id.tv_luck_num);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_luck_num.setText(luckNum.get(position));

        return convertView;
    }


    class ViewHolder{
        TextView tv_luck_num;
    }


}
