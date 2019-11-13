package com.fy.fayou.common;

import com.alibaba.android.arouter.launcher.ARouter;

public class ARoute {

    /**
     * 跳转到用户中心
     *
     * @param userId
     */
    public static void jumpUserCenter(String userId) {
        ARouter.getInstance()
                .build(Constant.USER_CENTER)
                .withString(Constant.Param.USER_ID, userId)
                .navigation();
    }

}
