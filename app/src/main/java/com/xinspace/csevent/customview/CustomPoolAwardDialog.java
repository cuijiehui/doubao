package com.xinspace.csevent.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xinspace.csevent.R;

/**
 * 抽奖池抽奖获取奖品后弹出的自定义对话框
 */
public class CustomPoolAwardDialog extends Dialog{

    private View view;
    private Button bt_back;
    private static CustomPoolAwardDialog dialog;
    private TextView tv_info;

    public CustomPoolAwardDialog(Context context) {
        super(context, R.style.dialogStyle);
    }
    //实例化一个对象
    public static CustomPoolAwardDialog getInstance(Context context){
        dialog=new CustomPoolAwardDialog(context);
        return dialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=View.inflate(getContext(),R.layout.custom_pool_award_dialog,null);
        
        //初始化组件
        setView();
        setListener();
        setContentView(view);
    }
    //设置显示文本信息
    public void setText(String text){
        tv_info.setText(text);
    }
    //设置监听器
    private void setListener() {
       bt_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(dialog!=null){
                   dialog.dismiss();
               }
           }
       });
    }
    //设置签到获取到的东西
    public void setMessage(String text){
        tv_info.setText(text);
    }
    private void setView() {
        bt_back= (Button) view.findViewById(R.id.bt_dialog_back);
        tv_info= (TextView) view.findViewById(R.id.tv_get_award_info);
    }
}
