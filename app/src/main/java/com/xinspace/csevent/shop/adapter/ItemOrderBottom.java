package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.AllOrderBean;
import com.xinspace.csevent.shop.weiget.OrderContent;
import com.xinspace.csevent.shop.weiget.ResultBean;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 订单布局-in
 * 作者：fly on 2016/8/24 0024 23:45
 */
public class ItemOrderBottom implements OrderContent {

    private Context context;
    private AllOrderBean allOrderBean;
    private String pageFlag;
    private ResultBean resultBean;

    public ItemOrderBottom(Context context, AllOrderBean allOrderBean, String pageFlag, ResultBean resultBean) {
        this.context = context;
        this.allOrderBean = allOrderBean;
        this.pageFlag = pageFlag;
        this.resultBean = resultBean;
    }

    @Override
    public int getLayout() {
        return R.layout.item_order_bottom;
    }

    @Override
    public boolean isClickAble() {
        return true;
    }

    @Override
    public View getView(Context mContext, View convertView, LayoutInflater inflater) {

        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(getLayout(), null);

        TextView tv_order_goods_num = (TextView) convertView.findViewById(R.id.tv_order_goods_num);
        TextView tv_order_goods_price = (TextView) convertView.findViewById(R.id.tv_order_goods_price);
        TextView tv_order_freight = (TextView) convertView.findViewById(R.id.tv_order_freight);     //运费
        final TextView tv_order_state = (TextView) convertView.findViewById(R.id.tv_order_state);

        tv_order_goods_num.setText(allOrderBean.getGoods_num());
        tv_order_goods_price.setText("¥" + allOrderBean.getPrice());
        tv_order_freight.setText("（含运费¥" + allOrderBean.getDispatchprice() + ")");

        final TextView tv_delete_order = (TextView) convertView.findViewById(R.id.tv_delete_order); //取消订单
        TextView tv_order_express = (TextView) convertView.findViewById(R.id.tv_order_express); //查看物流
        TextView tv_sales_return = (TextView) convertView.findViewById(R.id.tv_sales_return);   //售后
        TextView tv_remind_send = (TextView) convertView.findViewById(R.id.tv_remind_send);     //提醒发货
        TextView tv_confirm_receipt = (TextView) convertView.findViewById(R.id.tv_confirm_receipt); //确认收货
        final TextView tv_goto_pay = (TextView) convertView.findViewById(R.id.tv_goto_pay);  //付款

        String status = allOrderBean.getStatus();
        String paytype = allOrderBean.getPaytype();
        int sendtype = Integer.valueOf(allOrderBean.getSendtype());
        int refundstate = Integer.valueOf(allOrderBean.getRefundstate());

//        if (pageFlag.equals("0")){
//            if (status.equals("-1")){
//                tv_delete_order.setVisibility(View.GONE);
//            }else{
//                tv_delete_order.setVisibility(View.VISIBLE);
//            }
//        }else{
//            tv_delete_order.setVisibility(View.GONE);
//        }

        if (status.equals("0")) {
            if (!paytype.equals("") && paytype.equals("3")) {
                //货到付款，待发货
                tv_confirm_receipt.setVisibility(View.VISIBLE);
            } else {
                //等待付款
                tv_goto_pay.setVisibility(View.VISIBLE);
                tv_delete_order.setVisibility(View.VISIBLE);
            }
        }

        if (status.equals("1")) {
            if (sendtype > 0) {
                //部分商品已发货
                tv_confirm_receipt.setVisibility(View.VISIBLE);
            } else {
                //买家已付款
                tv_remind_send.setVisibility(View.VISIBLE);
            }
        }

        if (status.equals("2")) {
            //卖家已发货
            tv_order_express.setVisibility(View.VISIBLE);
            tv_confirm_receipt.setVisibility(View.VISIBLE);
        }

        if (status.equals("3")) {
            // 交易已完成
            tv_confirm_receipt.setVisibility(View.VISIBLE);
            tv_sales_return.setVisibility(View.VISIBLE);
        }

        if (status.equals("-1")) {
            // 交易取消
            tv_delete_order.setVisibility(View.GONE);

        }

        if (refundstate > 0) {
            if (status.equals("1")) {
                //申请退款
                tv_sales_return.setVisibility(View.VISIBLE);
            } else {
                // 申请售后
                tv_sales_return.setVisibility(View.VISIBLE);
            }
        }


        final String id = allOrderBean.getId();
        final String openId = SDPreference.getInstance().getContent("openid");

        tv_delete_order.setOnClickListener(new View.OnClickListener() {
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
                            tv_order_state.setText("订单已取消");
                            tv_delete_order.setVisibility(View.GONE);
                            tv_goto_pay.setVisibility(View.GONE);
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


        if (tv_goto_pay.getVisibility() == View.VISIBLE) {
            tv_goto_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //付款
                    LogUtil.i("付款付款");

                }
            });
        }
        //TODO 数据展示-订单内容
        return convertView;
    }


}
