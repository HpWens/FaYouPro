package com.fy.fayou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.fragment.RecommendFragment;

public class HomeColumnVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private OnScrollClashListener mListener;

    public HomeColumnVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(final int i) {
        return RecommendFragment.newInstance().setOnScrollClashListener(new OnScrollClashListener() {
            @Override
            public void onScroll(boolean isScroll) {
                if (mListener != null) {
                    mListener.onScroll(isScroll);
                }
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
