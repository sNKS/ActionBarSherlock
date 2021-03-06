package com.actionbarsherlock.internal.widget;

import com.actionbarsherlock.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ActionBarContainer extends FrameLayout {
    private boolean mIsTransitioning;

    public ActionBarContainer(Context context) {
        this(context, null);
    }
    public ActionBarContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SherlockTheme);
        setBackgroundDrawable(a.getDrawable(R.styleable.SherlockTheme_abBackground));
        a.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIsTransitioning || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    public void setTransitioning(boolean transitioning) {
        mIsTransitioning = transitioning;
        setDescendantFocusability(transitioning ? ViewGroup.FOCUS_BLOCK_DESCENDANTS : ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }
}