package com.xinspace.csevent.sweepstake.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by landsnail on 14-9-30.
 *
 * @author hongyun.fang
 * @email fanghongyun@gmail.com
 */
public class ScrollViewForTop extends ScrollView {

//	public interface OnScroll {
//		public void onScrollChanged(ScrollViewForTop scrollView, int x, int y, int oldx, int oldy);
//	}

	//private OnScroll onScroll;

	public ScrollViewForTop(Context context) {
		super(context);
	}

	public ScrollViewForTop(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewForTop(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}

//	public void setOnScroll(OnScroll onScroll) {
//		this.onScroll = onScroll;
//	}

	private ScrollViewListener scrollViewListener = null;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException ex) {
			return false;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			return super.onTouchEvent(ev);
		} catch (IllegalArgumentException ex) {
			return false;
		}

	}

	float mLastMotionX = 0;
	// 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
	float x1 = 0;
	float x2 = 0;
	float y1 = 0;
	float y2 = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		// getParent().requestDisallowInterceptTouchEvent(false);
		try {

			final float x = ((MotionEvent) event).getX();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionX = x;
				// 当手指按下的时候
				x1 = event.getX();
				y1 = event.getY();
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_MOVE: {
				// 当手指移动的时候
				x2 = event.getX();
				y2 = event.getY();
				float disX = x1 - x2;
				float disY = y1 - y2;
				if (Math.abs(disX) > Math.abs(disY)) {
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					if ((getChildAt(0).getMeasuredHeight() <= (getScrollY() + getHeight()))) {
						Log.i("msg", "offset---------" + getParent().toString());
						getParent().requestDisallowInterceptTouchEvent(false);
						// 获得 VerticalViewPager 的实例
						((View) getParent()).onTouchEvent(event);
					} else {
						getParent().requestDisallowInterceptTouchEvent(true);
					}
				}
			}
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}
			return super.dispatchTouchEvent(event);
		} catch (IllegalArgumentException ex) {
			Log.d("dispatchTouchEvent Exception:", ex.getLocalizedMessage());
			return false;
		}

	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}


	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}


}
