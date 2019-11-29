package com.fy.fayou.forum.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomeForumVPAdapter;
import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


@Route(path = "/plate/follow")
public class PlateFollowActivity extends BaseActivity {

    HomeForumVPAdapter mAdapter;
    List<CategoryEntity> mCategoryEntities = new ArrayList<>();

    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setLeftBackListener(v -> {
            finish();
        });
    }

    @Override
    protected void initData() {

        CategoryEntity entity = new CategoryEntity();
        entity.categoryName = "关注";
        mCategoryEntities.add(entity);

        entity = new CategoryEntity();
        entity.categoryName = "热门";
        mCategoryEntities.add(entity);

        entity = new CategoryEntity();
        entity.categoryName = "法律法规";
        mCategoryEntities.add(entity);

        viewpager.setAdapter(mAdapter = new HomeForumVPAdapter(getSupportFragmentManager(), mCategoryEntities, 1));
        tab.setViewPager(viewpager);

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_forum_plate;
    }
}
