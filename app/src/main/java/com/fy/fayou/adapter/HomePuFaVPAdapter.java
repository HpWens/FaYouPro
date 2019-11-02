package com.fy.fayou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.pufa.fragment.HotVideoFragment;
import com.fy.fayou.pufa.fragment.NewsFragment;
import com.fy.fayou.pufa.fragment.SmallVideoFragment;

public class HomePuFaVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    public HomePuFaVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(final int i) {
        if (i == 0) {
            return NewsFragment.newInstance();
        } else if (i == 1) {
            return SmallVideoFragment.newInstance();
        } else {
            return HotVideoFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
