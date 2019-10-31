package com.fy.fayou.utils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.common.Constant;
import com.google.gson.Gson;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.ParameterizedTypeImpl;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.exception.ApiException;

import java.lang.reflect.Type;
import java.util.List;

public class ParseUtils {

    public static <T> T parseData(String json, Class<T> t) {
        return new Gson().fromJson(json, t);
    }

    public static <T> Result<T> fromJsonObject(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return new Gson().fromJson(json, type);
    }

    public static <T> Result<List<T>> fromJsonArray(String json, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Result.class, new Type[]{listType});
        return new Gson().fromJson(json, type);
    }

    public static <T> List<T> parseListData(String json, Class<T> clazz) {
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        return new Gson().fromJson(json, listType);
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
