package com.fy.fayou.my.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.fragment.RecommendFragment;
import com.fy.fayou.my.fragment.UserCenterPostFragment;

public class UserCenterVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private OnScrollClashListener mListener;

    private String mUserId;

    public UserCenterVPAdapter(FragmentManager fm, String[] titles, String userId) {
        super(fm);
        this.mUserId = userId;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return RecommendFragment.newInstance("", false, true, mUserId).setOnScrollClashListener(isScroll -> {
                if (mListener != null) mListener.onScroll(isScroll);
            });
        }
        return UserCenterPostFragment.newInstance(mUserId).setOnScrollClashListener(isScroll -> {
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