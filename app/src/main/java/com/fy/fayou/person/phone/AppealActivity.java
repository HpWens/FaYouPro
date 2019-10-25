package com.fy.fayou.person.phone;

import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/person/appeal")
public class AppealActivity extends BaseActivity {

    @BindView(R.id.btn_appeal)
    Button btnAppeal;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("申诉");
        setLeftBackListener((v) -> finish());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_appeal;
    }

    @OnClick(R.id.btn_appeal)
    public void onClick() {
    }
}
