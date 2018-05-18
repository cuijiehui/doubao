package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.util.ToastUtil;

/**
 * 选择退款原因
 *
 * Created by Android on 2017/5/18.
 */

public class ReimbursePop2 extends PopupWindow{

    private Context context;
    private View view;

    private RadioGroup radioGroup;
    private RadioButton rb_1;
    private RadioButton rb_2;
    private RadioButton rb_3;
    private RadioButton rb_4;

    private TextView tv_cancel;
    private TextView tv_yes;
    private ReimburseReSult reimburseReSult;
    private String result;

    public ReimbursePop2(Context context , ReimburseReSult reimburseReSult) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_reimburse2, null);
        this.reimburseReSult = reimburseReSult;
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context));
        //this.getCouponListener = getCouponListener;
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
    }

    private void initView() {

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(onClickListener);
        tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        tv_yes.setOnClickListener(onClickListener);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
        rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
        rb_3 = (RadioButton) view.findViewById(R.id.rb_3);
        rb_4 = (RadioButton) view.findViewById(R.id.rb_4);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(rb_1.getId()==checkedId){
                    result =  rb_1.getText().toString();
                }
                if(rb_2.getId()==checkedId){
                    result = rb_2.getText().toString();
                }
                if(rb_3.getId()==checkedId){
                    result = rb_3.getText().toString();
                }
                if(rb_4.getId()==checkedId){
                    result = rb_4.getText().toString();
                }

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancel:
                    ReimbursePop2.this.dismiss();
                    break;
                case R.id.tv_yes:
                    if (result != null && !result.equals("")){
                        reimburseReSult.setResult(result);
                        ReimbursePop2.this.dismiss();
                    }else{
                        ToastUtil.makeToast("请选择原因");
                    }
                    break;
            }
        }
    };



    public void onDestory() {
        onClickListener = null;
    }



}
