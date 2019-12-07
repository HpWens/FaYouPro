package com.fy.fayou.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomeForumVPAdapter;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;

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
}
