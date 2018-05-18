package com.xinspace.csevent.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xinspace.csevent.R;

public class CustomAwardDialog extends Dialog{
    private static CustomAwardDialog dialog;
    private Button bt_get_award;
    private View view;
    private TextView award_info;

    public CustomAwardDialog(Context context) {
        super(context, R.style.dialogStyle);
    }
    //实例化一个对象
    public static CustomAwardDialog getInstance(Context context){
        dialog=new CustomAwardDialog(context);
        return dialog;
    }

    //设置监听器
    public void setOnClickListener(View.OnClickListener listener){
        bt_get_award.setOnClickListener(listener);
    }
    //设置中奖信息
    public void setMessage(String text){
        award_info.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=View.inflate(getContext(),R.layout.custom_award_dialog,null);
        //初始化组件
        setView();

        setContentView(view);
    }
    //初始化组件
    private void setView() {
        bt_get_award= (Button) view.findViewById(R.id.bt_dialog_get_award);
        award_info= (TextView) view.findViewById(R.id.huojiang_text);
    }
}
