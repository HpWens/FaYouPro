package com.fy.fayou.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fy.fayou.R;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;

import java.util.Random;

public class GlideOption {

    public static RequestOptions getAvatarOption() {
        return new RequestOptions()
                .placeholder(R.mipmap.ic_avatar_default)
                //异常占位图(当加载异常的时候出现的图片)
                .error(R.mipmap.ic_avatar_default)
                .transform(new CircleCrop())
                //禁止Glide硬盘缓存缓存
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }

    public static RequestOptions getAvatarOption(int width, int height) {
        return new RequestOptions()
                .placeholder(R.mipmap.ic_avatar_default)
                //异常占位图(当加载异常的时候出现的图片)
                .error(R.mipmap.ic_avatar_default)
                .override(width, height)
                .transform(new CircleCrop())
                //禁止Glide硬盘缓存缓存
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }

    public static RequestOptions getItemCircleOption(int width, int height) {
        int w = RxImageTool.dp2px(width);
        int h = RxImageTool.dp2px(height);
        return new RequestOptions()
                .placeholder(R.mipmap.ic_avatar_default)
                .error(R.mipmap.ic_avatar_default)
                .override(w, h)
                .transform(new CircleCrop());
    }

    public static RequestOptions getItemOptionByWH(int width, int height) {
        int w = RxImageTool.dp2px(width);
        int h = RxImageTool.dp2px(height);
        return new RequestOptions()
                .placeholder(R.mipmap.ic_avatar_default)
                .error(R.mipmap.ic_avatar_default)
                .override(w, h)
                .transform(new CenterCrop());
    }

    public static RequestOptions getFullScreenWROption(Context context, int radius) {
        int w = RxDeviceTool.getScreenWidth(context) - RxImageTool.dp2px(30);
        int h = w * 9 / 16;
        return new RequestOptions().override(w, h).transform(new CenterCrop(), new RoundedCorners(RxImageTool.dp2px(radius)));
    }

    public static RequestOptions getFullScreenWOption(Context context) {
        int w = RxDeviceTool.getScreenWidth(context) - RxImageTool.dp2px(30);
        int h = w * 9 / 16;
        return new RequestOptions().override(w, h).centerCrop();
    }

    public static RequestOptions getRadiusOption(int width, int height, int radius) {
        int w = RxImageTool.dp2px(width);
        int h = RxImageTool.dp2px(height);
        return new RequestOptions()
                .override(w, h)
                .transform(new CenterCrop(), new RoundedCorners(RxImageTool.dp2px(radius)));
    }

    @SuppressLint("ResourceType")
    public static RequestOptions getItemOption(int width, int height) {
        int w = RxImageTool.dp2px(width);
        int h = RxImageTool.dp2px(height);
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return new RequestOptions()
                .override(w, h)
                .placeholder(color)
                //异常占位图(当加载异常的时候出现的图片)
                .error(R.mipmap.base_net_error);
    }

    @SuppressLint("ResourceType")
    public static RequestOptions getRandomColorItemOption(int width, int height) {
        int w = RxImageTool.dp2px(width);
        int h = RxImageTool.dp2px(height);
        return new RequestOptions()
                .override(w, h)
                .placeholder(Color.parseColor(getRandColor()))
                //异常占位图(当加载异常的时候出现的图片)
                .error(R.mipmap.base_net_error);
    }

    public static String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }

}
