package com.xinspace.csevent.monitor.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;

public class ConnectDialog extends Dialog {

	private TextView textView1 ;
	private ImageView img_award_icon;
	Context context;
	private RelativeLayout rel_close;

	public ConnectDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		setCustomDialog();
	}

	private void setCustomDialog() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getContext()).inflate(R.layout.connect_dialog, null);
		textView1 = (TextView) view.findViewById(R.id.tv_award1);
		img_award_icon = (ImageView) view.findViewById(R.id.img_award_icon);
		rel_close = (RelativeLayout) view.findViewById(R.id.rel_close);
		super.setContentView(view);
	}

	public View getTextView1() {
		return textView1;
	}


	public View getImageView() {
		return img_award_icon;
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
		img_award_icon.setOnClickListener(listener);
	}

	/**
	 * 关闭 dialog的监听器
	 *
	 */
	public void setCloseDialog(View.OnClickListener listener){
		rel_close.setOnClickListener(listener);
	}

}
