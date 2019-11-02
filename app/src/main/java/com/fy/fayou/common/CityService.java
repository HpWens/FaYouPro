package com.fy.fayou.common;

import android.content.Context;

import com.fy.fayou.bean.City;
import com.fy.fayou.utils.ParseUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CityService {

    private volatile static CityService singleton = null;

    public CityService() {
    }

    public static CityService getInstance() {
        if (singleton == null) {
            synchronized (CityService.class) {
                if (singleton == null) {
                    singleton = new CityService();
                }
            }
        }
        return singleton;
    }

    public Observable<List<City>> loadCityData(Context context) {
        return Observable.just("city_list.json")
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    InputStream inputStream = context.getAssets().open(s);
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        arrayOutputStream.write(buffer, 0, len);
                    }
                    arrayOutputStream.flush();
                    arrayOutputStream.close();
                    inputStream.close();
                    String json = new String(arrayOutputStream.toByteArray());
                    return ParseUtils.parseListData(json, City.class);
                });
    }

}
