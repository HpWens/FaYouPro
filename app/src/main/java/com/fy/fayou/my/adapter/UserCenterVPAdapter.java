package com.fy.fayou.my.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.fragment.ColumnFragment;
import com.fy.fayou.fragment.RecommendFragment;

public class UserCenterVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private OnScrollClashListener mListener;

    public UserCenterVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return RecommendFragment.newInstance("", false, true).setOnScrollClashListener(isScroll -> {
                if (mListener != null) mListener.onScroll(isScroll);
            });
        }
        return ColumnFragment.newInstance().setOnScrollClashListener(isScroll -> {
            if (mListener != null) mListener.onScroll(isScroll);
        });
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


    public void setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
    }
}