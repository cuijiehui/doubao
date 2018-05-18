package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.xinspace.csevent.shop.activity.ExpressAct2;
import com.xinspace.csevent.shop.modle.ConvertRecordBean;
import com.xinspace.csevent.shop.weiget.ResultBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.ToastUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Android on 2017/5/17.
 */

public class AllConvertAdapter extends RecyclerView.Adapter<AllConvertAdapter.MyViewHolder>{

    private Context mContext;
    private LayoutInflater mIflater;
    private List<ConvertRecordBean> list;
    private ResultBean resultBean;
    private SDPreference preference;
    private String openId;

    /**
     * 更新数据
     * @param list
     */
    public AllConvertAdapter(Context mContext, List<ConvertRecordBean> list , ResultBean resultBean){
        this.mContext = mContext;
        this.list = list;
        this.resultBean = resultBean;
        mIflater = LayoutInflater.from(mContext);
        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
    }

    /**
     * 清除数据
     */
    public void clearList(){
        this.list.clear();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mIflater.inflate(R.layout.item_convert_record , parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //LogUtil.i("------------------------------");
        final ConvertRecordBean bean = list.get(position);

        holder.tv_goods_name.setText(bean.getTitle());
        holder.tv_order_name.setText("订单号:" + bean.getOrderno());
        ImagerLoaderUtil.displayImageWithLoadingIcon(bean.getThumb() , holder.iv_goods_image , R.drawable.icon_detail_load);
        holder.tv_goods_price.setText("消耗" + bean.getCredit() + "积分+"+ bean.getMoney() + "元");

        holder.tv_order_express.setVisibility(View.GONE);
        holder.tv_confirm_receipt.setVisibility(View.GONE);
        holder.lin_bottom.setVisibility(View.GONE);

        //1：待支付 2：待发货 3：待收货 4：已完成
        String status = bean.getStatus();
        if (status.equals("1")){
            holder.tv_order_state.setText("未付款");
        }else  if (status.equals("2")){
            holder.tv_order_state.setText("待发货");
        }else  if (status.equals("3")){
            holder.tv_order_state.setText("待收货");
            holder.lin_bottom.setVisibility(View.VISIBLE);
            holder.tv_order_express.setVisibility(View.VISIBLE);
            holder.tv_confirm_receipt.setVisibility(View.VISIBLE);
        }else  if (status.equals("4")){
            holder.tv_order_state.setText("已完成");
            holder.lin_bottom.setVisibility(View.VISIBLE);
            holder.tv_order_express.setVisibility(View.VISIBLE);
        }

        holder.lin_content.setOnClickListener(new View.OnClickListener() { //点击跳转详情页
            @Override
            public void onClick(View v) {

            }
        });

        holder.tv_order_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , ExpressAct2.class);
                intent.putExtra("flag" , "3");
                intent.putExtra("orderId" , bean.getId());
                mContext.startActivity(intent);
            }
        });

        holder.tv_confirm_receipt.setOnClickListener(new View.OnClickListener() {   //积分商城确认收货
            @Override
            public void onClick(View v) {
                GetDataBiz.JiFenShouHuo(bean.getId() , openId , new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        if (TextUtils.isEmpty(result)){
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getInt("code") == 200){
                            ToastUtil.makeToast("确认收货成功");
                            holder.tv_order_state.setText("已完成");
                            holder.tv_confirm_receipt.setVisibility(View.GONE);
                            resultBean.resultBean(position);
                        }else{
                            ToastUtil.makeToast("确认收货失败");
                        }
                    }

                    @Override
                    public void onHttpRequestError(String error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {

        //LogUtil.i("适配的长度-------------" + list.size());

        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_content;
        TextView tv_order_name;
        TextView tv_order_state;
        TextView tv_goods_name;
        TextView tv_goods_price;
        ImageView iv_goods_image;
        TextView tv_order_express;
        TextView tv_confirm_receipt;
        LinearLayout lin_bottom;


        public MyViewHolder(View itemView) {
            super(itemView);

            lin_content = (LinearLayout) itemView.findViewById(R.id.lin_content);
            tv_order_state = (TextView) itemView.findViewById(R.id.tv_order_state);
            tv_order_name = (TextView) itemView.findViewById(R.id.tv_order_name);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_goods_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            iv_goods_image = (ImageView) itemView.findViewById(R.id.iv_goods_image);

            tv_order_express = (TextView) itemView.findViewById(R.id.tv_order_express);
            tv_confirm_receipt = (TextView) itemView.findViewById(R.id.tv_confirm_receipt);

            lin_bottom  = (LinearLayout) itemView.findViewById(R.id.lin_bottom);
        }
    }

}
