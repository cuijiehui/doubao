package com.xinspace.csevent.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.view.CircleImageView;
import com.xinspace.csevent.publish.model.PartRecordBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;



/**
 * Created by Android on 2016/10/21.
 */
public class CPartRecordAdapter extends BaseAdapter{

    private Context context;
    private List<PartRecordBean> dataList;

    public CPartRecordAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<PartRecordBean> dataList) {
        this.dataList = dataList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_crowd_part_record, null);
            holder.tv_part_time = (TextView) convertView.findViewById(R.id.tv_part_time);
            holder.tv_crowd_user_name = (TextView) convertView.findViewById(R.id.tv_crowd_user_name);
            holder.tv_crowd_user_id = (TextView) convertView.findViewById(R.id.tv_crowd_user_id);
            holder.tv_duobao_record_num = (TextView) convertView.findViewById(R.id.tv_duobao_record_num);
            holder.iv_part_user = (CircleImageView) convertView.findViewById(R.id.iv_user_img);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_crowd_user_name.setText(dataList.get(position).getUsername());
        holder.tv_crowd_user_id.setText("(ID：" + dataList.get(position).getUid()+ ")");
        holder.tv_duobao_record_num.setText(dataList.get(position).getMatch());
        holder.tv_part_time.setText(dataList.get(position).getParticipate());
        ImagerLoaderUtil.displayImage(dataList.get(position).getUser_img() , holder.iv_part_user);

        return convertView;
    }


    class ViewHolder {

        TextView tv_part_time;
        TextView tv_crowd_user_name;
        TextView tv_crowd_user_id;
        TextView tv_duobao_record_num;

        CircleImageView iv_part_user;

    }

}
