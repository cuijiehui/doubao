package com.xinspace.csevent.shop.adapter;

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
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.shop.activity.ExpressAct;
import com.xinspace.csevent.shop.activity.MyTrailAct;
import com.xinspace.csevent.shop.modle.TrialBean;
import com.xinspace.csevent.sweepstake.activity.ChooseAddressAct2;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * 我的试用适配器
 *
 * Created by Android on 2017/3/19.
 */

public class MyTrailAdapter extends BaseAdapter{

    private Context context;
    private List<TrialBean> list;
    private LayoutInflater inflater;
    private int screenWidth;

    public MyTrailAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        screenWidth = ScreenUtils.getScreenWidth(context);
    }

    public void setList(List<TrialBean> list) {
        this.list = list;
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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_my_trial , null);

//            holder.lin_content = (LinearLayout) convertView.findViewById(R.id.lin_content);
//
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lin_content.getLayoutParams();
//            params.width = (int) (screenWidth * 1f);

            holder.iv_goods_image= (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_apply_num = (TextView) convertView.findViewById(R.id.tv_apply_num);
            holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_apply_state = (TextView) convertView.findViewById(R.id.tv_apply_state);
            holder.tv_apply_time = (TextView) convertView.findViewById(R.id.tv_apply_time);
            holder.rel_bottom = (RelativeLayout) convertView.findViewById(R.id.rel_bottom);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        final TrialBean bean =  list.get(position);
        ImagerLoaderUtil.displayImageWithLoadingIcon(bean.getThumb() , holder.iv_goods_image , R.drawable.goods_loading);
        holder.tv_goods_name.setText(bean.getTitle());
        holder.tv_apply_num.setText(bean.getApply());
        holder.tv_goods_price.setText("¥"+bean.getPrice());
        holder.tv_apply_time.setText("申请时间" + bean.getStarttime());

        final String success = bean.getSuccess();
        final String status = bean.getStatus();

        if (success != null && !success.equals("")){
            if (success.equals("0")){
                holder.tv_apply_state.setText("申请中,若通过短信提醒您");
            }else if (success.equals("1")){
                //在 success = 1的情况下，1 待发货，2 待收货，3 待确认地址，4 已完成
                if (status.equals("1")){
                    holder.tv_apply_state.setText("恭喜您,申请成功!待发货");
                }else if (status.equals("2")){
                    holder.tv_apply_state.setText("恭喜您,申请成功!待收货,查看物流");
                }else if (status.equals("3")){
                    holder.tv_apply_state.setText("恭喜您,申请成功!请填写收货地址");
                }else if (status.equals("4")){
                    holder.tv_apply_state.setText("恭喜您,申请成功!已完成");
                }
            }else if (success.equals("-1")){
                holder.tv_apply_state.setText("请遗憾，您未获得试用资格,尝试申请其他试用");
            }
        }else{
            //holder.tv_apply_state.setText("免费试用");
        }

        holder.rel_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (success != null && !success.equals("")){
                    if (success.equals("1")){
                        if (status.equals("3")){
                            //填写地址
                            Intent intent2 = new Intent( context , ChooseAddressAct2.class);
                            intent2.putExtra("id" , bean.getId());
                            context.startActivity(intent2);
                        }else if (status.equals("2")){

                            Intent intent = new Intent(context , ExpressAct.class);
                            intent.putExtra("orderId" , bean.getId());
                            intent.putExtra("flag" , "2");
                            context.startActivity(intent);

                        } else{
                            Intent intent2 = new Intent( context , MyTrailAct.class);
                            intent2.putExtra("id" , bean.getId());
                            intent2.putExtra("flag" , "1");
                            context.startActivity(intent2);
                        }
                    }else{

                    }
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_goods_image;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_apply_num;
        TextView tv_apply_state;
        TextView tv_apply_time;
        RelativeLayout rel_bottom;
    }
}
