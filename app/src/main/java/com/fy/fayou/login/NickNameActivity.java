package com.fy.fayou.login;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.ButterKnife;

@Route(path = "/login/nickname")
public class NickNameActivity extends BaseActivity {
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_create_nickname;
    }
}
