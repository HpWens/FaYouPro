package com.fy.fayou.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomeForumVPAdapter;
import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    List<CategoryEntity> mCategoryEntities = new ArrayList<>();

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
        CategoryEntity entity = new CategoryEntity();
        entity.categoryName = "关注";
        mCategoryEntities.add(entity);

        entity = new CategoryEntity();
        entity.categoryName = "推荐";
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
}
