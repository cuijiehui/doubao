package com.xinspace.csevent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.DuobaoRecordEntity;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

public class DuobaoRecordAdapter extends BaseAdapter{
    private Context context;
    private List<Object> list;
    private LayoutInflater inflater;


    public DuobaoRecordAdapter(Context context, List<Object> list) {
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
            convertView = inflater.inflate(R.layout.item_duo_bao_record_all2, null);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_duobao_record_name);
            holder.iv_photo= (ImageView) convertView.findViewById(R.id.iv_duobao_record_image);
            holder.tv_noactivity = (TextView) convertView.findViewById(R.id.tv_noactivity);
            holder.img_has_win = (ImageView) convertView.findViewById(R.id.img_has_win);
            holder.img_luck_icon = (ImageView) convertView.findViewById(R.id.img_luck_icon);
            holder.rel_item_content = (RelativeLayout) convertView.findViewById(R.id.rel_item_content);
            holder.tv_prize_valuee = (TextView) convertView.findViewById(R.id.tv_prize_valuee);
            holder.tv_attend_count= (TextView) convertView.findViewById(R.id.tv_duobao_record_attend_num);
            holder.tv_left = (TextView) convertView.findViewById(R.id.tv_duobao_record_left);
            holder.tv_wins_num = (TextView) convertView.findViewById(R.id.tv_wins_num);
            holder.rel_item = (RelativeLayout) convertView.findViewById(R.id.rel_item);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();

        //数据
        DuobaoRecordEntity enty = (DuobaoRecordEntity) list.get(position);
        String image = enty.getImage();
        String name = enty.getName();//活动名称
        String num = enty.getMatch();//活动参与次数
        String iswin=enty.getIswin();//是否中奖

        //显示数据
        if(iswin.equals("1")){
            holder.rel_item.setBackgroundColor(Color.parseColor("#eeeeee"));
            holder.rel_item_content.setBackgroundColor(Color.parseColor("#fae3ac"));
            holder.img_has_win.setVisibility(View.VISIBLE);
            holder.img_luck_icon.setVisibility(View.VISIBLE);
        }

        ImagerLoaderUtil.displayImage(image, holder.iv_photo);
        holder.tv_title.setText(name);
        holder.tv_attend_count.setText(num);
        holder.tv_noactivity.setText("期号："+ enty.getNoactivity());
        holder.tv_prize_valuee.setText("产品价值：¥" + enty.getPrice());
        int left = Integer.parseInt(enty.getPnum()) - Integer.parseInt(enty.getSnum());
        holder.tv_left.setText(String.valueOf(left));//剩余奖品
        holder.tv_wins_num.setText(enty.getNum());

        return convertView;
    }
    class ViewHolder{
        ImageView iv_photo;
        TextView tv_title;
        TextView tv_left;//剩余奖品
        TextView tv_attend_count;//参与次数
        TextView tv_noactivity;
        ImageView img_has_win;
        ImageView img_luck_icon;
        RelativeLayout rel_item_content;
        TextView tv_prize_valuee;
        TextView tv_wins_num;
        RelativeLayout rel_item;
    }
}
