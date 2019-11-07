package com.fy.fayou.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.contract.TemplateFragment;
import com.fy.fayou.home.fragment.WantedFragment;

public class WantedVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private int type = 1;

    public static int TEMPLATE = 2;
    public static int WANTED = 1;

    public WantedVPAdapter(FragmentManager fm, String[] titles, int type) {
        super(fm);
        mTitles = titles;
        this.type = type;
    }

    public WantedVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(final int i) {
        if (type == WANTED) {
            return WantedFragment.newInstance();
        } else if (type == TEMPLATE) {
            return TemplateFragment.newInstance();
        }
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