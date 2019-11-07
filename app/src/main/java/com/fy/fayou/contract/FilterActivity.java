package com.fy.fayou.contract;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.ButterKnife;

@Route(path = "/contract/filter")
public class FilterActivity extends BaseActivity {
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("婚内财产协议");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
