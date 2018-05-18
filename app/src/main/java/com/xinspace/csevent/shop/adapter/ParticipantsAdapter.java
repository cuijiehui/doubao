package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.GroupParticipants;

import java.util.List;

/**
 * Created by Android on 2017/8/22.
 */

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.VHolder>{

    private List<GroupParticipants.GroupBean> mDatas;
    private Context context;
    private int totalCount;

    public ParticipantsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GroupParticipants.GroupBean> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void setTotalParticipants(int count){
        this.totalCount = count;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_participants, parent, false);
        VHolder holder = new VHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        Glide.with(context)
                .load(mDatas.get(position).getAvatar())
                .error(R.drawable.loading_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return mDatas!=null ? mDatas.size() : 0;
    }

    class VHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        public VHolder(View itemView) {
            super(itemView);
            imgPhoto = (ImageView) itemView.findViewById(R.id.participants_logo);
        }
    }

}
