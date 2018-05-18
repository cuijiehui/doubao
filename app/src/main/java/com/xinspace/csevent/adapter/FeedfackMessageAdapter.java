package com.xinspace.csevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.EmailForSysMsgEntity;

import java.util.List;

/**
 * Created by lizhihong on 2015/11/26.
 * 推送消息的adapter
 */
public class FeedfackMessageAdapter extends BaseAdapter{

    Context context;
    private List<Object> list;
    LayoutInflater inflater;

    public FeedfackMessageAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;

        this.inflater=LayoutInflater.from(context);
    }
    //更新数据
    public void updateData(List<Object> list){
        this.list=list;
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.item_email,null);
            holder.tvMsg= (TextView) convertView.findViewById(R.id.tv_item_email_sys_msg_txt);
            holder.tvTime= (TextView) convertView.findViewById(R.id.tv_email_sys_time);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();

        //数据
        EmailForSysMsgEntity enty = (EmailForSysMsgEntity) list.get(position);
        holder.tvMsg.setText(enty.getFeedback());
        holder.tvTime.setText(enty.getDate());
        return convertView;
    }

    class ViewHolder{
        TextView tvMsg;
        TextView tvTime;
    }
}
