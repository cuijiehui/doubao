package com.xinspace.csevent.sweepstake.adapter;

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
import com.xinspace.csevent.data.biz.AddressAdapterBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.weiget.UIAlertView;
import com.xinspace.csevent.sweepstake.activity.ChangeAddressAct;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.AddressEditActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lizhihong on 2015/11/26.
 * 此类为地址适配器
 */
public class ChangeAddressAdapter extends BaseAdapter {

    Context context;
    List<GetAddressEntity> list;
    LayoutInflater inflater;

    private View view;
    // 屏幕宽度,由于我们用的是HorizontalScrollView,所以按钮选项应该在屏幕外
    private int mScreentWidth;

    public ChangeAddressAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<GetAddressEntity> list) {
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_addressmanager_list, null);
            holder = new ViewHolder();
            mScreentWidth = ScreenUtils.getScreenWidth(context);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_address_manager_name);
            holder.tvPhoneNum = (TextView) convertView.findViewById(R.id.tv_address_manager_phone);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address_manager_address);
            //holder.tv_address_pcd = (TextView) convertView.findViewById(R.id.tv_address_pcd);
            holder.tv_default_address = (TextView) convertView.findViewById(R.id.tv_default_address);
            holder.rl_address_manager_item_delete = (RelativeLayout) convertView.findViewById(R.id.rl_address_manager_item_delete);
            holder.rl_address_manager_item_edit = (RelativeLayout) convertView.findViewById(R.id.rl_address_manager_item_edit);
            holder.iv_default_address = (ImageView) convertView.findViewById(R.id.iv_default_address);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        //給holder中的控件赋值

        final GetAddressEntity entity = list.get(position);
        holder.tvName.setText(entity.getRealname());
        holder.tvPhoneNum.setText(entity.getMobile());
        holder.tvAddress.setText(entity.getProvince() + entity.getCity() + entity.getArea() + entity.getAddress());
        //holder.tv_address_pcd.setText(entity.getProvince() + entity.getCity() + entity.getArea());

        if (entity.getIsdefault().equals("1")) {
            ImagerLoaderUtil.displayImage("drawable://" + R.drawable.userdeal, holder.iv_default_address);
            holder.tv_default_address.setText("默认地址");
        } else {
            ImagerLoaderUtil.displayImage("drawable://" + R.drawable.icon_cb_address_no, holder.iv_default_address);
            holder.tv_default_address.setText("设为默认地址");
        }

        //点击进入编辑页面
        holder.rl_address_manager_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddressEditActivity.class);
                //把信息传到编辑页面
                intent.putExtra("id", entity.getId());
                intent.putExtra("address", entity.getAddress());
                intent.putExtra("name", entity.getRealname());
                intent.putExtra("tel", entity.getMobile());
                intent.putExtra("province", entity.getProvince());
                intent.putExtra("city", entity.getCity());
                intent.putExtra("area", entity.getArea());
                intent.putExtra("pcd", entity.getProvince() + entity.getCity() + entity.getArea());
                context.startActivity(intent);
            }
        });

        //删除地址
        holder.rl_address_manager_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDelDialog(entity);

//                AddressAdapterBiz.addressDelete2( entity.getId() , entity.getOpenid() , new HttpRequestListener() {
//                    @Override
//                    public void onHttpRequestFinish(String result) throws JSONException {
//                        JSONObject jsonObject = new JSONObject(result);
//                        if (jsonObject.getString("code").equals("200")){
//                            ChangeAddressAct activity = (ChangeAddressAct) context;
//                            activity.getAddresses();
//                        }else{
//                            ToastUtil.makeToast("删除失败");
//                        }
//                    }
//
//                    @Override
//                    public void onHttpRequestError(String error) {
//
//                    }
//                });
            }
        });


        //删除地址
        holder.iv_default_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressAdapterBiz.changeDefaultAddress2(entity.getId(), entity.getOpenid(), new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("200")) {
                            ChangeAddressAct activity = (ChangeAddressAct) context;
                            activity.getAddresses();
                        } else {
                            ToastUtil.makeToast("删除失败");
                        }
                    }

                    @Override
                    public void onHttpRequestError(String error) {

                    }
                });
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvPhoneNum;
        TextView tvAddress;
        TextView tv_default_address;
        RelativeLayout rl_address_manager_item_edit;
        RelativeLayout rl_address_manager_item_delete;
        ImageView iv_default_address;
    }

    /**
     * 发请求后的回调接口
     */
//    @Override
//    public void onHttpRequestFinish(String result) {
//        //再次发请求,获取新数据更新listview
//        AddressManagerActivity activity = (AddressManagerActivity) context;
//        activity.getAddresses();
//    }
//
//    @Override
//    public void onHttpRequestError(String error) {
//
//    }
    private void showDelDialog(final GetAddressEntity bean) {
        final UIAlertView delDialog = new UIAlertView(context, "温馨提示", "确认删除此收货地址么?",
                "取消", "确定");
        delDialog.show();

        delDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {

                                       @Override
                                       public void doLeft() {
                                           delDialog.dismiss();
                                       }

                                       @Override
                                       public void doRight() {
                                           deteAdd(bean);
                                           delDialog.dismiss();
                                       }
                                   }
        );
    }


    private void deteAdd(GetAddressEntity bean) {
        AddressAdapterBiz.addressDelete2(bean.getId(), bean.getOpenid(), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")) {
                    ChangeAddressAct activity = (ChangeAddressAct) context;
                    activity.getAddresses();
                } else {
                    ToastUtil.makeToast("删除失败");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


}
