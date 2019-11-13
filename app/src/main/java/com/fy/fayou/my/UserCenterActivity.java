package com.fy.fayou.my;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.my.fragment.UserCenterFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

@Route(path = "/user/center")
public class UserCenterActivity extends BaseActivity {

    @Autowired(name = "userId")
    public String userId = "";

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        Eyes.translucentStatusBar(this, true, false);
    }

    @Override
    protected void initData() {
        if (findFragment(UserCenterFragment.class) == null) {
            loadRootFragment(R.id.fl_container, UserCenterFragment.newInstance(userId));
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_user_center;
    }
}
