package com.fy.fayou.my.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.my.fragment.CollectFragment;
import com.fy.fayou.my.fragment.NewsListFragment;

public class NewsListVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private int type = 1;

    public static final int COLLECT_TYPE = 2;

    public NewsListVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    public NewsListVPAdapter(FragmentManager fm, String[] titles, int type) {
        super(fm);
        mTitles = titles;
        this.type = type;
    }

    @Override
    public Fragment getItem(int i) {
        if (type == 1) {
            return NewsListFragment.newInstance();
        } else {
            return CollectFragment.newInstance();
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
