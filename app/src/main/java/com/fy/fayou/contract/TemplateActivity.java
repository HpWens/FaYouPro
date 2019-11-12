package com.fy.fayou.contract;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.home.adapter.WantedVPAdapter;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/contract/template")
public class TemplateActivity extends BaseActivity {

    @BindView(R.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    WantedVPAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true, false);
        tvCenterTitle.setText("合同模板");
    }

    @Override
    protected void initData() {
        viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), new ArrayList<>(), WantedVPAdapter.TEMPLATE));
        tab.setViewPager(viewpager);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_home_wanted;
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
