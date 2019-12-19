package com.fy.fayou.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;

public class MapLocationUtils {

    public AMapLocationClient mLocationClient = null;

    public MapLocationUtils(Context application) {
        //声明AMapLocationClient类对象
        //初始化定位
        mLocationClient = new AMapLocationClient(application);
        //设置定位回调监听
        mLocationClient.setLocationListener(amapLocation -> {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    amapLocation.getCity();

                    mLocationClient.stopLocation();
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    public static void startLocation(Context context, OnLocationListener listener) {
        AMapLocationClient locationClient = new AMapLocationClient(context);
        locationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    String city = aMapLocation.getCity();
                    listener.location(city);
                    locationClient.stopLocation();
                } else {
                    locationClient.stopLocation();
                }
            }
        });
        locationClient.startLocation();
    }

    public interface OnLocationListener {
        void location(String cityName);
    }
}
