package com.xinspace.csevent.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.data.biz.AddressAdapterBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.login.view.MyHorizontalScrollView;
import com.xinspace.csevent.login.weiget.ClickListener;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.AddressManagerActivity;

import java.util.List;

/**
 * Created by lizhihong on 2015/11/26.
 * 此类为地址适配器
 */
public class AddressAdapter extends BaseAdapter implements HttpRequestListener {

    Context context;
    List<GetAddressEntity> list;

    public void setList(List<GetAddressEntity> list) {
        this.list = list;
    }

    LayoutInflater inflater;

    private View view;
    // 屏幕宽度,由于我们用的是HorizontalScrollView,所以按钮选项应该在屏幕外
    private int mScreentWidth;

    private ClickListener clickListener;

    public AddressAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_addressmanager_list2, null);
            holder = new ViewHolder();
            holder.hSView = (MyHorizontalScrollView) convertView.findViewById(R.id.horizontalScrollView);
            holder.lin_content = (LinearLayout) convertView.findViewById(R.id.lin_content);
            mScreentWidth = ScreenUtils.getScreenWidth(context);
            ViewGroup.LayoutParams lp = holder.lin_content.getLayoutParams();
            lp.width = mScreentWidth;
            holder.ll_action = (LinearLayout) convertView.findViewById(R.id.ll_action);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_address_manager_name);
            holder.tvPhoneNum = (TextView) convertView.findViewById(R.id.tv_address_manager_phone);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address_manager_address);
            holder.tv_address_pcd = (TextView) convertView.findViewById(R.id.tv_address_pcd);
            holder.button1 = (Button) convertView.findViewById(R.id.button1);
            holder.but_delete = (Button) convertView.findViewById(R.id.but_delete);
            holder.tv_default_address = (TextView) convertView.findViewById(R.id.tv_default_address);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        //給holder中的控件赋值

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (view != null) {
                            ViewHolder viewHolder1 = (ViewHolder) view.getTag();
                            viewHolder1.hSView.smoothScrollTo(0, 0);
                            notifyDataSetChanged();
                        }
                    case MotionEvent.ACTION_UP:
                        // 获得ViewHolder
                        ViewHolder viewHolder = (ViewHolder) v.getTag();
                        view = v;
                        // 获得HorizontalScrollView滑动的水平方向值.
                        int scrollX = viewHolder.hSView.getScrollX();
                        // 获得操作区域的长度
                        int actionW = viewHolder.ll_action.getWidth();

                        if (scrollX <= actionW / 2) {
                            viewHolder.hSView.smoothScrollTo(0, 0);
                        } else {
                            viewHolder.hSView.smoothScrollTo(actionW, 0);
                        }
                        return true;
                }
                return false;
            }
        });
        // 这里防止删除一条item后,ListView处于操作状态,直接还原
        if (holder.hSView.getScrollX() != 0) {
            holder.hSView.scrollTo(0, 0);
        }

        final GetAddressEntity entity = list.get(position);
        LogUtil.i("holder中的实体类:" + entity.toString());
        holder.tvName.setText(entity.getRealname());
        holder.tvPhoneNum.setText(entity.getMobile());
        holder.tvAddress.setText(entity.getAddress());
        holder.tv_address_pcd.setText(entity.getProvince() + entity.getCity() + entity.getArea());

        if (entity.getIsdefault().equals("1")){
            holder.tv_default_address.setVisibility(View.VISIBLE);
        }else{
            holder.tv_default_address.setVisibility(View.GONE);
        }

        //点击删除地址
        holder.but_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("www" , "点击删除地址记录了");
                String id = entity.getId();
                AddressAdapterBiz.addressDelete(context, id, AddressAdapter.this);
            }
        });

        //点击进入编辑页面
        holder.lin_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, AddressEditActivity.class);
//                //把信息传到编辑页面
//                intent.putExtra("id", entity.getId());
//                intent.putExtra("address", entity.getAddress());
//                intent.putExtra("name", entity.getName());
//                intent.putExtra("tel", entity.getRtel());
//                intent.putExtra("province" , entity.getProvince());
//                intent.putExtra("city" , entity.getCity());
//                intent.putExtra("area" , entity.getArea());
//                intent.putExtra("pcd" ,entity.getProvince() + entity.getCity() + entity.getArea());
//                context.startActivity(intent);
                clickListener.clickListenet(true , position);
            }
        });

        //点击文字设为默认地址
        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = entity.getId();
                AddressAdapterBiz.changeDefaultAddress(context, id, AddressAdapter.this);
            }
        });
        return convertView;
    }

    class ViewHolder {
        MyHorizontalScrollView hSView;
        TextView tvName;
        TextView tvPhoneNum;
        TextView tvAddress;
        TextView tv_address_pcd;
        Button button1;
        Button but_delete;
        LinearLayout lin_content, ll_action;
        TextView tv_default_address;
    }

    /**
     * 发请求后的回调接口
     */
    @Override
    public void onHttpRequestFinish(String result) {
        //再次发请求,获取新数据更新listview
        AddressManagerActivity activity = (AddressManagerActivity) context;
        activity.getAddresses();
    }

    @Override
    public void onHttpRequestError(String error) {

    }
}
