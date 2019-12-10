package com.fy.fayou.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomeForumVPAdapter;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.LoginSuccessOrExitEvent;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForumFragment extends BaseFragment {

    @BindView(R.id.iv_publish)
    ImageView ivPublish;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    Unbinder unbinder;

    HomeForumVPAdapter mAdapter;
    List<PlateEntity> mCategoryEntities = new ArrayList<>();

    public static ForumFragment newInstance() {
        Bundle args = new Bundle();
        ForumFragment fragment = new ForumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
    }

    @Override
    protected void initData() {
        PlateEntity entity = new PlateEntity();
        entity.name = "关注";
        mCategoryEntities.add(entity);

        entity = new PlateEntity();
        entity.name = "推荐";
        mCategoryEntities.add(entity);


        viewpager.setAdapter(mAdapter = new HomeForumVPAdapter(getChildFragmentManager(), mCategoryEntities));
        tab.setViewPager(viewpager);

        // 跳转到关注TAB登陆验证
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    UserService.getInstance().checkLoginAndJump();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.forum_fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!UserService.getInstance().isLogin()) {
            // 未登录显示推荐
            tab.setCurrentTab(1);
        }
    }

    @OnClick({R.id.iv_search, R.id.iv_publish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                ARoute.jumpSearch(true);
                break;
            case R.id.iv_publish:
                ARoute.jumpPlateSelect();
                break;
        }
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessOrExitEvent event) {
        // 登陆成功后刷新-关注tab
        if (tab != null) {
            tab.setCurrentTab(0);
        }
    }

}
