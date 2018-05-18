package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.RecommendBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * 热门试用列表适配器
 *
 * Created by Android on 2017/3/19.
 */

public class MoreAdapter extends BaseAdapter{

    private Context context;
    private List<RecommendBean> list;
    private LayoutInflater inflater;

    public MoreAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<RecommendBean> list) {
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
            convertView = inflater.inflate(R.layout.item_more_goods , null);

//            holder.lin_content = (LinearLayout) convertView.findViewById(R.id.lin_content);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lin_content.getLayoutParams();
//            params.width = (int) (screenWidth * 0.5f);

            holder.iv_goods_image= (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_good_price = (TextView) convertView.findViewById(R.id.tv_good_price);
            holder.tv_good_sale = (TextView) convertView.findViewById(R.id.tv_good_sale);
            convertView.setTag(holder);

        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        RecommendBean recommendBean = list.get(position);

        ImagerLoaderUtil.displayImageWithLoadingIcon(recommendBean.getThumb() , holder.iv_goods_image , R.drawable.goods_loading);
        holder.tv_goods_name.setText(recommendBean.getTitle());

        holder.tv_good_price.setText("¥" + recommendBean.getTotal());
        holder.tv_good_sale.setText("销量" + recommendBean.getSales());

//        holder.tv_apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return convertView;
    }

    static class ViewHolder{
        ImageView iv_goods_image;
        TextView tv_goods_name;

        TextView tv_good_price;
        TextView tv_good_sale;
    }
}
