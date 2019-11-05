package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.my.adapter.UserCenterVPAdapter;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserCenterFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.tv_fan_num)
    TextView tvFanNum;
    @BindView(R.id.tv_praise_num)
    TextView tvPraiseNum;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_top_avatar)
    ImageView ivTopAvatar;
    @BindView(R.id.tv_top_name)
    TextView tvTopName;
    @BindView(R.id.tv_top_follow)
    TextView tvTopFollow;
    @BindView(R.id.top_header_layout)
    LinearLayout topHeaderLayout;

    Unbinder unbinder;

    private final String[] mTitles = {
            "资讯", "帖子"
    };

    UserCenterVPAdapter mAdapter;

    public static UserCenterFragment newInstance() {
        Bundle args = new Bundle();
        UserCenterFragment fragment = new UserCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());

        viewpager.setAdapter(mAdapter = new UserCenterVPAdapter(getChildFragmentManager(), mTitles));
        tab.setViewPager(viewpager);

        mAdapter.setOnScrollClashListener(isScroll -> viewpager.setScroll(isScroll));

        appBar.addOnOffsetChangedListener((appBarLayout, i) -> {

        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.tv_follow, R.id.iv_close, R.id.tv_top_follow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_follow:
                break;
            case R.id.iv_close:
                break;
            case R.id.tv_top_follow:
                break;
        }
    }
}
