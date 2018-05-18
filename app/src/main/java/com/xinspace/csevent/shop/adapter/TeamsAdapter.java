package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.view.CircleImageView;
import com.xinspace.csevent.shop.activity.BuyGroupAct;
import com.xinspace.csevent.shop.activity.SpellGroupActivity;
import com.xinspace.csevent.shop.modle.GroupDetailBean;
import com.xinspace.csevent.shop.modle.TreamBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.ToastUtil;

import java.util.List;

/**
 * Created by Android on 2017/6/7.
 */

public class TeamsAdapter extends BaseAdapter {

    private Context context;
    private List<TreamBean> treamList;
    private LayoutInflater inflater;

    private GroupDetailBean goodsDetail;
    private boolean isCanBuy;

    public void setCanBuy(boolean canBuy) {
        isCanBuy = canBuy;
    }

    private final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };

    public void setTeamList(List<TreamBean> treamList) {
        this.treamList = treamList;
    }


    public TeamsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setGoodsDetail(GroupDetailBean goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    @Override
    public int getCount() {
        return treamList.size();
    }

    @Override
    public Object getItem(int position) {
        return treamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_team, null);
            holder.iv_user_head = (CircleImageView) convertView.findViewById(R.id.iv_user_head);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_user_content = (TextView) convertView.findViewById(R.id.tv_user_content);
            holder.tv_goto_add_group = (TextView) convertView.findViewById(R.id.tv_goto_add_group);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TreamBean bean = treamList.get(position);
        ImagerLoaderUtil.displayImage(bean.getAvatar() ,holder.iv_user_head);
        holder.tv_user_name.setText(bean.getNickname());

        holder.tv_user_content.setText("还差" + bean.getNum() + "人成团");

        holder.tv_goto_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 去参团
                if (isCanBuy){
                    Intent intent1 = new Intent(context , SpellGroupActivity.class);
                    intent1.putExtra("bean" , goodsDetail);
                    intent1.putExtra("type" , "groups");
                    intent1.putExtra("teamid" , bean.getId());
                    context.startActivity(intent1);
                }else{
                    ToastUtil.makeToast("您已经参与了该团，请等待拼团结束后再进行购买！");
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        CircleImageView iv_user_head;
        TextView tv_user_name;
        TextView tv_user_content;
        TextView tv_goto_add_group;
    }


    public void onDestroy() {

        treamList = null;
    }

}
