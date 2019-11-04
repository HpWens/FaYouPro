package com.fy.fayou.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

public class MapLocationUtils {

    public AMapLocationClient mLocationClient = null;

    public MapLocationUtils(Context application) {
        //声明AMapLocationClient类对象
        //初始化定位
        mLocationClient = new AMapLocationClient(application);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    //异步获取定位结果
    AMapLocationListener mAMapLocationListener = amapLocation -> {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //解析定位结果
            }
        }
    };
}
