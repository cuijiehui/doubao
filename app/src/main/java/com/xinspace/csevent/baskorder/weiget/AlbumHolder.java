package com.xinspace.csevent.baskorder.weiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;


public class AlbumHolder {
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private int width;
	private RelativeLayout rl_dir_item;

	private AlbumHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		// setTag
		mConvertView.setTag(this);
		width = new DisplayMetrics().widthPixels / 3;

	}

	/**
	 * 拿到个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static AlbumHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		AlbumHolder holder = null;
		if (convertView == null) {
			holder = new AlbumHolder(context, parent, layoutId, position);
		} else {
			holder = (AlbumHolder) convertView.getTag();
			holder.rl_dir_item = (RelativeLayout) convertView.findViewById(R.id.rl_dir_item);
			holder.mPosition = position;
		}
		return holder;
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public AlbumHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public AlbumHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @return
	 */
	public AlbumHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width;
		view.setLayoutParams(layoutParams);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @return
	 */
	public AlbumHolder setImageByUrl(int viewId, String url) {
		//Glide.with(mConvertView.getContext()).load(url).into((ImageView) getView(viewId));
		return this;
	}

	public int getPosition() {
		return mPosition;
	}

}
