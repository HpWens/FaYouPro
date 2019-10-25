package com.fy.fayou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.fragment.ColumnFragment;
import com.fy.fayou.fragment.RecommendFragment;

public class HomeRecommendVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private OnScrollClashListener mListener;

    public HomeRecommendVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return RecommendFragment.newInstance().setOnScrollClashListener(new OnScrollClashListener() {
                @Override
                public void onScroll(boolean isScroll) {
                    if (mListener != null) mListener.onScroll(isScroll);
                }
            });
        }
        return ColumnFragment.newInstance().setOnScrollClashListener(new OnScrollClashListener() {
            @Override
            public void onScroll(boolean isScroll) {
                if (mListener != null) mListener.onScroll(isScroll);
            }
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
