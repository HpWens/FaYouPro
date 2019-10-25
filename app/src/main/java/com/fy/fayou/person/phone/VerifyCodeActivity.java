package com.fy.fayou.person.phone;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.ButterKnife;

@Route(path = "/person/verify_code")
public class VerifyCodeActivity extends BaseActivity {
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("请输入验证码");
        setLeftBackListener((v) -> finish());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_verify_code;
    }
}
