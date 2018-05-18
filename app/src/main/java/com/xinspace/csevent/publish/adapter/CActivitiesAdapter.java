package com.xinspace.csevent.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.publish.model.PublishActivitiesBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;


/**
 * Created by Android on 2016/10/21.
 */
public class CActivitiesAdapter extends BaseAdapter{

    private Context context;
    private List<PublishActivitiesBean> dataList;

    public CActivitiesAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<PublishActivitiesBean> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_crowd_activities, null);
            holder.tv_crowd_noactivity = (TextView) convertView.findViewById(R.id.tv_crowd_noactivity);
            holder.tv_crowd_time = (TextView) convertView.findViewById(R.id.tv_crowd_time);
            holder.tv_crowd_user_name = (TextView) convertView.findViewById(R.id.tv_crowd_user_name);
            holder.tv_crowd_user_id = (TextView) convertView.findViewById(R.id.tv_crowd_user_id);
            holder.tv_duobao_record_num = (TextView) convertView.findViewById(R.id.tv_duobao_record_num);
            holder.iv_win_user = (ImageView) convertView.findViewById(R.id.iv_win_user);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_crowd_noactivity.setText("期号： " + dataList.get(position).getNoactivity());
        holder.tv_crowd_time.setText( dataList.get(position).getWintime());

        holder.tv_crowd_user_name.setText("获得者： " + dataList.get(position).getUsername());
        holder.tv_crowd_user_id.setText("ID：" + dataList.get(position).getUid());
        holder.tv_duobao_record_num.setText(dataList.get(position).getMatch());

        ImagerLoaderUtil.displayRoundedImage(dataList.get(position).getUser_img() , holder.iv_win_user);

        return convertView;
    }


    class ViewHolder {

        TextView tv_crowd_noactivity;
        TextView tv_crowd_time;
        TextView tv_crowd_user_name;
        TextView tv_crowd_user_id;
        TextView tv_duobao_record_num;

        ImageView iv_win_user;

    }

}
