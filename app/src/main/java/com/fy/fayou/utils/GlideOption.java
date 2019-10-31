package com.fy.fayou.utils;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fy.fayou.R;

public class GlideOption {

    public static RequestOptions getAvatarOption() {
        return new RequestOptions()
                .placeholder(R.color.color_e5e5e5)
                //异常占位图(当加载异常的时候出现的图片)
                .error(R.color.color_e5e5e5)
                .transform(new CircleCrop())
                //禁止Glide硬盘缓存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    public static RequestOptions getAvatarOption(int width, int height) {
        return new RequestOptions()
                .placeholder(R.mipmap.ic_avatar_default)
                //异常占位图(当加载异常的时候出现的图片)
                .error(R.mipmap.ic_avatar_default)
                .override(width, height)
                .transform(new CircleCrop())
                //禁止Glide硬盘缓存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

}
