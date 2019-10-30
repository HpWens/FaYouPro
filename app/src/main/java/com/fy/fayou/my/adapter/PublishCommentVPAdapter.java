package com.fy.fayou.my.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.my.PublishFragment;

public class PublishCommentVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    public PublishCommentVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return PublishFragment.newInstance();
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
