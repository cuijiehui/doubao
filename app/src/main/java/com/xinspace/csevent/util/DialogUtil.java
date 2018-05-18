package com.xinspace.csevent.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.MyCoinBiz;
import com.xinspace.csevent.myinterface.DialogListener;


public class DialogUtil {
    private static AlertDialog dialog;

    /**
     * @param context 上下文对象
     * @param title 对话框标题
     * @param msg 提示消息
     * @param positiveListener 确定监听
     * @param negativeListener 取消监听
     */
    public static void showTipsDailog(Context context,String title,String msg,DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title)
        .setPositiveButton("确定", positiveListener)
        .setNegativeButton("取消", negativeListener);
        dialog=builder.create();
        dialog.show();
    }

    /**
     * 关闭提示框
     */
    public static void close(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    /**
     * 得到自定义的progressDialog,效果为显示菊花转圈
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.dialog_loading);// 创建自定义样式dialog

        //loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }


    /**
     *
     * @param context
     * @param layoutBackground  dialog布局 ,主要是不同布局的背景图的不同
     * @return
     */
    public static Dialog createLoadingTutorial(Context context, int layoutBackground) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layoutBackground, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_tutorial);// 加载布局
//        // main.xml中的ImageView
//        Button btClose = (Button) v.findViewById(R.id.bt_dialog_close_gui);
//        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字


//        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//        tipTextView.setText(msg);// 设置加载信息

        final Dialog loadingDialog = new Dialog(context, R.style.dialog_newcomer_gui);// 创建自定义样式dialog

//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.cancel();
            }
        });
        return loadingDialog;
    }

    /**
     * 得到自定义的qiandaoDialog,效果为显示抽奖转盘转圈
     * @param context
     * @return
     */
    public static Dialog createQianDaoDialog(Context context,float radius) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_qiandao, null);// 得到加载view
        FrameLayout layout = (FrameLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView ivZhuanpan = (ImageView) v.findViewById(R.id.zhuanpan);
        ImageView ivZhizhen = (ImageView) v.findViewById(R.id.zhizhen);

//        // 加载动画
//        Animation animation = AnimationUtils.loadAnimation(
//                context, R.anim.qiandao_animation);
//        RotateAnimation (float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
//        参数说明：
//        float fromDegrees：旋转的开始角度。
//        float toDegrees：旋转的结束角度。
//        int pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//        float pivotXValue：X坐标的伸缩值。
//        int pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//        float pivotYValue：Y坐标的伸缩值。
        Animation animation = new RotateAnimation(0f,1440f+radius,Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
                animation.setInterpolator(interpolator);//旋转方式
                animation.setDuration(2000);//旋转时间
                animation.setFillAfter(true);//停在最后
        // 使用ImageView显示动画
        ivZhuanpan.startAnimation(animation);//转动转盘
        Dialog qiandaoDialog = new Dialog(context, R.style.dialog_loading);// 创建自定义样式dialog

        //qiandaoDialog.setCancelable(false);// 不可以用“返回键”取消
        qiandaoDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return qiandaoDialog;
    }

    public static Dialog createRelativeDialog(Context context, final DialogListener listener) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_relative, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        TextView tvZhen = (TextView) v.findViewById(R.id.dialog_zhen);
        TextView tvBengong = (TextView) v.findViewById(R.id.dialog_bengong);
        TextView tvBenbaobao = (TextView) v.findViewById(R.id.dialog_benbaobao);
        TextView tvJiyou = (TextView) v.findViewById(R.id.dialog_jiyou);
        TextView tvGuimi = (TextView) v.findViewById(R.id.dialog_guimi);
        TextView tvLaogong = (TextView) v.findViewById(R.id.dialog_laogong);
        TextView tvLaopo = (TextView) v.findViewById(R.id.dialog_laopo);
        TextView tvFuhuang = (TextView) v.findViewById(R.id.dialog_fuhuang);
        TextView tvMuhou = (TextView) v.findViewById(R.id.dialog_muhou);

        tvZhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("朕");
            }
        });
        tvBengong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("本宫");
            }
        });
        tvBenbaobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("本宝宝");
            }
        });
        tvJiyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("基友");
            }
        });
        tvGuimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("闺蜜");
            }
        });
        tvLaogong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("老公");
            }
        });
        tvLaopo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("老婆");
            }
        });
        tvMuhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("母后");
            }
        });
        tvFuhuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogFinish("父皇");
            }
        });

        Dialog loadingDialog = new Dialog(context, R.style.dialog_loading);// 创建自定义样式dialog

        //loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }
}

