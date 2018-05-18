package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;

/**
 * Created by Android on 2017/6/2.
 */

public class IdentityAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private int clickPosition = -1;

    public void setList(List<String> list) {
        this.list = list;
    }

    public IdentityAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        ViewHodler viewHodler = null;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.item_community , null);
            viewHodler.tv_name = (TextView) convertView.findViewById(R.id.tv_community_name);
            convertView.setTag(viewHodler);
        }else{
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.tv_name.setText(list.get(position));

        viewHodler.tv_name.setTextColor(0xFF666666);
        if (position == clickPosition) {

            LogUtil.i("----------------position" + position + "clickPosition" + clickPosition);

            viewHodler.tv_name.setTextColor(0xFFFF8C00);
        }
        return convertView;
    }

    static class ViewHodler{
        TextView tv_name;
    }

}
