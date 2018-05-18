package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.GoodsTypeBean;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;

/**
 * 此类为菜单适配器
 */
public class GoodTypeAdapter extends BaseAdapter{

    Context context;
    private List<GoodsTypeBean> list;
    LayoutInflater inflater;

    private int pos = -1;

    public void setPos(int pos) {
        this.pos = pos;
        list.get(pos).setSelect(true);
    }

    public GoodTypeAdapter(Context context) {
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    public void setList(List<GoodsTypeBean> list) {

        LogUtil.i("---------list-------------" + list.size());

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
        view = inflater.inflate(R.layout.item_menu,null);
        TextView tvTxt= (TextView) view.findViewById(R.id.item_menu_txt);

        tvTxt.setText(list.get(position).getName());
        if(position == pos){
            tvTxt.setTextColor(Color.parseColor("#ffffff"));
            tvTxt.setBackgroundColor(Color.parseColor("#ea5205"));
        }else{
            tvTxt.setTextColor(Color.parseColor("#333333"));
            tvTxt.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return view;
    }
}
