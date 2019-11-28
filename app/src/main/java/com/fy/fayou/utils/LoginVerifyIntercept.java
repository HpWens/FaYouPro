package com.fy.fayou.utils;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;

//  导致转场问题
// @Interceptor(priority = 2)
public class LoginVerifyIntercept implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getPath().equals(Constant.REPORT)) {
            if (UserService.getInstance().checkLoginAndJump()) {
                callback.onContinue(postcard);
            } else {
                callback.onInterrupt(null);
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {

    }
}
