package com.xinspace.csevent.login.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xinspace.csevent.R;

/**
 * Created by Android on 2016/10/13.
 *
 * 选择充值积分 元宝弹窗
 */
public class RechargeDialog extends Dialog{


    Context context;

    private RelativeLayout rel_exchange_integral , rel_recharge_gold;

    public RechargeDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getContext()).inflate(R.layout.home_recharge_dialog, null);
        rel_exchange_integral = (RelativeLayout) view.findViewById(R.id.rel_exchange_integral);
        rel_recharge_gold = (RelativeLayout) view.findViewById(R.id.rel_recharge_gold);
        super.setContentView(view);
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 积分
     *
     * @param listener
     */
    public void setOnIntegralListener(View.OnClickListener listener) {
        rel_exchange_integral.setOnClickListener(listener);
    }

    /**
     * 元宝
     *
     * @param listener
     */

    public void setOnGoldListener(View.OnClickListener listener) {
        rel_recharge_gold.setOnClickListener(listener);
    }

}
