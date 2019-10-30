package com.fy.fayou.utils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.common.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

public class ParseUtils {

    public static <T> T parseData(String json, Class<T> t) {
        return new Gson().fromJson(json, t);
    }

    public static <T> List<T> parseListData(String json, Class<T> t) {
        return new Gson().fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    public static void handlerError(ApiException apiException) {
        if (null != apiException) {
            // 未登陆
            if (apiException.getCode() == 401) {
                ARouter.getInstance().build(Constant.LOGIN)
                        .navigation();
                RxToast.normal("登陆验证失效，请重新登陆");
            }
        }
    }
}
