package com.fy.fayou.person.setting;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.FYApplication;
import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.ExitLoginEvent;
import com.fy.fayou.utils.GlideCacheUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.view.RxToast;

import org.greenrobot.eventbus.EventBus;

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
        setLeftBackListener(v -> finish());
    }


    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.recommend_layout, R.id.suggest_layout, R.id.tv_cache, R.id.tv_service, R.id.tv_praise,
            R.id.about_layout, R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recommend_layout:
                RxToast.normal("敬请期待");
                break;
            case R.id.suggest_layout:
                ARouter.getInstance().build(Constant.PERSON_SUGGEST).navigation();
                break;
            case R.id.tv_cache:
                GlideCacheUtil.getInstance().clearImageAllCache(mContext);
                RxToast.normal("清理成功");
                break;
            case R.id.tv_service:
                break;
            case R.id.tv_praise:
                RxToast.normal("敬请期待~");
                break;
            case R.id.about_layout:
                ARouter.getInstance().build(Constant.ABOUT_MY).navigation();
                break;
            case R.id.tv_exit:
                UserService.getInstance().clearUser();
                ((FYApplication) getApplication()).clearTokenHeader();
                EventBus.getDefault().post(new ExitLoginEvent());
                finish();
                break;
        }
    }
}
