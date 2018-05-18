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
import com.xinspace.csevent.shop.modle.ChildGoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;


/**
 * 拼团的分类
 *
 * Created by Android on 2017/6/8.
 */

public class TypeCateAdapter2 extends BaseAdapter {

    private Context context;
    private List<ChildGoodsBean> list;
    private LayoutInflater inflater;
    private int pos = -1;

    public void setPos(int pos) {
        this.pos = pos;
    }

    public TypeCateAdapter2(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<ChildGoodsBean> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_menu, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChildGoodsBean bean = list.get(position);

        TextView tvTxt= (TextView) convertView.findViewById(R.id.item_menu_txt);
        tvTxt.setText((list.get(position)).getName());

        if(position == pos){
            tvTxt.setTextColor(Color.parseColor("#ffffff"));
            tvTxt.setBackgroundColor(Color.parseColor("#ea5205"));
        }else{
            tvTxt.setTextColor(Color.parseColor("#333333"));
            tvTxt.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return convertView;
    }


    static class ViewHolder {

        TextView tv_cate;

    }

}
