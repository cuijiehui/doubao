package sdk_sample.sdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Android on 2017/3/23.
 */

public class SlidingLinearLayout extends LinearLayout {
    private boolean isMe = false;

    public SlidingLinearLayout(Context context) {
        super(context);
    }

    public boolean isMe() {
        return this.isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == 0) {
            this.isMe = true;
        } else if(event.getAction() == 1) {
            this.isMe = false;
        }

        return false;
    }

    public boolean handleActivityEvent(MotionEvent activityEvent) {
        boolean result = false;
        if(this.isMe()) {
            LayoutParams lp;
            if(activityEvent.getAction() == 1) {
                if(this.getLeft() + this.getWidth() / 2 > ((FrameLayout)this.getParent().getParent()).getWidth() - this.getWidth()) {
                    lp = (LayoutParams)this.getLayoutParams();
                    lp.leftMargin = 0;
                    this.setLayoutParams(lp);
                    this.setMe(false);
                    result = true;
                } else {
                    lp = (LayoutParams)this.getLayoutParams();
                    lp.leftMargin = 0;
                    this.setLayoutParams(lp);
                    this.setMe(false);
                    result = false;
                }
            } else {
                lp = (LayoutParams)this.getLayoutParams();
                lp.leftMargin = (int)(activityEvent.getX() - (float)((FrameLayout)this.getParent().getParent()).getLeft()) - this.getWidth() / 2;
                if(lp.leftMargin > 0 && lp.leftMargin < ((FrameLayout)this.getParent().getParent()).getWidth() - this.getWidth()) {
                    this.setLayoutParams(lp);
                }
            }
        }

        return result;
    }
}

