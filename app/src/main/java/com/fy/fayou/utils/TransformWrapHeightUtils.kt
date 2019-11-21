package com.fy.fayou.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget
import com.vondear.rxtool.RxDeviceTool
import com.vondear.rxtool.RxImageTool

class TransformWrapHeightUtils : ImageViewTarget<Bitmap> {

    val target: ImageView

    constructor(target: ImageView) : super(target) {
        this.target = target
    }

    override fun setResource(resource: Bitmap?) {
        val width = resource?.width ?: 0
        val height = resource?.height ?: 0

        var screenWidth = RxDeviceTool.getScreenWidth(target.context);

        screenWidth -= RxImageTool.dp2px(30f);

        var realHeight: Int = (screenWidth.toFloat() / width * height).toInt();

        if (realHeight <= 200) {
            realHeight = 200;
        } else if (realHeight >= 1920) {
            realHeight = 1920;
        }

        var params = target.layoutParams
        params.height = realHeight;
        target.layoutParams = params

        view.setImageBitmap(resource)
    }


}