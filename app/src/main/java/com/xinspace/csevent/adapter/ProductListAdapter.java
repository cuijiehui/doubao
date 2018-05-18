package com.xinspace.csevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

public class ProductListAdapter extends BaseAdapter{
    private Context context;
    private List<Object> list;
    private LayoutInflater inflater;


    public ProductListAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }

    //更新数据
    public void updateData(List<Object> list){
        this.list=list;
        this.notifyDataSetChanged();
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
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = inflater.inflate(R.layout.item_dujia_activity_list, null);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_act_name);
            holder.tv_detail= (TextView) convertView.findViewById(R.id.tv_act_detail);

            holder.iv_photo= (ImageView) convertView.findViewById(R.id.iv_main_act_icon);
            holder.iv_hot= (ImageView) convertView.findViewById(R.id.iv_dujia_hot);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        //为了重用性,每次重用之前都将hot标记隐藏
        holder.iv_hot.setVisibility(View.GONE);

        //数据
        ActivityListEntity enty = (ActivityListEntity) list.get(position);
        String name=enty.getName();
        String url=enty.getImage();
        String remark=enty.getAbbreviation();
        int is_hot=enty.getIs_top();

        //显示热门标记
        if(is_hot==1){
            holder.iv_hot.setVisibility(View.VISIBLE);
        }
        holder.tv_title.setText(name);
        holder.tv_detail.setText(remark);

        ImagerLoaderUtil.displayImage(url,holder.iv_photo);
        return convertView;
    }

    class ViewHolder{
        ImageView iv_hot;
        ImageView iv_photo;
        TextView tv_title;
        TextView tv_detail;
    }
}
