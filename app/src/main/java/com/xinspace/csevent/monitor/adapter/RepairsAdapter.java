package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinspace.csevent.monitor.bean.RepairsBean;
import com.xinspace.csevent.monitor.view.MultiImageView;
import com.xinspace.csevent.R;

import java.util.List;

/**
 * Created by Android on 2017/5/22.
 */

public class RepairsAdapter extends RecyclerView.Adapter<RepairsAdapter.MyViewHolder>{

    private Context mContext;
    private LayoutInflater mIflater;
    private List<RepairsBean> list;

    public RepairsAdapter(Context mContext,  List<RepairsBean> list) {
        this.mContext = mContext;
        this.list = list;
        mIflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RepairsAdapter.MyViewHolder holder = new RepairsAdapter.MyViewHolder(mIflater.inflate(R.layout.item_repairs_record , parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RepairsBean bean = list.get(position);
        holder.tv_community_name.setText(bean.getName());
        holder.tv_repairs_content.setText(bean.getDescribe());
        holder.tv_time.setText(bean.getCreate_time());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_community_name;
        MultiImageView img_transpond;
        TextView tv_repairs_content;
        TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_community_name = (TextView) itemView.findViewById(R.id.tv_community_name);
            tv_repairs_content = (TextView) itemView.findViewById(R.id.tv_repairs_content);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            img_transpond = (MultiImageView) itemView.findViewById(R.id.img_transpond);
        }
    }
}
