package com.fy.fayou;

import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.status.ViewState;

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

        setState(ViewState.ERROR);

//        ARouter.getInstance().build(UserService.getInstance().isFirstLaunch() ?
//                Constant.MAIN : Constant.FIRST_LAUNCH).navigation();
//
//        overridePendingTransition(0, 0);
//        finish();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
