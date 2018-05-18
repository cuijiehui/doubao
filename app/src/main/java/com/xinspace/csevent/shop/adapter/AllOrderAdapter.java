package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinspace.csevent.shop.weiget.OrderContent;

import java.util.List;

/**
 * Created by Android on 2017/5/17.
 */

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.MyViewHolder>{

    private Context mContext;
    private List<OrderContent> list;
    private LayoutInflater mIflater;

    /**
     * 更新数据
     * @param orderContents
     */
    public AllOrderAdapter(Context mContext, List<OrderContent> orderContents){
        this.mContext = mContext;
        this.list = orderContents;
    }

    /**
     * 清除数据
     */
    public void clearList(){
        this.list.clear();
    }

    int times =0;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(list.get(viewType).getView(mContext, parent, mIflater));
        times++;
        Log.d("MyAdapter", "times:" + times);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    /**
     * 每一个位置的item都作为单独一项来设置
     * viewType 设置为position
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

//        OrderContent content = list.get(position);
//        if(content instanceof ItemOrderTop){
//            return 0;
//        }
//        if(content instanceof ItemOrderBottom){
//            return 1;
//        }
//        return 2;


        return position;
    }


    /**
     * 更新数据
     * @param orderContents
     */
    public void updateList(List<OrderContent> orderContents){
        this.list = orderContents;
        this.notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
