package com.fy.fayou.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomePuFaVPAdapter;
import com.fy.fayou.common.Constant;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LearnFragment extends BaseFragment {

    private final String[] mTitles = {
            "新闻", "小视频", "热门视频"
    };

    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.iv_publish)
    ImageView ivPublish;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    Unbinder unbinder;

    private HomePuFaVPAdapter mAdapter;

    public static LearnFragment newInstance() {
        Bundle args = new Bundle();
        LearnFragment fragment = new LearnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());

        viewpager.setAdapter(mAdapter = new HomePuFaVPAdapter(getChildFragmentManager(), mTitles));
        tab.setViewPager(viewpager);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.learn_fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_publish)
    public void onClick() {
        ARouter.getInstance().build(Constant.NEWS_PUBLISH).navigation();
    }
}
