package com.fy.fayou.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.home.adapter.WantedVPAdapter;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/home/wanted")
public class WantedActivity extends BaseActivity {

    private final String[] mTitles = {
            "A级通缉(100)", "B级通缉(100)"
    };

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    WantedVPAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true, false);
    }

    @Override
    protected void initData() {
        viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), mTitles));
        tab.setViewPager(viewpager);

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_home_wanted;
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_right:

                break;
        }
    }
}
