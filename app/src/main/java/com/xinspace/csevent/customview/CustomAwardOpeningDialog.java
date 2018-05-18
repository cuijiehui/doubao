package com.xinspace.csevent.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinspace.csevent.R;

/**
 * 正在开奖对话框
 */
public class CustomAwardOpeningDialog extends Dialog{

    private View view;
    private static CustomAwardOpeningDialog dialog;
    private ProgressBar progressBar;
    private TextView tv_progress;

    public CustomAwardOpeningDialog(Context context) {
        super(context, R.style.dialogStyle);
    }
    //实例化一个对象
    public static CustomAwardOpeningDialog getInstance(Context context){
        dialog=new CustomAwardOpeningDialog(context);
        return dialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=View.inflate(getContext(),R.layout.custom_award_opening_dialog,null);
        
        //初始化组件
        setView();
        setContentView(view);
    }


    public TextView getTextView(){
        return tv_progress;
    }

    //设置进度
    public void setProgress(int progress){
        progressBar.setProgress(progress);
    }
    //初始化
    private void setView() {
        progressBar= (ProgressBar) view.findViewById(R.id.dianlog_opening_award_progressbar);
        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
    }
}
