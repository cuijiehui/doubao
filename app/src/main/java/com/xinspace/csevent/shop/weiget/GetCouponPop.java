package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.shop.adapter.CouponAdapter;
import com.xinspace.csevent.shop.modle.CouponBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/4/14.
 *
 * 领取优惠券的弹窗
 */

public class GetCouponPop extends PopupWindow {

    private Context context;
    private View view;
    private TextView tv_close_pop;
    private ListView lv_coupon;
    private int screenWidth;
    private List<CouponBean> couponList;
    private CouponAdapter adapter;
    private GetCouponListener getCouponListener;


    public GetCouponPop(Context context , GetCouponListener getCouponListener) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_get_coupon, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context));
        this.getCouponListener = getCouponListener;
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
    }

    private void initView() {

        tv_close_pop = (TextView) view.findViewById(R.id.tv_close_pop);
        tv_close_pop.setOnClickListener(onClickListener);

        lv_coupon = (ListView) view.findViewById(R.id.lv_coupon);
        screenWidth = ScreenUtils.getScreenWidth(context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lv_coupon.getLayoutParams();
        params.height = (int) (screenWidth * 1.0f);
        params.width = (int) (screenWidth * 1.0f);
        lv_coupon.setLayoutParams(params);

        couponList = new ArrayList<CouponBean>();
        for (int i = 0 ; i < 3 ; i++){
            CouponBean bean = new CouponBean();
            couponList.add(bean);
        }
        adapter = new CouponAdapter(context);
        adapter.setList(couponList);
        lv_coupon.setAdapter(adapter);
        lv_coupon.setOnItemClickListener(onItemClickListener);

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            getCouponListener.sendCoupon(couponList.get(position));
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_close_pop:

                    break;
            }
            GetCouponPop.this.dismiss();
        }
    };



    public void onDestory(){
        onItemClickListener = null;
        onClickListener = null;
        couponList = null;
    }


}
