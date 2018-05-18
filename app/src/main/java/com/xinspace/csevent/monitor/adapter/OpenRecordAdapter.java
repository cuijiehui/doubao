package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinspace.csevent.monitor.bean.OpenLockBean;
import com.xinspace.csevent.R;

import java.util.List;


/**
 * 开门记录
 *
 * Created by Android on 2017/4/6.
 */

public class OpenRecordAdapter extends RecyclerView.Adapter<OpenRecordAdapter.ViewHolder>{

    private Context context;
    private List<OpenLockBean> list;
    private LayoutInflater mIflater;


    public OpenRecordAdapter(Context context , List<OpenLockBean> list) {
        this.context = context;
        this.list = list;
        mIflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OpenRecordAdapter.ViewHolder holder = new OpenRecordAdapter.ViewHolder(mIflater.inflate(R.layout.item_open_door_record , parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OpenLockBean bean = list.get(position);

        holder.tv_time.setText(bean.getCreate_time());
        holder.tv_name.setText(bean.getName());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_time;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

}
