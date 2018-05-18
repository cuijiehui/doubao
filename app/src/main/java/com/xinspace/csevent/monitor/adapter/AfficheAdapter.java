package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinspace.csevent.monitor.bean.AfficheBean;
import com.xinspace.csevent.R;

import java.util.List;


/**
 * Created by Android on 2017/4/6.
 */

public class AfficheAdapter extends RecyclerView.Adapter<AfficheAdapter.ViewHolder>{

    private Context context;
    private List<AfficheBean> list;
    private LayoutInflater mIflater;


    public AfficheAdapter(Context context , List<AfficheBean> list) {
        this.context = context;
        this.list = list;
        mIflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AfficheAdapter.ViewHolder holder = new AfficheAdapter.ViewHolder(mIflater.inflate(R.layout.item_affiche , parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AfficheBean afficheBean = list.get(position);

        holder.tv_affiche_time.setText(afficheBean.getCreate_time());
        holder.tv_affiche_content.setText(afficheBean.getContent());
        holder.tv_affiche_title.setText(afficheBean.getTitle());

        holder.rel_read_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context , "点击阅读原文了", Toast.LENGTH_SHORT).show();
            }
        });

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

        TextView tv_affiche_time;
        TextView tv_affiche_content;
        ImageView img_affiche_pic;
        TextView tv_affiche_title;
        RelativeLayout rel_read_all;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_affiche_time = (TextView) itemView.findViewById(R.id.tv_affiche_time);
            tv_affiche_content = (TextView) itemView.findViewById(R.id.tv_affiche_content);
            img_affiche_pic = (ImageView) itemView.findViewById(R.id.img_affiche_pic);
            tv_affiche_title = (TextView) itemView.findViewById(R.id.tv_affiche_title);

            rel_read_all = (RelativeLayout) itemView.findViewById(R.id.rel_read_all);

        }
    }

}
