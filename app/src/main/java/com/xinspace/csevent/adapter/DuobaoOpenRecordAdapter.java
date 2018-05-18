package com.xinspace.csevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.DuobaoRecordEntity;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

public class DuobaoOpenRecordAdapter extends BaseAdapter{
    private Context context;
    private List<Object> list;
    private LayoutInflater inflater;


    public DuobaoOpenRecordAdapter(Context context, List<Object> list) {
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
            convertView = inflater.inflate(R.layout.item_duo_bao_record_opened, null);
            holder.iv_photo= (ImageView) convertView.findViewById(R.id.iv_awards_record_image);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_duobao_record_name);
            holder.tv_attend_num= (TextView) convertView.findViewById(R.id.tv_duobao_record_number);
            holder.tv_record_count= (TextView) convertView.findViewById(R.id.tv_duobao_record_time);
            holder.tv_record_count_left= (TextView) convertView.findViewById(R.id.tv_duobao_record_time2);
            holder.tv_attend_count= (TextView) convertView.findViewById(R.id.tv_awards_record_attend_num);

            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        //数据
        DuobaoRecordEntity enty = (DuobaoRecordEntity) list.get(position);
        String image=enty.getImage();
        String name=enty.getName();//活动名称
        String aid=enty.getAid();
        String integral=enty.getIntegral();//消耗积分
        String num=enty.getNum();//活动参与次数
        String pum=enty.getPnum();//奖品数量
        String snum=enty.getSnum();//发放数量
        String startdate=enty.getStartdate();//开始时间

        //显示数据
        ImagerLoaderUtil.displayImage(image, holder.iv_photo);
        holder.tv_title.setText(name);
        holder.tv_record_count.setText(pum);
        //TODO 此处有异常,当这两个数字为null时,会抛出异常
        holder.tv_record_count_left.setText(String.valueOf(Integer.valueOf(pum)-Integer.valueOf(snum)));//剩余奖品
        holder.tv_attend_num.setText(aid);
        holder.tv_attend_count.setText(num);

        return convertView;
    }

    class ViewHolder{
        ImageView iv_photo;
        TextView tv_title;
        TextView tv_attend_num;//参与号
        TextView tv_record_count;//奖品总数
        TextView tv_record_count_left;//剩余
        TextView tv_attend_count;//参与人数
    }
}
