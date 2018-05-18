package com.xinspace.csevent.sweepstake.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.xinspace.csevent.R;

public class RemindDialog extends Dialog {

	private TextView  btn_yes;
	Context context;

	public RemindDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		setCustomDialog();
	}

	private void setCustomDialog() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_remind, null);
		btn_yes = (TextView) view.findViewById(R.id.tv_make_sure);
		super.setContentView(view);
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


}
