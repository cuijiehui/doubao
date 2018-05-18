package com.xinspace.csevent.login.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {

	public MyHorizontalScrollView(Context context) {
		super(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}

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

	float mLastMotionY = 0;
	float x1 = 0;
	float x2 = 0;
	float y1 = 0;
	float y2 = 0;

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		try {
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				mLastMotionY = event.getY();
//				x1 = event.getX();
//				y1 = event.getY();
//				getParent().requestDisallowInterceptTouchEvent(true);
//				break;
//
//			case MotionEvent.ACTION_MOVE: {
//
//				float direction = mLastMotionY - event.getY();
//				mLastMotionY = event.getY();
//				x2 = event.getX();
//				y2 = event.getY();
//				float velocityX = x1 - x2;
//				float velocityY = y1 - y2;
//				if (velocityX < velocityY) {
//					getParent().requestDisallowInterceptTouchEvent(false);
//				} else {
//					if (getScrollX() == 0) {
//						if (velocityX > 0) {// 向右滑
//							getParent().requestDisallowInterceptTouchEvent(false);
//						} else {// 向左滑
//							getParent().requestDisallowInterceptTouchEvent(true);
//						}
//
//					} else {
//						getParent().requestDisallowInterceptTouchEvent(true);
//					}
//				}
//			}
//				break;
//
//			case MotionEvent.ACTION_UP:
//			case MotionEvent.ACTION_CANCEL:
//				getParent().requestDisallowInterceptTouchEvent(false);
//				break;
//			}
//			return super.dispatchTouchEvent(event);
//		} catch (IllegalArgumentException ex) {
//			Log.d("dispatchTouchEvent Exception:", ex.getLocalizedMessage());
//			return false;
//		}
//	}

}
