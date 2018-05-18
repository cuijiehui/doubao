package com.xinspace.csevent.monitor.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xinspace.csevent.R;

/**
 * Created by Android on 2016/10/13.
 *
 * 选择充值积分 元宝弹窗
 */
public class OpenDoorDialog extends Dialog{


    Context context;
    private RelativeLayout rel_one_door , rel_two_door;

    private Button btn_cancel;

    public OpenDoorDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_open_door, null);

        rel_one_door = (RelativeLayout) view.findViewById(R.id.rel_one_door);
        rel_two_door = (RelativeLayout) view.findViewById(R.id.rel_two_door);

        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

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

    public void setOneListener(View.OnClickListener listener) {
        rel_one_door.setOnClickListener(listener);
    }

    public void setTwoListener(View.OnClickListener listener) {
        rel_two_door.setOnClickListener(listener);
    }

    public void setCancelListener(View.OnClickListener listener){
        btn_cancel.setOnClickListener(listener);
    }

}
