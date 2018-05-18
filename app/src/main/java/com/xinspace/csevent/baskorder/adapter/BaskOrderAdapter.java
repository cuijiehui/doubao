package com.xinspace.csevent.baskorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.view.CircleImageView;
import com.xinspace.csevent.baskorder.activity.BaskOrderDetailAct;
import com.xinspace.csevent.baskorder.model.BaskOrder;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;


/**
 * 揭晓活动adapter
 */
public class BaskOrderAdapter extends BaseAdapter {

    private Context context;

    private List<BaskOrder> dataList;

    public List<BaskOrder> getDataList() {
        return dataList;
    }

    public void setDataList(List<BaskOrder> dataList) {
        this.dataList = dataList;
    }

    public BaskOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bask_order, null);
            holder = new ViewHolder();
            holder.rel_item = (RelativeLayout) convertView.findViewById(R.id.rel_item);
            holder.iv_user_image = (CircleImageView) convertView.findViewById(R.id.iv_user_image);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_bask_time = (TextView) convertView.findViewById(R.id.tv_bask_time);
            holder.tv_bask_title = (TextView) convertView.findViewById(R.id.tv_bask_title);
            holder.tv_bask_comment = (TextView) convertView.findViewById(R.id.tv_bask_comment);
            holder.iv_small_1 = (ImageView) convertView.findViewById(R.id.iv_small_1);
            holder.iv_small_2 = (ImageView) convertView.findViewById(R.id.iv_small_2);
            holder.iv_small_3 = (ImageView) convertView.findViewById(R.id.iv_small_3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ImagerLoaderUtil.displayImageWithLoadingIcon(dataList.get(position).getImg(), holder.iv_user_image, R.drawable.loading_icon);
        holder.tv_user_name.setText(dataList.get(position).getUsername());
        holder.tv_bask_time.setText(dataList.get(position).getDatetime());
        holder.tv_bask_title.setText(dataList.get(position).getTitle());
        holder.tv_bask_comment.setText(dataList.get(position).getComment());

        List<String> smallList = dataList.get(position).getSmallList();
        int size = smallList.size();
        if (size == 1) {
            ImagerLoaderUtil.displayRoundedImage(smallList.get(0), holder.iv_small_1);
        } else if (size == 2) {
            ImagerLoaderUtil.displayRoundedImage(smallList.get(0), holder.iv_small_1);
            ImagerLoaderUtil.displayRoundedImage(smallList.get(1), holder.iv_small_2);
        } else {
            ImagerLoaderUtil.displayRoundedImage(smallList.get(0), holder.iv_small_1);
            ImagerLoaderUtil.displayRoundedImage(smallList.get(1), holder.iv_small_2);
            ImagerLoaderUtil.displayRoundedImage(smallList.get(2), holder.iv_small_3);
        }

        holder.rel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BaskOrderDetailAct.class);
                intent.putExtra("data", dataList.get(position));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {

        RelativeLayout rel_item;

        CircleImageView iv_user_image;
        TextView tv_user_name;
        TextView tv_bask_time;

        TextView tv_bask_title;
        TextView tv_bask_comment;

        ImageView iv_small_1;
        ImageView iv_small_2;
        ImageView iv_small_3;

    }


}
