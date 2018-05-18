package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.ExpressBean;

import java.util.List;

/**
 * Created by Android on 2017/5/20.
 */

public class ExpressAdapter extends BaseAdapter{

    private List<ExpressBean> allList;
    private Context context;

    public ExpressAdapter(Context context , List<ExpressBean> allList) {
        this.allList = allList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return allList.size();
    }

    @Override
    public Object getItem(int position) {
        return allList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_express , null);
            viewHodler.time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHodler.content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHodler.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHodler);
        }else{
            viewHodler = (ViewHodler) convertView.getTag();
        }

        if (position == 0){
            viewHodler.time.setText(allList.get(position).getTime());
            viewHodler.time.setTextColor(Color.parseColor("#00b09c"));
            viewHodler.content.setText(allList.get(position).getStep());
            viewHodler.content.setTextColor(Color.parseColor("#00b09c"));
            viewHodler.image.setImageResource(R.drawable.icon_express_1);
        }else{
            viewHodler.time.setText(allList.get(position).getTime());
            viewHodler.time.setTextColor(Color.parseColor("#4a4a4a"));
            viewHodler.content.setText(allList.get(position).getStep());
            viewHodler.content.setTextColor(Color.parseColor("#4a4a4a"));
        }

        return convertView;
    }

    static class ViewHodler{

        TextView time;
        TextView content;
        ImageView image;

    }
}
