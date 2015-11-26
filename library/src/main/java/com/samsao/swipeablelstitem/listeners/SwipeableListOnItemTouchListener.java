package com.samsao.swipeablelstitem.listeners;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.samsao.swipeablelstitem.views.SwipeableView;

/**
 * Created by lcampos on 2015-11-05.
 */
public class SwipeableListOnItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {
    // minimum distance to enter swipe
    private final static float SWIPE_THRESHOLD = 80;

    private float mDownX = 0;
    private float mDownY = 0;
    private float mLastX = 0;
    private float mLastY = 0;
    private float mDeltaX = 0;
    private float mDeltaY = 0;
    // true if the user is swiping horizontally
    private boolean mIsSwipingHorizontally = false;
    // true if the user is swiping vertically
    private boolean mIsSwipingVertically = false;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = e.getX();
            mDownY = e.getY();
            resetSwiping(e.getX(), e.getY());
        } else if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
            boolean isSwipingHorizontally = mIsSwipingHorizontally;
            resetSwiping(e.getX(), e.getY());
            if (isSwipingHorizontally) {
                return true;
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (mIsSwipingVertically) {
                return super.onInterceptTouchEvent(rv, e);
            } else if (mIsSwipingHorizontally) {
                return true;
            } else {
                // check for swipe
                setValues(e.getX(), e.getY());

                if (Math.abs(mDeltaY) >= SWIPE_THRESHOLD) {
                    mIsSwipingVertically = true;
                    return super.onInterceptTouchEvent(rv, e);
                } else if (Math.abs(mDeltaX) >= SWIPE_THRESHOLD) {
                    mIsSwipingHorizontally = true;
                    // adjust the delta to include the threshold so the view does not skip
                    if (mDeltaX < 0) {
                        mDeltaX += SWIPE_THRESHOLD;
                    } else {
                        mDeltaX -= SWIPE_THRESHOLD;
                    }
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(rv, e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        setValues(e.getX(), e.getY());
        View childView = rv.findChildViewUnder(mDownX, mDownY);
        if (childView != null) {
            if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
                ((SwipeableView) childView).onSwipeRelease(mDeltaX);
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                ((SwipeableView) childView).onSwipe(mDeltaX);
            }
        }
    }

    /**
     * Set the x,y parameters
     *
     * @param x
     * @param y
     */
    private void setValues(final float x, final float y) {
        final float deltaX = x - mLastX;
        final float deltaY = y - mLastY;
        mLastX = x;
        mLastY = y;
        mDeltaX += deltaX;
        mDeltaY += deltaY;
    }

    /**
     * Reset swiping state
     *
     * @param x
     * @param y
     */
    private void resetSwiping(float x, float y) {
        mLastX = x;
        mLastY = y;
        mDeltaX = 0;
        mDeltaY = 0;
        mIsSwipingHorizontally = false;
        mIsSwipingVertically = false;
    }
}
