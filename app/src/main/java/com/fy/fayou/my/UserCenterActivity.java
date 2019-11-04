package com.fy.fayou.my;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.my.fragment.UserCenterFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

@Route(path = "/user/center")
public class UserCenterActivity extends BaseActivity {
    @Override
    protected void initView() {
        Eyes.translucentStatusBar(this, true);
    }

    @Override
    protected void initData() {
        if (findFragment(UserCenterFragment.class) == null) {
            loadRootFragment(R.id.fl_container, UserCenterFragment.newInstance());
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_user_center;
    }
}
