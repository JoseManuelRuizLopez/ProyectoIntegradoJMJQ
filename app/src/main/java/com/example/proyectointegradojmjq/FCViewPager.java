package com.example.proyectointegradojmjq;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class FCViewPager extends ViewPager {
    private boolean enableSwipe;

    public FCViewPager(Context context) {
        super(context);
        this.enableSwipe = true;
        init();
    }

    public FCViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        enableSwipe = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enableSwipe) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enableSwipe) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled)
    {
        this.enableSwipe = enabled;
    }
}
