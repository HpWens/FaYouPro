package com.fy.fayou.person.setting;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.kyleduo.switchbutton.SwitchButton;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/person/setting")
public class SettingActivity extends BaseActivity {

    @BindView(R.id.recommend_layout)
    LinearLayout recommendLayout;
    @BindView(R.id.suggest_layout)
    LinearLayout suggestLayout;
    @BindView(R.id.skin_btn)
    SwitchButton skinBtn;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.tv_praise)
    TextView tvPraise;
    @BindView(R.id.about_layout)
    LinearLayout aboutLayout;
    @BindView(R.id.tv_exit)
    TextView tvExit;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("设置");
        setLeftBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.recommend_layout, R.id.suggest_layout, R.id.tv_cache, R.id.tv_service, R.id.tv_praise, R.id.about_layout, R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recommend_layout:
                break;
            case R.id.suggest_layout:
                break;
            case R.id.tv_cache:
                break;
            case R.id.tv_service:
                break;
            case R.id.tv_praise:
                break;
            case R.id.about_layout:
                break;
            case R.id.tv_exit:
                break;
        }
    }
}
