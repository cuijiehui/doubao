package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.OptionBean;
import com.xinspace.csevent.shop.modle.SpecBean;
import java.util.List;

/**
 * Created by Android on 2017/5/2.
 */

public class SpecAdapter extends BaseAdapter{

    private Context context;
    private List<OptionBean> specList;

    public SpecAdapter(Context context) {
        this.context = context;
    }

    public void setSpecList(List<OptionBean> specList) {
        this.specList = specList;
    }

    @Override
    public int getCount() {
        return specList.size();
    }

    @Override
    public Object getItem(int position) {
        return specList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goods_spec , null);
            viewHodler.tv_spec = (TextView) convertView.findViewById(R.id.tv_spec);
            convertView.setTag(viewHodler);
        }else{
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.tv_spec.setText(specList.get(position).getTitle());
        return convertView;
    }

    class ViewHodler{
        TextView tv_spec;
    }
}
