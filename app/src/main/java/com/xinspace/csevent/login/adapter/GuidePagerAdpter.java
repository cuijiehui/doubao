package com.xinspace.csevent.login.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xinspace.csevent.util.ImagerLoaderUtil;
import java.util.ArrayList;
import java.util.List;

public class GuidePagerAdpter extends PagerAdapter {
	private String[] imgSrcs;
	private List<ImageView> views = new ArrayList<ImageView>();
	private Context context;

	public GuidePagerAdpter(String[] imgSrcs, Context context) {
		super();
		this.imgSrcs = imgSrcs;
		this.context = context;
		for (int i = 0; i < imgSrcs.length; i++) {
			LayoutInflater inflater = LayoutInflater.from(context);
			ImageView view = new ImageView(context);
			views.add(view);
		}

	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = views.get(position);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		ImagerLoaderUtil.displayImage("assets://" + imgSrcs[position],imageView);
		container.addView(imageView);
		return views.get(position);
	}

}
