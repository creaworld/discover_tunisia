/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by phamngocthanh on 8/29/17.
 */

public class CustomScrollView extends ScrollView {

    // true if we can scroll the ScrollView
    // false if we cannot scroll
    private boolean scrollable = true;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, boolean scrollable) {
        super(context);
        this.scrollable = scrollable;
    }

    public CustomScrollView(Context context, AttributeSet attrs, boolean scrollable) {
        super(context, attrs);
        this.scrollable = scrollable;
    }

    public void setScrollingEnabled(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // if we can scroll pass the event to the superclass
//                if (scrollable) return super.onTouchEvent(ev);
//                // only continue to handle the touch event if scrolling enabled
//                return scrollable; // scrollable is always false at this point
//            default:
//                return super.onTouchEvent(ev);
//        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Log.i("CustomScrollView", "onInterceptTouchEvent: DOWN super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                return false; // redirect MotionEvents to ourself

            case MotionEvent.ACTION_CANCEL:
                // Log.i("CustomScrollView", "onInterceptTouchEvent: CANCEL super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_UP:
                //Log.i("CustomScrollView", "onInterceptTouchEvent: UP super false" );
                return false;

            default:
                //Log.i("CustomScrollView", "onInterceptTouchEvent: " + action );
                break;
        }

        return false;
    }

}