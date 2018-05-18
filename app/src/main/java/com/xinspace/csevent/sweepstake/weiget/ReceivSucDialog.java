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


public class ReceivSucDialog extends Dialog {

	private TextView tv_yes;
	Context context;

	public ReceivSucDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		setCustomDialog();
	}

	private void setCustomDialog() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_receiv_suc, null);
		tv_yes = (TextView) view.findViewById(R.id.btn_yes);
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
		tv_yes.setOnClickListener(listener);
	}

}
