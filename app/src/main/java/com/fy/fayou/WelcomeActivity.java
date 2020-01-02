package com.fy.fayou;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.meis.base.mei.base.BaseActivity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

public class WelcomeActivity extends BaseActivity {

    private boolean isFirstLaunch = false;

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        isFirstLaunch = UserService.getInstance().isFirstLaunch();

        if (isFirstLaunch) {
            EasyHttp.get(ApiUrl.GET_SPLASH_DURATION)
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onError(ApiException e) {
                        }

                        @Override
                        public void onSuccess(String s) {
                        }
                    });
        }

        ARouter.getInstance().build(isFirstLaunch ? Constant.MAIN : Constant.FIRST_LAUNCH).navigation();

        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
