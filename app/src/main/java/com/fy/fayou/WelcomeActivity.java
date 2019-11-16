package com.fy.fayou;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.meis.base.mei.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        ARouter.getInstance().build(UserService.getInstance().isFirstLaunch() ?
                Constant.MAIN : Constant.FIRST_LAUNCH).navigation();

        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
