package com.fy.fayou.utils;

import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.bean.ErrorApi;
import com.fy.fayou.common.ApiResult;
import com.fy.fayou.common.Constant;
import com.fy.fayou.utils.listener.OnApiErrorListener;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.ParameterizedTypeImpl;
import com.vondear.rxtool.RxTimeTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ParseUtils {

    public static <T> T parseData(String json, Class<T> t) {
        return new Gson().fromJson(json, t);
    }

    public static <T> Result<T> fromJsonObject(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return new Gson().fromJson(json, type);
    }

    public static <T> ApiResult<T> fromJsonApiResult(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(ApiResult.class, new Class[]{clazz});
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
        try {
            return new Gson().fromJson(json, listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static <T> List<T> parseListData(String json, String field, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<T>();
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(field)) {
                return parseListData(jsonObject.optString(field), clazz);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    public static <T> ArrayList<T> parseArrayListData(String json, Class<T> clazz) {
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        return new Gson().fromJson(json, listType);
    }

    public static String parseJSONObject(String json, String fieldName) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(fieldName)) {
                return jsonObject.optString(fieldName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void handlerError(ApiException apiException) {
        if (null != apiException) {
            // 未登陆
            if (apiException.getCode() == 401) {
                ARouter.getInstance().build(Constant.LOGIN)
                        .navigation();
                RxToast.normal("登录验证失效，请重新登录");
            }
        }
    }

    // fork
    public static void handlerApiError(ApiException apiException, OnApiErrorListener listener) {
        if (apiException == null) return;
        // fork 后台接口封装的错误
        if (apiException.getCode() == 500) {
            if (apiException.getCause() != null && apiException.getCause() instanceof HttpException) {
                HttpException exception = (HttpException) apiException.getCause();
                ResponseBody responseBody = exception.response().errorBody();
                if (responseBody != null) {
                    Observable.just(responseBody.byteStream())
                            .filter(inputStream -> inputStream != null)
                            .observeOn(Schedulers.io())
                            .map(inputStream -> {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                int read;
                                while ((read = inputStream.read()) != -1) {
                                    bos.write(read);
                                }
                                String str = bos.toString();
                                inputStream.close();
                                bos.close();
                                return str;
                            }).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                if (!TextUtils.isEmpty(s)) {
                                    try {
                                        ErrorApi errorApi = parseData(s, ErrorApi.class);
                                        if (!TextUtils.isEmpty(errorApi.display)) {
                                            listener.onError(errorApi.display);
                                        }
                                    } catch (Exception e) {
                                        listener.onError(s);
                                    }
                                } else {
                                    listener.onError(s);
                                }
                            });
                }
            }
        } else if (apiException.getCode() == 401) {
            ARouter.getInstance().build(Constant.LOGIN)
                    .navigation();
            RxToast.normal("登录验证失效，请重新登录");
        } else {
            if (!TextUtils.isEmpty(apiException.getMessage())) {
                listener.onError(apiException.getMessage());
            }
        }
    }

    public static boolean getFieldByJson(String json, String filed) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(filed)) {
                return jsonObject.optBoolean(filed);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 判断字符串基本没有用 body:服务异常
    private static boolean isJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static String getTime(String time) {
        if (TextUtils.isEmpty(time)) return "";
        long t = RxTimeTool.string2Milliseconds(time);
        return getTimeStateNew(t);
    }

    public static String getTimeStateNew(long long_time) {
        Timestamp time = new Timestamp(long_time);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //    System.out.println("传递过来的时间:"+format.format(time));
        //    System.out.println("现在的时间:"+format.format(now));
        long day_conver = 1000 * 60 * 60 * 24;
        long hour_conver = 1000 * 60 * 60;
        long min_conver = 1000 * 60;
        long time_conver = now.getTime() - time.getTime();
        long temp_conver;
        //    System.out.println("天数:"+time_conver/day_conver);
        if ((time_conver / day_conver) < 1) {
            temp_conver = time_conver / day_conver;
            if (temp_conver <= 2 && temp_conver >= 1) {
                return temp_conver + "天前";
            } else {
                temp_conver = (time_conver / hour_conver);
                if (temp_conver >= 1) {
                    return temp_conver + "小时前";
                } else {
                    temp_conver = (time_conver / min_conver);
                    if (temp_conver > 5) {
                        return (int) Math.ceil(temp_conver / 10f) + "0分钟前";
                    } else if (temp_conver > 1 && temp_conver <= 5) {
                        return temp_conver + "分钟前";
                    } else {
                        return "刚刚";
                    }
                }
            }
        } else {
            return format.format(time);
        }
    }

}
