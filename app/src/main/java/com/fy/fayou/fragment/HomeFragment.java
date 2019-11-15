package com.fy.fayou.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomeRecommendVPAdapter;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.Constant;
import com.fy.fayou.event.HomeRefreshEvent;
import com.fy.fayou.view.HomeViewpager;
import com.fy.fayou.view.TextSwitcherAnimation;
import com.meis.base.mei.base.BaseFragment;
import com.vondear.rxtool.RxImageTool;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {

    private final String[] mTitles = {
            "推荐"
    };

    private TextSwitcherAnimation textSwitcherAnimation;
    private Unbinder unbinder;
    private HomeRecommendVPAdapter mAdapter;
    private int mFloatSearchVisibleHeight;
    private boolean mFloatSearchVisible = false;

    @BindView(R.id.switcher)
    TextSwitcher switcher;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tab_layout)
    LinearLayout tabParentLayout;
    @BindView(R.id.tab_top_spacing)
    View tabTopSpacing;
    @BindView(R.id.iv_float_search)
    ImageView ivFloatSearch;
    @BindView(R.id.top_search_layout)
    LinearLayout topSearchLayout;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
        viewpager.setAdapter(mAdapter = new HomeRecommendVPAdapter(getChildFragmentManager(), mTitles));
        tab.setViewPager(viewpager);

        mAdapter.setOnScrollClashListener(isScroll -> viewpager.setScroll(isScroll));

        appBarLayout.addOnOffsetChangedListener(this);

        mFloatSearchVisibleHeight = -RxImageTool.dp2px(50);
    }

    @Override
    protected void initData() {
        ivFloatSearch.setOnClickListener(v -> {
            ARouter.getInstance().build(Constant.HOME_SEARCH).navigation();
        });
        topSearchLayout.setOnClickListener(v -> {
            ivFloatSearch.performClick();
        });

        initSwitcher();
    }

    private void initSwitcher() {
        List<String> hintList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            hintList.add("电信诈骗");
        }
        switcher.setFactory(() -> {
            TextView tv = new TextView(getActivity());
            tv.setTextColor(getActivity().getResources().getColor(R.color.color_a0a0a0));
            tv.setTextSize(16);
            tv.post(() -> {
                ((FrameLayout.LayoutParams) tv.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
            });
            return tv;
        });
        textSwitcherAnimation = new TextSwitcherAnimation(switcher, hintList);

        switcher.postDelayed(() -> {
            textSwitcherAnimation.create();
        }, 400);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBarLayout.removeOnOffsetChangedListener(this);
        unbinder.unbind();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int[] loc = new int[2];
        tabParentLayout.getLocationInWindow(loc);
        tabTopSpacing.setBackgroundColor(getResources().getColor(loc[1] <= 0 ? R.color.color_ffffff : R.color.color_f5f5f5));

        FloatSearchVisible();
    }

    private void FloatSearchVisible() {
        int[] location = new int[2];
        topSearchLayout.getLocationInWindow(location);
        if (!mFloatSearchVisible && location[1] <= mFloatSearchVisibleHeight) {
            mFloatSearchVisible = true;
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
            animator.setDuration(200);
            animator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                ivFloatSearch.setScaleX(value);
                ivFloatSearch.setScaleY(value);
            });
            animator.start();
        }

        if (mFloatSearchVisible && location[1] > mFloatSearchVisibleHeight) {
            mFloatSearchVisible = false;
            ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0f);
            animator.setDuration(200);
            animator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                ivFloatSearch.setScaleX(value);
                ivFloatSearch.setScaleY(value);
            });
            animator.start();
        }
    }

    public void offsetTop() {
        CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
            if (topAndBottomOffset != 0) {
                appBarLayoutBehavior.setTopAndBottomOffset(0);
            }
        }

        // 发送事件 刷新首页
        EventBus.getDefault().post(new HomeRefreshEvent());

        FloatSearchVisible();
    }

    @Override
    public void onDestroy() {
        textSwitcherAnimation.onDestroy();
        super.onDestroy();
    }

    @OnClick({R.id.iv_column0, R.id.iv_column1, R.id.iv_column2, R.id.iv_column3,
            R.id.iv_column4, R.id.iv_column5, R.id.tab_top_spacing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_column0:
                ARoute.jumpModule(ARoute.LEGAL_TYPE);
                break;
            case R.id.iv_column1:
                ARoute.jumpModule(ARoute.JUDICIAL_TYPE);
                break;
            case R.id.iv_column2:
                ARoute.jumpModule(ARoute.GUIDE_TYPE);
                break;
            case R.id.iv_column3:
                ARoute.jumpModule(ARoute.JUDGE_TYPE);
                break;
            case R.id.iv_column4:
                ARoute.jumpModule(ARoute.TEMPLATE_TYPE);
                break;
            case R.id.iv_column5:
                ARoute.jumpModule(ARoute.BOOKS_TYPE);
                break;
            case R.id.tab_top_spacing:
                break;
        }
    }
}
