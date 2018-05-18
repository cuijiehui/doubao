package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.GoodsModle;
import com.xinspace.csevent.shop.activity.GoodsDetailAct;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * 搜索全商场
 *
 * Created by Android on 2017/5/17.
 */

public class SearchShopAdapter extends RecyclerView.Adapter<SearchShopAdapter.MyViewHolder>{

    private Context mContext;
    private LayoutInflater mIflater;
    private List<GoodsModle> list;

    /**
     * 更新数据
     * @param list
     */
    public SearchShopAdapter(Context mContext, List<GoodsModle> list ){
        this.mContext = mContext;
        this.list = list;
        mIflater = LayoutInflater.from(mContext);
    }

    /**
     * 清除数据
     */
    public void clearList(){
        this.list.clear();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mIflater.inflate(R.layout.item_search_goods , parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //LogUtil.i("------------------------------");
        final GoodsModle bean = list.get(position);
        holder.tv_goods_name.setText(bean.getTitle());
        ImagerLoaderUtil.displayImageWithLoadingIcon(bean.getThumb() , holder.iv_goods_image , R.drawable.icon_detail_load);
        holder.tv_goods_price.setText("¥" + bean.getMarketprice());
        holder.tv_goods_price2.setText("¥" +bean.getProductprice());
        holder.tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_goods_buy_num.setText("销量" + bean.getSales());

        holder.lin_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext , GoodsDetailAct.class);
                intent.putExtra("goodId" , bean.getId());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        //LogUtil.i("适配的长度-------------" + list.size());

        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_goods_image;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_goods_price2;
        TextView tv_goods_buy_num;
        LinearLayout lin_1;


        public MyViewHolder(View itemView) {
            super(itemView);

            iv_goods_image = (ImageView) itemView.findViewById(R.id.iv_goods_image);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_goods_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tv_goods_price2 = (TextView) itemView.findViewById(R.id.tv_goods_price2);
            tv_goods_buy_num = (TextView) itemView.findViewById(R.id.tv_goods_buy_num);
            lin_1 = (LinearLayout) itemView.findViewById(R.id.lin_1);

        }
    }

}
