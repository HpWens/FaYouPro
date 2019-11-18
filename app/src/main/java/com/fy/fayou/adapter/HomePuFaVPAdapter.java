package com.fy.fayou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.fragment.RecommendFragment;

import java.util.List;

public class HomePuFaVPAdapter extends FragmentPagerAdapter {

    List<CategoryEntity> mTags;

    public HomePuFaVPAdapter(FragmentManager fm, List<CategoryEntity> tags) {
        super(fm);
        this.mTags = tags;
    }

    @Override
    public Fragment getItem(final int i) {
        return RecommendFragment.newInstance();
//        if (i == 0) {
//            return NewsFragment.newInstance();
//        } else if (i == 1) {
//            return SmallVideoFragment.newInstance();
//        } else {
//            return HotVideoFragment.newInstance();
//        }
    }

    @Override
    public int getCount() {
        return mTags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTags.get(position).categoryName;
    }
}
