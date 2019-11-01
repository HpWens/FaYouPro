package com.fy.fayou.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.vondear.rxtool.RxDeviceTool;

/**
 * Created by wenshi on 2019/7/8.
 * Description
 */
public class PercentRelativeLayout extends RelativeLayout {

    private float percent = 1.0f;

    private float maxPercent = 16 / 9f;

    public PercentRelativeLayout(Context context) {
        this(context, null);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        int heightSize = (int) (MeasureSpec.getSize(widthMeasureSpec) * percent);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.getMode(widthMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    // 根据真实的宽高比重新测量
    public void reMeasure(String path) {
        // 排除网络图片 直接在url中获取宽高
        if (path.startsWith("http://") || path.startsWith("https://")) {
            //  UrlUtil.getWidthHeightByUrl(path);
            int[] whArray = {640, 480};
            if (whArray == null) return;
            float realPercent = whArray[1] / (whArray[0] * 1.0f);
            percent = realPercent >= maxPercent ? maxPercent : realPercent;
            requestLayout();
            return;
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            return;
        }
        // 获取到图片的真实宽高
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        if (w == 0 || h == 0) return;
        float realPercent = h / (w * 1.0f);
        percent = realPercent >= maxPercent ? maxPercent : realPercent;
        requestLayout();
    }

    // 获取父类宽高
    public int[] getWH() {
        if (getWidth() == 0 || getHeight() == 0) {
            return new int[]{RxDeviceTool.getScreenWidth(getContext()), RxDeviceTool.getScreenWidth(getContext())};
        }
        return new int[]{getWidth(), (int) (getWidth() * percent)};
    }

}
