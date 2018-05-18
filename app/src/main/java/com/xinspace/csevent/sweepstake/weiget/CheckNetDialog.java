package com.xinspace.csevent.sweepstake.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.xinspace.csevent.R;


public class CheckNetDialog extends Dialog {

	private Button btn_no, btn_yes;
	private TextView textView;
	Context context;

	public CheckNetDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		setCustomDialog();
	}

	private void setCustomDialog() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getContext()).inflate(R.layout.check_net_dialog, null);
		textView = (TextView) view.findViewById(R.id.tv_back_dialog);
		btn_no = (Button) view.findViewById(R.id.btn_no);
		btn_yes = (Button) view.findViewById(R.id.btn_yes);
		super.setContentView(view);
	}

	public View getTextView() {
		return textView;
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
