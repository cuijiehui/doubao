package com.xinspace.csevent.sweepstake.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;


public class AwardSucDialog extends Dialog {

	private Button btn_no, btn_yes;
	private TextView textView1 , textView2;
	private ImageView img_award_icon;
	Context context;

	public AwardSucDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		setCustomDialog();
	}

	private void setCustomDialog() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getContext()).inflate(R.layout.award_suc_dialog, null);
		textView1 = (TextView) view.findViewById(R.id.tv_award1);
		textView2 = (TextView) view.findViewById(R.id.tv_award2);
		img_award_icon = (ImageView) view.findViewById(R.id.img_award_icon);
		btn_no = (Button) view.findViewById(R.id.btn_no);
		btn_yes = (Button) view.findViewById(R.id.btn_yes);
		super.setContentView(view);
	}

	public View getTextView1() {
		return textView1;
	}

	public View getTextView2() {
		return textView2;
	}

	public View getImageView() {
		return img_award_icon;
	}

	public View getButton2(){
		return 	btn_yes;
	}
	
	@Override
	public void setContentView(int layoutResID) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}

	@Override
	public void setContentView(View view) {
	}

	/**
	 * 确定键监听器
	 * 
	 * @param listener
	 */
	public void setOnPositiveListener(View.OnClickListener listener) {
		btn_yes.setOnClickListener(listener);
	}

	/**
	 * 取消键监听器
	 * 
	 * @param listener
	 */
	public void setOnNegativeListener(View.OnClickListener listener) {
		btn_no.setOnClickListener(listener);
	}

}
