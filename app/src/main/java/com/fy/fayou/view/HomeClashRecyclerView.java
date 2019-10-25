package com.fy.fayou.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HomeClashRecyclerView extends RecyclerView {

    float startX;
    float startY;
    float moveX;
    float moveY;
    boolean isSelectTouch = false;

    private OnScrollClashListener mListener;

    public HomeClashRecyclerView(@NonNull Context context) {
        super(context);
    }

    public HomeClashRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeClashRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                if (mListener != null) {
                    mListener.onScroll(false);
                }
                isSelectTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();
                if (!isSelectTouch) {
                    if (Math.abs(moveY - startY) > Math.abs(moveX - startX)) {
                        if (mListener != null) {
                            mListener.onScroll(false);
                        }
                        // 上下滑动
                        isSelectTouch = true;
                    } else if (Math.abs(moveX - startX) > Math.abs(moveY - startY)) {
                        if (mListener != null) {
                            mListener.onScroll(true);
                        }
                        // 左右滑动
                        isSelectTouch = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mListener != null) {
                    mListener.onScroll(true);
                }
                isSelectTouch = false;
                break;
        }

        return super.onTouchEvent(event);
    }

    public interface OnScrollClashListener {
        void onScroll(boolean isScroll);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    public void setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
    }
}
