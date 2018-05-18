package com.xinspace.csevent.login.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xinspace.csevent.R;

/**
 * Created by Android on 2016/10/13.
 *
 * 选择充值 元宝弹窗
 */
public class OtherGoldDialog extends Dialog{


    private Context context;
    private EditText ed_recharge_num;
    private TextView tv_recharge_money;
    private TextView tv_yes_money;


    public OtherGoldDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getContext()).inflate(R.layout.other_gold_dialog, null);
        ed_recharge_num = (EditText) view.findViewById(R.id.ed_recharge_num);
        tv_recharge_money = (TextView) view.findViewById(R.id.tv_recharge_money);
        tv_yes_money = (TextView) view.findViewById(R.id.tv_yes_money);
        super.setContentView(view);
    }



    public EditText getEditText(){
        return  ed_recharge_num;
    }

    public TextView getTextView(){
        return  tv_recharge_money;
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

    public void setOnPositiveListener(View.OnClickListener listener) {
        tv_yes_money.setOnClickListener(listener);
    }

}
