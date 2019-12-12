package com.fy.fayou.person.phone;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.utils.RegexUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxKeyboardTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/person/reset_phone")
public class ResetPhoneActivity extends BaseActivity {

    @Autowired
    public String mobile = "";

    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.iv_clean_phone)
    ImageView ivCleanPhone;
    @BindView(R.id.btn_send)
    Button btnSend;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("请输入新手机号");
        setLeftBackListener((v) -> finish());
    }

    @Override
    protected void initData() {
        String formatMobile = "";
        if (!TextUtils.isEmpty(mobile)) {
            formatMobile = "+86" + RxDataTool.hideMobilePhone4(mobile);
            tvHint.setText(getString(R.string.mobile_bind_hint, formatMobile));
        } else {
            tvHint.setText(getString(R.string.empty_mobile_bind_hint));
        }

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ivCleanPhone.getVisibility() == View.GONE) {
                    ivCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivCleanPhone.setVisibility(View.GONE);
                }

                if (s.length() == 11 && RegexUtils.checkMobile(s.toString())) {
                    btnSend.setEnabled(true);
                } else {
                    btnSend.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_reset_phone;
    }

    @OnClick({R.id.iv_clean_phone, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                etMobile.setText("");
                break;
            case R.id.btn_send:
                String mob = etMobile.getText().toString();
                if (mob.equals(mobile)) {
                    Toast.makeText(mContext, "您输入的手机号不能与原手机号一样", Toast.LENGTH_SHORT).show();
                } else {
                    if (RegexUtils.checkMobile(mob)) {
                        RxKeyboardTool.hideSoftInput(mContext, etMobile);
                        ARouter.getInstance().build(Constant.PERSON_VERIFY_CODE)
                                .withString(Constant.Param.MOBILE, mob)
                                .navigation();
                        finish();
                    } else {
                        Toast.makeText(mContext, "您输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
