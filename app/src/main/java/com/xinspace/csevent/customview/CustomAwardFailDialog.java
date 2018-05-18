package com.xinspace.csevent.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xinspace.csevent.R;

public class CustomAwardFailDialog extends Dialog{

    private View view;
    private Button bt_back;
    private static CustomAwardFailDialog dialog;

    public CustomAwardFailDialog(Context context) {
        super(context, R.style.dialogStyle);
    }

    //实例化一个对象
    public static CustomAwardFailDialog getInstance(Context context){
        dialog=new CustomAwardFailDialog(context);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=View.inflate(getContext(),R.layout.custom_award_fail_dialog,null);
        //初始化组件
        setView();
        //设置监听器
        setListener();
        setContentView(view);
    }

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
    private void setView() {
        bt_back= (Button) view.findViewById(R.id.fail_dialog_back);
    }
}
