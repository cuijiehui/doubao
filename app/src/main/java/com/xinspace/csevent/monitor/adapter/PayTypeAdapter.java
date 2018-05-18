package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.monitor.bean.TypeBean;
import com.xinspace.csevent.R;

import java.util.List;


/**
 * Created by Android on 2017/5/26.
 */

public class PayTypeAdapter extends BaseAdapter{

    private List<TypeBean> list;
    private Context content;

    public PayTypeAdapter(Context content) {
        this.content = content;
    }

    public void setList(List<TypeBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(content).inflate(R.layout.item_pay_type , null);
            viewHodler.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHodler);
        }else{
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.tv_name.setText(list.get(position).getName());

        return convertView;
    }

    static class ViewHodler{
        TextView tv_name;
    }


}
