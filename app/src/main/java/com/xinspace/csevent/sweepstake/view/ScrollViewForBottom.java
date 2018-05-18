package com.xinspace.csevent.sweepstake.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


/**
 * Created by landsnail on 14-9-30.
 *
 * @author hongyun.fang
 * @email fanghongyun@gmail.com
 *
 */
public class ScrollViewForBottom extends ScrollView {

//	public interface OnScroll {
//		public void onScrollChanged(ScrollViewForBottom scrollView, int x, int y, int oldx, int oldy);
//	}

	private ScrollFootViewListener scrollFootViewListener = null;

	//private OnScroll onScroll;

	public ScrollViewForBottom(Context context) {
		super(context);
	}

	public ScrollViewForBottom(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewForBottom(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}

//	public void setOnScroll(OnScroll onScroll) {
//		this.onScroll = onScroll;
//	}

	float mLastMotionY = 0;
	float x1 = 0;
	float x2 = 0;
	float y1 = 0;
	float y2 = 0;

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

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		try {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionY = event.getY();
				x1 = event.getX();
				y1 = event.getY();
				getParent().requestDisallowInterceptTouchEvent(true);
				break;

			case MotionEvent.ACTION_MOVE: {

				float direction = mLastMotionY - event.getY();
				mLastMotionY = event.getY();
				x2 = event.getX();
				y2 = event.getY();
				float disX = x1 - x2;
				float disY = y1 - y2;
				if (Math.abs(disX) > Math.abs(disY)) {
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					if (disY > 5) {
						if (getScrollY() <= 0) {
							getParent().requestDisallowInterceptTouchEvent(true);
							((View) getParent()).onTouchEvent(event);
						} else {
							getParent().requestDisallowInterceptTouchEvent(false);
						}
					} else {
						getParent().requestDisallowInterceptTouchEvent(false);
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
			//LogManger.d("dispatchTouchEvent Exception:", ex.getLocalizedMessage());
			return false;
		}
	}

//	@Override
//	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		super.onScrollChanged(l, t, oldl, oldt);
//		if (onScroll != null) {
//			onScroll.onScrollChanged(this, l, t, oldl, oldt);
//		}
//	}


	public void setScrollFootViewListener(ScrollFootViewListener scrollFootViewListener) {
		this.scrollFootViewListener = scrollFootViewListener;
	}


	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollFootViewListener != null) {
			scrollFootViewListener.onFootScrollChanged(this, x, y, oldx, oldy);
		}
	}


}
