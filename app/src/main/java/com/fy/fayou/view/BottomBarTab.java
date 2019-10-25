package com.fy.fayou.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fy.fayou.R;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class BottomBarTab extends LinearLayout {
    private ImageView mIcon;
    private TextView mTitle;
    private Context mContext;
    private int mTabPosition = -1;

    private int mSelectedRes;
    private int mSelectedColor;

    private int mNormalRes;
    private int mNormalColor;

    public BottomBarTab(Context context, @DrawableRes int icon, String name,
                        int normalColor, int selectedRes, int selectedColor) {
        this(context, null, icon, name, normalColor, selectedRes, selectedColor);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int icon, String name, int normalColor,
                        int selectedRes, int selectedColor) {
        this(context, attrs, 0, icon, name, normalColor, selectedRes, selectedColor);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int icon, String name,
                        int normalColor, int selectedRes, int selectedColor) {
        super(context, attrs, defStyleAttr);
        this.mNormalColor = normalColor;
        this.mNormalRes = icon;
        this.mSelectedColor = selectedColor;
        this.mSelectedRes = selectedRes;
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        init(context, icon, name, mNormalColor);
    }

    private void init(Context context, int icon, String name, int color) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackgroundDrawable(drawable);
        typedArray.recycle();

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        LayoutParams params = new LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        mIcon.setImageResource(icon);
        mIcon.setLayoutParams(params);
        // mIcon.setColorFilter(ContextCompat.getColor(context, R.color.tab_unselect));
        addView(mIcon);

        mTitle = new TextView(context);
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = topMargin;
        mTitle.setLayoutParams(lp);
        mTitle.setText(name);
        mTitle.setTextColor(color);
        mTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        addView(mTitle);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            //mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
            mIcon.setImageResource(mSelectedRes);
            mTitle.setTextColor(mSelectedColor);
        } else {
            //mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.tab_unselect));
            mIcon.setImageResource(mNormalRes);
            mTitle.setTextColor(mNormalColor);
        }

        if (selected) {
            ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 1.2f, 0.8f, 1.0f);
            animator.setDuration(400);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    setScaleX(value);
                    setScaleY(value);
                }
            });
            animator.start();
        }
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }
}
