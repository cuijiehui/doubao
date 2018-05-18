package com.xinspace.csevent.monitor.weiget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.xinspace.csevent.R;

public class VideoHintDialog extends Dialog {

	private TextView textView1 ;
	Context context;
	private Button btn_no , btn_yes;


	public VideoHintDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		setCustomDialog();
	}

	private void setCustomDialog() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getContext()).inflate(R.layout.video_hint_dialog, null);
		textView1 = (TextView) view.findViewById(R.id.tv_award1);

		btn_no = (Button) view.findViewById(R.id.btn_no);
		btn_yes = (Button) view.findViewById(R.id.btn_yes);

		super.setContentView(view);
	}

	public View getTextView1() {
		return textView1;
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
	 * 关闭 dialog的监听器
	 *
	 */

	public void setOnNegativeListener(View.OnClickListener listener) {
		btn_no.setOnClickListener(listener);
	}



}
