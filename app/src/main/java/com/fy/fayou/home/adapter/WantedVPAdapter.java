package com.fy.fayou.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.home.fragment.WantedFragment;

public class WantedVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    public WantedVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(final int i) {
        return WantedFragment.newInstance();
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