package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.ExpressAct;
import com.xinspace.csevent.shop.activity.OrderDetailAct;
import com.xinspace.csevent.shop.activity.PayOrderActivity;
import com.xinspace.csevent.shop.modle.AllOrderBean;
import com.xinspace.csevent.shop.modle.OrderMiddleBean;
import com.xinspace.csevent.shop.weiget.ResultBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Android on 2017/5/17.
 */

public class AllOrderAdapter2 extends RecyclerView.Adapter<AllOrderAdapter2.MyViewHolder>{

    private Context mContext;
    private LayoutInflater mIflater;
    private List<AllOrderBean> list;
    private ResultBean resultBean;

    /**
     * 更新数据
     * @param list
     */
    public AllOrderAdapter2(Context mContext, List<AllOrderBean> list , ResultBean resultBean){
        this.mContext = mContext;
        this.list = list;
        this.resultBean = resultBean;
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
        MyViewHolder holder = new MyViewHolder(mIflater.inflate(R.layout.item_order_list , parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //LogUtil.i("------------------------------");
        final AllOrderBean allOrderBean = list.get(position);
        List<OrderMiddleBean> middleBeanList = allOrderBean.getMiddleBeanList();
        //LogUtil.i("middleBeanList" + middleBeanList.size());

        holder.tv_store_name.setText("订单号:" + allOrderBean.getOrdersn());

        if (holder.lin_goods_content.getChildCount() != 0){
            holder.lin_goods_content.removeAllViews();
        }

        holder.tv_cancle_order.setVisibility(View.GONE);
        holder.tv_order_express.setVisibility(View.GONE);
        //holder.tv_sales_return.setVisibility(View.GONE);
        holder.tv_remind_send.setVisibility(View.GONE);
        holder.tv_confirm_receipt.setVisibility(View.GONE);
        holder.tv_goto_pay.setVisibility(View.GONE);
        holder.tv_del_order.setVisibility(View.GONE);


        for (int i = 0 ; i < middleBeanList.size() ; i++){
            View view = mIflater.inflate(R.layout.item_order_in , null);
            OrderMiddleBean orderMiddleBean = middleBeanList.get(i);
            TextView nameTv = (TextView) view.findViewById(R.id.tv_goods_name);
            nameTv.setText(orderMiddleBean.getTitle());
            TextView snTv = (TextView) view.findViewById(R.id.tv_goods_spec);
            TextView tv_goods_num = (TextView) view.findViewById(R.id.tv_goods_num);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price);

            tv_goods_num.setText("×" + orderMiddleBean.getTotal());
            tv_price.setText("¥" + orderMiddleBean.getPrice());

            //LogUtil.i("规格是什么" + orderMiddleBean.getOptiontitle());
            if (!orderMiddleBean.getOptiontitle().equals("")){
                snTv.setText("规格：" + orderMiddleBean.getOptiontitle());
            }else{
                snTv.setVisibility(View.INVISIBLE);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_goods_image);
            ImagerLoaderUtil.displayImageWithLoadingIcon(orderMiddleBean.getThumb() , imageView , R.drawable.icon_detail_load);

            //ImagerLoaderUtil.displayImage(orderMiddleBean.getThumb() , imageView);
            holder.lin_goods_content.addView(view);
        }


        holder.tv_order_goods_num.setText(allOrderBean.getGoods_num());
        holder.tv_order_goods_price.setText("¥" + allOrderBean.getPrice());
        holder.tv_order_freight.setText("（含运费¥" + allOrderBean.getDispatchprice() + ")");

        String status = allOrderBean.getStatus();
        String paytype = allOrderBean.getPaytype();
        int sendtype = Integer.valueOf(allOrderBean.getSendtype());
        int refundstate = Integer.valueOf(allOrderBean.getRefundstate());

        if (refundstate > 0) {
            if (status.equals("1")) {
                //申请退款
                holder.tv_order_state.setText("申请退款中");
            } else {
                // 申请售后
                //holder.tv_sales_return.setVisibility(View.VISIBLE);
                holder.tv_order_state.setText("申请售后中");
            }
        }else{

            if (status.equals("0")) {
                if (!paytype.equals("") && paytype.equals("3")) {
                    //货到付款，待发货
                    holder.tv_confirm_receipt.setVisibility(View.VISIBLE);
                    holder.tv_order_state.setText("货到付款,待发货");
                } else {
                    //等待付款
                    holder.tv_goto_pay.setVisibility(View.VISIBLE);
                    holder.tv_cancle_order.setVisibility(View.VISIBLE);
                    holder.tv_order_state.setText("待付款");
                }
            }

            if (status.equals("1")) {
                if (sendtype > 0) {
                    //部分商品已发货
                    holder.tv_confirm_receipt.setVisibility(View.VISIBLE);
                    holder.tv_order_state.setText("部分商品已发货");
                } else {
                    //买家已付款
                    holder.tv_remind_send.setVisibility(View.VISIBLE);
                    holder.tv_order_state.setText("待发货");
                }
            }

            if (status.equals("2")) {
                //卖家已发货
                holder.tv_order_express.setVisibility(View.VISIBLE);
                holder.tv_confirm_receipt.setVisibility(View.VISIBLE);
                holder.tv_order_state.setText("待收货");
            }

            if (status.equals("3")) {
                // 交易已完成
                holder.tv_order_state.setText("已完成");
                holder.tv_order_express.setVisibility(View.VISIBLE);
            }

            if (status.equals("-1")) {
                // 交易取消
                holder.tv_cancle_order.setVisibility(View.GONE);
                holder.tv_order_state.setText("订单已取消");
                holder.tv_del_order.setVisibility(View.VISIBLE);
            }

        }

        final String id = allOrderBean.getId();
        final String openId = SDPreference.getInstance().getContent("openid");

        holder.tv_cancle_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("点击取消订单了");
                GetDataBiz.cancleOrderDetailData(id, openId, new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        LogUtil.i("点击取消订单" + result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("200")) {
                            ToastUtil.makeToast("取消成功");
                            //resultBean.resultBean(allOrderBean);
                            holder.tv_order_state.setText("订单已取消");
                            holder.tv_cancle_order.setVisibility(View.GONE);
                            holder.tv_goto_pay.setVisibility(View.GONE);
                            holder.tv_del_order.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtil.makeToast("取消失败");
                        }
                    }

                    @Override
                    public void onHttpRequestError(String error) {
                        ToastUtil.makeToast("取消失败");
                    }
                });
            }
        });


        holder.lin_goods_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , OrderDetailAct.class);
                intent.putExtra("orderId" , allOrderBean.getId());
                mContext.startActivity(intent);
            }
        });


        holder.tv_confirm_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataBiz.confirmReceiptData(id, openId, new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        LogUtil.i("点击取消订单" + result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("200")) {

                            holder.tv_confirm_receipt.setVisibility(View.GONE);
                            holder.tv_order_state.setText("交易成功");

                        } else {
                            ToastUtil.makeToast("确认收货失败");
                        }
                    }

                    @Override
                    public void onHttpRequestError(String error) {
                        ToastUtil.makeToast("确认收货失败");
                    }
                });
            }
        });


        holder.tv_del_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataBiz.delOrderDetailData(id, openId, new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("200")){
                            resultBean.resultBean(position);
                        }
                    }

                    @Override
                    public void onHttpRequestError(String error) {

                    }
                });
            }
        });

        holder.tv_remind_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.makeToast("已提醒发货");
            }
        });


        if (holder.tv_goto_pay.getVisibility() == View.VISIBLE) {
            holder.tv_goto_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //付款
                    LogUtil.i("付款付款");

                    Intent intent = new Intent(mContext , PayOrderActivity.class);
                    intent.putExtra("orderId" , allOrderBean.getId());
                    mContext.startActivity(intent);

                }
            });
        }


        holder.tv_order_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , ExpressAct.class);
                intent.putExtra("orderId" , allOrderBean.getId());
                intent.putExtra("flag" , "1");
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 更新数据
     * @param orderContents
     */
    public void updateList(List<AllOrderBean> orderContents){
        this.list = orderContents;
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {

        //LogUtil.i("适配的长度-------------" + list.size());

        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_store_name;   //订单号
        TextView tv_order_state;
        LinearLayout lin_goods_content;
        TextView tv_order_goods_num;
        TextView tv_order_goods_price;
        TextView tv_order_freight;
        TextView tv_cancle_order; //取消订单
        TextView tv_order_express; //查看物流
        //TextView tv_sales_return;   //售后
        TextView tv_remind_send;     //提醒发货
        TextView tv_confirm_receipt; //确认收货
        TextView tv_goto_pay;  //付款
        TextView tv_del_order; //删除订单

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_store_name = (TextView) itemView.findViewById(R.id.tv_store_name);

            tv_order_state = (TextView) itemView.findViewById(R.id.tv_order_state);
            tv_order_goods_num = (TextView) itemView.findViewById(R.id.tv_order_goods_num);
            tv_order_goods_price = (TextView) itemView.findViewById(R.id.tv_order_goods_price);
            tv_order_freight = (TextView) itemView.findViewById(R.id.tv_order_freight);
            tv_cancle_order = (TextView) itemView.findViewById(R.id.tv_delete_order);
            tv_order_express = (TextView) itemView.findViewById(R.id.tv_order_express);
           // tv_sales_return = (TextView) itemView.findViewById(R.id.tv_sales_return);
            tv_remind_send = (TextView) itemView.findViewById(R.id.tv_remind_send);
            tv_confirm_receipt = (TextView) itemView.findViewById(R.id.tv_confirm_receipt);
            tv_goto_pay = (TextView) itemView.findViewById(R.id.tv_goto_pay);
            lin_goods_content = (LinearLayout) itemView.findViewById(R.id.lin_goods_content);

            tv_del_order = (TextView) itemView.findViewById(R.id.tv_del_order);
        }
    }

}
