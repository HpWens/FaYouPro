package com.fy.fayou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.forum.fragment.FollowFragment;
import com.fy.fayou.forum.fragment.PlateFollowFragment;

import java.util.List;

public class HomeForumVPAdapter extends FragmentPagerAdapter {

    List<CategoryEntity> mTags;

    // 0 论坛首页  1 板块关注
    int forumType = 0;

    public HomeForumVPAdapter(FragmentManager fm, List<CategoryEntity> tags) {
        super(fm);
        this.mTags = tags;
    }

    public HomeForumVPAdapter(FragmentManager fm, List<CategoryEntity> tags, int forumType) {
        super(fm);
        mTags = tags;
        this.forumType = forumType;
    }

    @Override
    public Fragment getItem(final int i) {
        if (forumType == 0) {
            return FollowFragment.newInstance(i == 1);
        } else {
            return PlateFollowFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTags.get(position).categoryName;
    }

    public List<CategoryEntity> getTags() {
        return mTags;
    }
}
