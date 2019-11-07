package com.fy.fayou.person.phone;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxDataTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/person/phone_bind_success")
public class PhoneBindSuccessActivity extends BaseActivity {

    @Autowired
    public String mobile = "";

    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.btn_complete)
    Button btnComplete;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("更换完成");
        setLeftBackListener((v) -> finish());
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(mobile)) {
            tvMobile.setText(RxDataTool.hideMobilePhone4(mobile));
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_phone_bind_success;
    }

    @OnClick(R.id.btn_complete)
    public void onClick() {
        finish();
    }
}
