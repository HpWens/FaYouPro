package com.fy.fayou.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NoNetworkInterceptor implements Interceptor {



    @Override
    public Response intercept(Chain chain) throws IOException {

        Log.e("----------------", "***********22222");
        return chain.proceed(chain.request());
    }
}
