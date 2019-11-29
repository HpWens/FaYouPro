package com.fy.fayou.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.vondear.rxtool.RxDeviceTool;

public class SquareRecyclerView extends RecyclerView {

    public SquareRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SquareRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int heightSize = MeasureSpec.getSize(heightSpec);
        int screenHeight = RxDeviceTool.getScreenHeight(getContext());
        if (heightSize >= screenHeight * 0.75f) {
            heightSize = (int) (screenHeight * 0.75f);
        }
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.getMode(heightSpec)));
    }
}
