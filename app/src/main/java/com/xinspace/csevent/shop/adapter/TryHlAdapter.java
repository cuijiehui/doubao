package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.shop.modle.IsselectBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * 热门试用列表适配器
 *
 * Created by Android on 2017/3/19.
 */

public class TryHlAdapter extends BaseAdapter{

    private Context context;
    private List<IsselectBean> list;
    private LayoutInflater inflater;
    private int screenWidth;

    public TryHlAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        screenWidth = ScreenUtils.getScreenWidth(context);
    }

    public void setList(List<IsselectBean> list) {
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
            convertView = inflater.inflate(R.layout.item_hl_try , null);

            holder.lin_content = (LinearLayout) convertView.findViewById(R.id.lin_content);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lin_content.getLayoutParams();
            params.width = (int) (screenWidth * 0.5f);

            holder.iv_goods_image= (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_num = (TextView) convertView.findViewById(R.id.tv_goods_num);
            holder.tv_apply_num = (TextView) convertView.findViewById(R.id.tv_apply_num);
            holder.iv_has_apply = (ImageView) convertView.findViewById(R.id.iv_icon);

            //holder.tv_apply = (TextView) convertView.findViewById(R.id.tv_apply);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        IsselectBean goodsEntity =  list.get(position);
        ImagerLoaderUtil.displayImageWithLoadingIcon(goodsEntity.getThumb() , holder.iv_goods_image , R.drawable.goods_loading);
        holder.tv_goods_name.setText(goodsEntity.getTitle());

        holder.tv_goods_num.setText(goodsEntity.getTotal());
        holder.tv_apply_num.setText(goodsEntity.getApply());

        final String success = goodsEntity.getSuccess();
        if (success != null && !success.equals("")){
            if (success.equals("0")){
                //holder.tv_apply.setText("申请中");
            }else if (success.equals("1")){
                //holder.tv_apply.setText("申请成功");
                holder.iv_has_apply.setVisibility(View.VISIBLE);
            }else if (success.equals("-1")){
                //holder.tv_apply.setText("申请失败");
            }
            //holder.tv_apply.setBackgroundResource(R.drawable.trial_tv_shape);
        }else{
//            holder.tv_apply.setText("免费试用");
//            holder.tv_apply.setBackgroundResource(R.drawable.selector_bt_state_change);
        }


//        holder.tv_apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        return convertView;
    }

    static class ViewHolder{
        LinearLayout lin_content;
        ImageView iv_goods_image;
        TextView tv_goods_name;
        TextView tv_goods_num;
        TextView tv_apply_num;
        //TextView tv_apply;
        ImageView iv_has_apply;
    }
}
