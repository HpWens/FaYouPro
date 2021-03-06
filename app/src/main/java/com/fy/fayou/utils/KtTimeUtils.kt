package com.fy.fayou.utils

import android.text.TextUtils
import com.vondear.rxtool.RxTimeTool
import java.text.SimpleDateFormat
import java.util.*

object KtTimeUtils {

    private val DEFAULT_SDF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private val DEFAULT_YMDHM = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());

    fun getYMDTime(publishTime: String): String {
        return if (TextUtils.isEmpty(publishTime)) "" else RxTimeTool.milliseconds2String(RxTimeTool.string2Milliseconds(publishTime), DEFAULT_SDF)
    }

    fun getYMDHMTime(publishTime: String): String {
        return if (TextUtils.isEmpty(publishTime)) "" else RxTimeTool.milliseconds2String(RxTimeTool.string2Milliseconds(publishTime), DEFAULT_YMDHM)
    }

}