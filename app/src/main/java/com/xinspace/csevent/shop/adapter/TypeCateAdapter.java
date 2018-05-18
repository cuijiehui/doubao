package com.xinspace.csevent.shop.adapter;

import android.content.Context;
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

public class TypeCateAdapter extends BaseAdapter {

    private Context context;
    private List<ChildGoodsBean> list;
    private LayoutInflater inflater;

    public TypeCateAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.item_group_cate, null);
            holder.iv_cate= (ImageView) convertView.findViewById(R.id.iv_cate);
            holder.tv_cate= (TextView) convertView.findViewById(R.id.tv_cate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChildGoodsBean bean = list.get(position);
        ImagerLoaderUtil.displayImageWithLoadingIcon(bean.getThumb() , holder.iv_cate , R.drawable.goods_loading);
        holder.tv_cate.setText(bean.getName());

        return convertView;
    }


    static class ViewHolder {

        ImageView iv_cate;
        TextView tv_cate;

    }

}
