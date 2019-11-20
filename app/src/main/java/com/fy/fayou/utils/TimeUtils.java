package com.fy.fayou.utils;

import android.text.TextUtils;

import com.vondear.rxtool.RxTimeTool;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    public static String getYMDTime(String publishTime) {
        if (TextUtils.isEmpty(publishTime)) return "";
        return RxTimeTool.milliseconds2String(RxTimeTool.string2Milliseconds(publishTime), DEFAULT_SDF);
    }
}
